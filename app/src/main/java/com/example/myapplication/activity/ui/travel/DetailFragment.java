package com.example.myapplication.activity.ui.travel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.StepDetailsActivity;
import com.example.myapplication.TravelMapsActivity;
import com.example.myapplication.adapter.MyAdapter;
import com.example.myapplication.adapter.StepsAdapter;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private StepsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Step> datalist;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //int id_step = getIntent().getIntExtra("id", 0);
        Bundle arguments = getArguments();
        int id_step = Integer.parseInt(arguments.getString("id"));
        loadStep(id_step);

        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        return root;
    }


    private void loadStep(final int id) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
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
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetails (String name, String flag, String from, String to, String desc) {
        TextView nameView = (TextView) getView().findViewById(R.id.det_name);
        nameView.setText(name);

        ImageView flagView = (ImageView) getView().findViewById(R.id.det_flag);
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();
        picasso.load(flag)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(flagView);

        TextView dateView = (TextView) getView().findViewById(R.id.det_date);
        String date = "du " + from + "\nau " + to;
        dateView.setText(date);

        TextView descView = (TextView) getView().findViewById(R.id.det_desc);
        descView.setText(desc);

        String kms = String.format("%.0f", calculKms());
        TextView kmsView = (TextView) getView().findViewById(R.id.det_kms);
        if (kms.matches("0")) {
            kmsView.setText("");
        } else {
            kmsView.setText(kms + " km");
        }
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
        adapter = new StepsAdapter(getContext(), list, new StepsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {
                openDetails(step);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void openDetails (Step step) {

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle arguments = new Bundle();

        Bundle arg = getArguments();
        String travel_id = arg.getString("id");
        arguments.putString( "travel_id" , travel_id);
        arguments.putString( "step_id" , String.valueOf(step.getId()));
        stepDetailFragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, stepDetailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // TODO: create fragment
    public void openTravelMaps (View view) {

        Intent travelMapsIntent = new Intent(getContext(), TravelMapsActivity.class);
        //int travel_id = getIntent().getIntExtra("id", 0);
        int travel_id = 0;
        travelMapsIntent.putExtra("id", travel_id);
        startActivity(travelMapsIntent);
    }

}