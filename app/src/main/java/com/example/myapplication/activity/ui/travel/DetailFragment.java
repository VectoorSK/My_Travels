package com.example.myapplication.activity.ui.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.StepAdapter;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private StepAdapter adapter;
    private RecyclerView recyclerView;
    private List<Step> datalist;
    //ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        int id_step = Integer.parseInt(arguments.getString("id"));
        loadStep(id_step);
        ImageView map_btn = (ImageView) getView().findViewById(R.id.travel_map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTravelMaps();
            }
        });
    }


    private void loadStep(final int id) {
        final ProgressBar progressBar = getView().findViewById(R.id.progress_circular);
        // Create handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressBar.setVisibility(View.GONE);
                datalist = response.body().get(id).getSteps_array();
                generateSteps(datalist);

                String name = response.body().get(id).getCountry();
                String flag = response.body().get(id).getFlag();
                String from = response.body().get(id).getDate_from();
                String to = response.body().get(id).getDate_to();
                String desc = response.body().get(id).getDesc();
                showDetails(name, flag, from, to, desc);
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetails (String name, String flag, String from, String to, String desc) {

        TextView nameView = (TextView) getView().findViewById(R.id.det_name);
        nameView.setText(name.toUpperCase());

        TextView dateView = (TextView) getView().findViewById(R.id.det_date);
        dateView.setText(toDateFr(from));

        TextView dayView = (TextView) getView().findViewById(R.id.det_nb_day);
        String nb_day = "", plural;
        try {
            int nb = dayBetweenStrDates(from, to);
            plural = nb == 1 ? "" : "s";
            nb_day = nb + " jour" + plural;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dayView.setText(nb_day);

        TextView stepView = (TextView) getView().findViewById(R.id.det_step);
        plural = datalist.size() == 1 ? "" : "s";
        String step = datalist.size() + " étape" + plural;

        String kms = String.format("%.0f", calculKms());
        if (!kms.matches("0")) {
            step += " : " + kms + " km";
        }
        stepView.setText(step);

        ImageView flagView = (ImageView) getView().findViewById(R.id.det_flag);
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();
        picasso.load(flag)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(flagView);

        TextView descView = (TextView) getView().findViewById(R.id.det_desc);
        descView.setText(desc);
    }

    private double calculKms() {
        double kms = 0;
        for (Step step : datalist) {
            double lat1, lng1, lat2, lng2;
            lat1 = step.getLat();
            lng1 = step.getLng();
            if (step.getId() != datalist.size() - 1) {
                lat2 = datalist.get(step.getId() + 1).getLat();
                lng2 = datalist.get(step.getId() + 1).getLng();
            } else {
                lat2 = datalist.get(0).getLat();
                lng2 = datalist.get(0).getLng();
            }
            System.out.println("from " + step.getCity() + " = " + distKmFromCoord(lat1, lng1, lat2, lng2));
            kms += distKmFromCoord(lat1, lng1, lat2, lng2);
        }
        return kms;
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

    private void generateSteps(final List<Step> list) {
        recyclerView = getActivity().findViewById(R.id.step_recycler_view);
        adapter = new StepAdapter(getContext(), list, new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {
                openDetails(step);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private int dayBetweenStrDates (String str_d1, String str_d2) throws Exception {

        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(str_d1);
        Date date2 = new SimpleDateFormat("yyyy/MM/dd").parse(str_d2);
        int diff_in_day = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(date1.getTime() - date2.getTime()));

        return diff_in_day;
    }

    private void openDetails (Step step) {

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle arguments = new Bundle();

        String travel_id = getArguments().getString("id");
        arguments.putString( "travel_id" , travel_id);
        arguments.putString( "step_id" , String.valueOf(step.getId()));
        stepDetailFragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, stepDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openTravelMaps () {

        DetailMapFragment detailMapFragment = new DetailMapFragment();
        Bundle arguments = new Bundle();

        double[] listLatLng = new double[datalist.size()*2];
        int i = 0;
        for (Step step : datalist) {
            listLatLng[i] = step.getLat();
            listLatLng[i+1] = step.getLng();
            i+=2;
        }

        arguments.putDoubleArray("list_lat_lng", listLatLng);
        detailMapFragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detailMapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public String toDateFr(String date) {

        String[] date_array = date.split("/");
        String year = date_array[0];
        String month = date_array[1];
        //int day = Integer.parseInt(date_array[2]);

        switch (month) {
            case "01":
                month = "Janvier";
                break;
            case "02":
                month = "Février";
                break;
            case "03":
                month = "Mars";
                break;
            case "04":
                month = "Avril";
                break;
            case "05":
                month = "Mai";
                break;
            case "06":
                month = "Juin";
                break;
            case "07":
                month = "Juillet";
                break;
            case "08":
                month = "Août";
                break;
            case "09":
                month = "Septembre";
                break;
            case "10":
                month = "Octobre";
                break;
            case "11":
                month = "Novembre";
                break;
            case "12":
                month = "Décembre";
                break;
        }
        String day_str = ""; //day < 10 ? "début " : day > 20 ? "fin " : "";
        return day_str + month + " " + year;
    }
}