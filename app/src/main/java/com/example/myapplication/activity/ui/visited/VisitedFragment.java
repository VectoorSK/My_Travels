package com.example.myapplication.activity.ui.visited;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.DetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.VisitedMapActivity;
import com.example.myapplication.activity.ui.map.MapViewModel;
import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Country;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedFragment extends Fragment {

    private List<Country> countryList;
    private List<Travel> travelList;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loadCountry();

        View root = inflater.inflate(R.layout.fragment_visited, container, false);
        return root;
    }

    public void loadCountry() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Country>> call = service.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                countryList = response.body();
                loadTravel();
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadTravel() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
                travelList = response.body();
                drawView();
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawView() {
        List<String> codeList = new ArrayList<>();
        int nb_eur = 0, nb_afr = 0, nb_asie = 0, nb_amn = 0, nb_ams = 0, nb_oce = 0, nb_ant = 0, nb_tot = 0;
        for (Travel travel : travelList) {
            if (!codeList.contains(travel.getCode())) {
                codeList.add(travel.getCode());
            }
            if (travel.getContinent().matches("Europe")) {
                nb_eur++;
            } else if (travel.getContinent().matches("Afrique")) {
                nb_afr++;
            } else if (travel.getContinent().matches("Asie")) {
                nb_asie++;
            } else if (travel.getContinent().matches("Amerique du Nord")) {
                nb_amn++;
            } else if (travel.getContinent().matches("Amerique du Sud")) {
                nb_ams++;
            } else if (travel.getContinent().matches("Océanie")) {
                nb_oce++;
            } else {
                nb_ant++;
            }
            nb_tot++;
        }
        int tot_eur = 0, tot_afr = 0, tot_asie = 0, tot_amn = 0, tot_ams = 0, tot_oce = 0, tot_ant = 0;
        for (Country country : countryList) {
            if (country.getContinent().matches("europe")) {
                tot_eur++;
            } else if (country.getContinent().matches("afrique")) {
                tot_afr++;
            } else if (country.getContinent().matches("asie")) {
                tot_asie++;
            } else if (country.getContinent().matches("amerique_nord")) {
                tot_amn++;
            } else if (country.getContinent().matches("amerique_sud")) {
                tot_ams++;
            } else if (country.getContinent().matches("oceanie")) {
                tot_oce++;
            } else {
                tot_ant++;
            }
        }

        TextView pays_prct = (TextView) getView().findViewById(R.id.stat_percent);
        pays_prct.setText(pays_prct.getText() + String.valueOf(codeList.size()) + "/" + String.valueOf(countryList.size()));

        TextView eur_prct = (TextView) getView().findViewById(R.id.stat_eur_percent);
        eur_prct.setText(eur_prct.getText() + String.valueOf(nb_eur) + "/" + String.valueOf(tot_eur));
        TextView afr_prct = (TextView) getView().findViewById(R.id.stat_afr_percent);
        afr_prct.setText(afr_prct.getText() + String.valueOf(nb_afr) + "/" + String.valueOf(tot_afr));
        TextView asie_prct = (TextView) getView().findViewById(R.id.stat_asie_percent);
        asie_prct.setText(asie_prct.getText() + String.valueOf(nb_asie) + "/" + String.valueOf(tot_asie));
        TextView amn_prct = (TextView) getView().findViewById(R.id.stat_amn_percent);
        amn_prct.setText(amn_prct.getText() + String.valueOf(nb_amn) + "/" + String.valueOf(tot_amn));
        TextView ams_prct = (TextView) getView().findViewById(R.id.stat_ams_percent);
        ams_prct.setText(ams_prct.getText() + String.valueOf(nb_ams) + "/" + String.valueOf(tot_ams));
        TextView oce_prct = (TextView) getView().findViewById(R.id.stat_oce_percent);
        oce_prct.setText(oce_prct.getText() + String.valueOf(nb_oce) + "/" + String.valueOf(tot_oce));
        TextView ant_prct = (TextView) getView().findViewById(R.id.stat_ant_percent);
        ant_prct.setText(ant_prct.getText() + String.valueOf(nb_ant) + "/" + String.valueOf(tot_ant));

        int[] arr_nb_cont = { nb_eur, nb_afr, nb_asie, nb_amn, nb_ams, nb_oce, nb_ant };
        String[] arr_title_cont = { "Europe", "Afrique", "Asie", "Amérique du Nord", "Amérique du Sud", "Océanie", "Antartique" };
        int[] arr_clr_cont = { 0xff3b5998, 0xffffd355, 0xffff9466, 0xfff44336, 0xff0392cf, 0xff028900, 0xffffffff };
        drawContinentChart(nb_tot, arr_nb_cont, arr_title_cont, arr_clr_cont);
    }

    private void drawContinentChart(int nb_tot, int[] arr_nb_cont, String[] arr_title_cont, int[] arr_clr_cont) {
        PieChartView pieChartView = getView().findViewById(R.id.stat_chart);
        List<SliceValue> pieData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (arr_nb_cont[i] != 0) {
                double percent = arr_nb_cont[i] * 100 / nb_tot;
                //int colorID = getResources().getIdentifier("bluePalette" + id, "color", getPackageName());
                SliceValue slice = new SliceValue((float) percent, arr_clr_cont[i]);
                pieData.add(slice.setLabel(arr_title_cont[i]));
            }
        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelBackgroundEnabled(false);
        pieChartData.setValueLabelsTextColor(0xff000000);
        //pieChartData.setHasCenterCircle(true).setCenterText1("Répartition").setCenterText1FontSize(20);
        pieChartView.setPieChartData(pieChartData);
    }
}