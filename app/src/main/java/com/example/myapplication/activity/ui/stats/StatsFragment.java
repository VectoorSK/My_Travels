package com.example.myapplication.activity.ui.stats;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Country;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsFragment extends Fragment {

    private List<Country> countryList;
    private List<Travel> travelList;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        progressBar = root.findViewById(R.id.progress_circular);
        loadCountry(root);
        return root;
    }

    public void loadCountry(View view) {

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
                progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
                travelList = response.body();
                drawView();
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawView() {
        List<String> eur = new ArrayList<>();
        List<String> afr = new ArrayList<>();
        List<String> asie = new ArrayList<>();
        List<String> amn = new ArrayList<>();
        List<String> ams = new ArrayList<>();
        List<String> oce = new ArrayList<>();
        List<String> ant = new ArrayList<>();
        List<String> all = new ArrayList<>();
        for (Travel travel : travelList) {
            if (travel.getContinent().matches("Europe")) {
                if(!eur.contains(travel.getCountry())) {
                    eur.add(travel.getCountry());
                }
            } else if (travel.getContinent().matches("Afrique")) {
                if(!afr.contains(travel.getCountry())) {
                    afr.add(travel.getCountry());
                }
            } else if (travel.getContinent().matches("Asie")) {
                if(!asie.contains(travel.getCountry())) {
                    asie.add(travel.getCountry());
                }
            } else if (travel.getContinent().matches("Amerique du Nord")) {
                if(!amn.contains(travel.getCountry())) {
                    amn.add(travel.getCountry());
                }
            } else if (travel.getContinent().matches("Amerique du Sud")) {
                if(!ams.contains(travel.getCountry())) {
                    ams.add(travel.getCountry());
                }
            } else if (travel.getContinent().matches("Océanie")) {
                if(!oce.contains(travel.getCountry())) {
                    oce.add(travel.getCountry());
                }
            } else {
                if(!ant.contains(travel.getCountry())) {
                    ant.add(travel.getCountry());
                }
            }
            if(!all.contains(travel.getCountry())) {
                all.add(travel.getCountry());
            }
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

        // Total pays visités
        TextView pays_prct = getView().findViewById(R.id.stat_percent);
        pays_prct.setText(String.valueOf(all.size()));

        // Total distance parcouru
        TextView tot_dist = getView().findViewById(R.id.stat_dist);
        String tot_dist_str = Math.round(calculTotalDist()) + " km";
        tot_dist.setText(tot_dist_str);

        // Distance Max atteinte
        TextView farest_dist = getView().findViewById(R.id.stat_farest_pnt);
        String farest_dist_str = calculMostFar()[3] + " km";
        farest_dist.setText(farest_dist_str);

        // Total temps en voyage
        TextView time_spend = getView().findViewById(R.id.stat_time);
        String time = "";
        try {
            time = calculTimeSpend();
        } catch (Exception e) {
            e.printStackTrace();
        }
        time_spend.setText(time);


        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();

        // Pays le plus visité (nombre)
        TextView most_visited = (TextView) getView().findViewById(R.id.stat_most_visited);
        String most_visited_str = calculMostVisited()[0] + "\n(" + calculMostVisited()[2] + " fois)";
        most_visited.setText(most_visited_str);
        ImageView most_visited_icon = (ImageView) getView().findViewById(R.id.stat_ic_most_visited);
        picasso.load(calculMostVisited()[1])
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(most_visited_icon);

        // Pays avec le plus de distance
        TextView most_dist = getView().findViewById(R.id.stat_most_dist);
        String most_dist_str = calculMostDist()[0] + "\n(" + calculMostDist()[2] + " km)";
        most_dist.setText(most_dist_str);
        ImageView most_dist_icon = getView().findViewById(R.id.stat_ic_most_dist);
        picasso.load(calculMostDist()[1])
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(most_dist_icon);

        // Pays le plus loin
        TextView most_far = getView().findViewById(R.id.stat_most_far);
        String most_far_str = calculMostFar()[0] + "\n(" + calculMostFar()[1] + ")";
        most_far.setText(most_far_str);
        ImageView most_far_icon = getView().findViewById(R.id.stat_ic_most_far);
        picasso.load(calculMostFar()[2])
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(most_far_icon);

        // Pays le plus visité (temps)
        String most_time_count = "", most_time_flag = "", most_time_day = "";
        try {
            most_time_count = calculMostTime()[0];
            most_time_flag = calculMostTime()[1];
            most_time_day = calculMostTime()[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView most_time_spend = getView().findViewById(R.id.stat_most_time);
        String most_time_str = most_time_count + "\n(" + most_time_day + " jours)";
        most_time_spend.setText(most_time_str);
        ImageView most_time_icon = getView().findViewById(R.id.stat_ic_most_time);
        picasso.load(most_time_flag)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(most_time_icon);

        TextView eur_prct = getView().findViewById(R.id.stat_eur_percent);
        String eur_str = eur.size() + "/" + tot_eur;
        eur_prct.setText(eur_str);
        TextView afr_prct = getView().findViewById(R.id.stat_afr_percent);
        String afr_str = afr.size() + "/" + tot_afr;
        afr_prct.setText(afr_str);
        TextView asie_prct = getView().findViewById(R.id.stat_asie_percent);
        String asie_str = asie.size() + "/" + tot_asie;
        asie_prct.setText(asie_str);
        TextView amn_prct = getView().findViewById(R.id.stat_amn_percent);
        String amn_str = amn.size() + "/" + tot_amn;
        amn_prct.setText(amn_str);
        TextView ams_prct = getView().findViewById(R.id.stat_ams_percent);
        String ams_str = ams.size() + "/" + tot_ams;
        ams_prct.setText(ams_str);
        TextView oce_prct = getView().findViewById(R.id.stat_oce_percent);
        String oce_str = oce.size() + "/" + tot_oce;
        oce_prct.setText(oce_str);
        TextView ant_prct = getView().findViewById(R.id.stat_ant_percent);
        String ant_str = ant.size() + "/" + tot_ant;
        ant_prct.setText(ant_str);

        drawContinentChart();
    }

    private void drawContinentChart() {
        int eur = 0, afr = 0, asie = 0, amn = 0, ams = 0, oce = 0, ant = 0, tot = 0;
        for (Travel travel : travelList) {
            eur = travel.getContinent().matches("Europe") ? eur + 1 : eur;
            afr = travel.getContinent().matches("Afrique") ? afr + 1 : afr;
            asie = travel.getContinent().matches("Asie") ? asie + 1 : asie;
            amn = travel.getContinent().matches("Amerique du Nord") ? amn + 1 : amn;
            ams = travel.getContinent().matches("Amerique du Sud") ? ams + 1 : ams;
            oce = travel.getContinent().matches("Océanie") ? oce + 1 : oce;
            ant = travel.getContinent().matches("Antartique") ? ant + 1 : ant;
            tot++;
        }
        int[] color_cont = { 0xff3b5998, 0xffffd355, 0xffff9466, 0xfff44336, 0xff0392cf, 0xff028900, 0xffffffff };
        int[] nb_cont = { eur, afr, asie, amn, ams, oce, ant };
        String[] cont = { "eur", "afr", "asie", "amn", "ams", "oce", "ant" };
        PieChartView pieChartView = getView().findViewById(R.id.stat_chart);
        List<SliceValue> pieData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (nb_cont[i] != 0) {
                float percent = nb_cont[i] * 100 / tot;
                SliceValue slice = new SliceValue(percent, color_cont[i]).setLabel(String.valueOf(nb_cont[i]));
                pieData.add(slice);
            }
            if (i < 6) {
                int id = getResources().getIdentifier("leg_" + cont[i] + "_nb", "id", getContext().getPackageName());
                TextView textView = getView().findViewById(id);
                textView.setText(String.valueOf(nb_cont[i]));
            }
        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabelsOnlyForSelected(true);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
    }

    private double calculTotalDist() {
        double kms = 0;
        for (Travel travel : travelList) {
            List<Step> stepList = travel.getSteps_array();
            for (Step step : stepList) {
                double lat1, lng1, lat2, lng2;
                lat1 = step.getLat();
                lng1 = step.getLng();
                if (step.getId() != stepList.size() - 1) {
                    lat2 = stepList.get(step.getId() + 1).getLat();
                    lng2 = stepList.get(step.getId() + 1).getLng();
                } else {
                    lat2 = stepList.get(0).getLat();
                    lng2 = stepList.get(0).getLng();
                }
                kms += distKmFromCoord(lat1, lng1, lat2, lng2);
            }
        }
        return kms;
    }

    private String calculTimeSpend() throws Exception {
        long diff_in_sec = 0;
        for (Travel travel : travelList) {
            Date date_start = new SimpleDateFormat("yyyy/MM/dd").parse(travel.getDate_from());
            Date date_end = new SimpleDateFormat("yyyy/MM/dd").parse(travel.getDate_to());
            diff_in_sec += Math.abs(date_end.getTime() - date_start.getTime()) / 1000;
        }
        int nb_day = (int) TimeUnit.SECONDS.toDays(diff_in_sec);
        return String.valueOf(nb_day) + " jours";
    }

    private String[] calculMostVisited() {
        Map<String, Integer> map = new HashMap<>();
        for (Travel travel : travelList) {
            Integer val = map.get(travel.getCountry());
            map.put(travel.getCountry(), val == null ? 1 : val + 1);
        }
        Map.Entry<String, Integer> max = null;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        String[] res = new String[3];
        res[0] = max.getKey();
        res[2] = String.valueOf(max.getValue());
        for (Travel travel : travelList) {
            if (travel.getCountry().matches(res[0])) {
                res[1] = travel.getFlag();
                break;
            }
        }
        return res;
    }

    private String[] calculMostDist() {
        String country = "";
        String flag = "";
        double max_dist = 0;
        for (Travel travel : travelList) {
            double kms = 0;
            List<Step> stepList = travel.getSteps_array();
            for (Step step : stepList) {
                double lat1, lng1, lat2, lng2;
                lat1 = step.getLat();
                lng1 = step.getLng();
                if (step.getId() != stepList.size() - 1) {
                    lat2 = stepList.get(step.getId() + 1).getLat();
                    lng2 = stepList.get(step.getId() + 1).getLng();
                } else {
                    lat2 = stepList.get(0).getLat();
                    lng2 = stepList.get(0).getLng();
                }
                kms += distKmFromCoord(lat1, lng1, lat2, lng2);
            }
            if (max_dist < kms) {
                max_dist = kms;
                country = travel.getCountry();
                flag = travel.getFlag();
            }
        }
        String[] res = new String[3];
        res[0] = country;
        res[1] = flag;
        res[2] = String.format("%.0f", max_dist);
        return res;
    }

    private String[] calculMostFar() {
        String country = "";
        String city = "";
        String flag = "";
        double max_dist = 0;
        for (Travel travel : travelList) {
            List<Step> stepList = travel.getSteps_array();
            for (Step step : stepList) {
                double lat1, lng1, lat2, lng2;
                lat1 = 48.852310;
                lng1 = 2.146690;
                lat2 = step.getLat();
                lng2 = step.getLng();
                if (distKmFromCoord(lat1, lng1, lat2, lng2) > max_dist) {
                    max_dist = distKmFromCoord(lat1, lng1, lat2, lng2);
                    city = step.getCity();
                    country = travel.getCountry();
                    flag = travel.getFlag();
                }
            }
        }
        String[] res = new String[4];
        res[0] = country;
        res[1] = city;
        res[2] = flag;
        res[3] = String.format("%.0f", max_dist);
        return res;
    }

    private String[] calculMostTime() throws Exception {

        String country = "";
        String flag = "";
        long max_time = 0;
        for (Travel travel : travelList) {
            Date date_start = new SimpleDateFormat("yyyy/MM/dd").parse(travel.getDate_from());
            Date date_end = new SimpleDateFormat("yyyy/MM/dd").parse(travel.getDate_to());
            long diff = Math.abs(date_end.getTime() - date_start.getTime()) / 1000;
            if (max_time < diff) {
                max_time = diff;
                country = travel.getCountry();
                flag = travel.getFlag();
            }
        }
        int nb_day = (int) TimeUnit.SECONDS.toDays(max_time);

        String[] res = new String[3];
        res[0] = country;
        res[1] = flag;
        res[2] = String.valueOf(nb_day);
        return res;
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double distKmFromCoord(double lat1, double lng1, double lat2, double lng2) {
        double earthRadiusKm = 6371;
        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lng2-lng1);
        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
}