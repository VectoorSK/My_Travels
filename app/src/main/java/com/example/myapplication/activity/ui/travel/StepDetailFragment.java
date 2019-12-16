package com.example.myapplication.activity.ui.travel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ImgAdapter;
import com.example.myapplication.model.Img;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepDetailFragment extends Fragment {

    private ImgAdapter adapter;
    private RecyclerView recyclerView;
    private List<Img> datalist;
    ProgressDialog progressDialog;
    private double latitude;
    private double longitude;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arg = getArguments();
        int id_travel = Integer.parseInt(arg.getString("travel_id"));
        int id_step = Integer.parseInt(arg.getString("step_id"));
        loadImgs(id_travel, id_step);

        View root = inflater.inflate(R.layout.fragment_step_detail, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView map_btn = (ImageView) getView().findViewById(R.id.img_map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGMaps();
            }
        });
    }

    private void loadImgs(final int id_travel, final int id_step) {
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
                Step step = response.body().get(id_travel).getSteps_array().get(id_step);
                datalist = step.getPictures();
                generateImgs(datalist);

                String name = step.getCity();
                String img = step.getImg();
                String desc = step.getDesc();
                loadDetails(name, img, desc);

                latitude = step.getLat();
                longitude = step.getLng();
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

    private void loadDetails (String title, String img, String desc) {
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();

        TextView step_title = (TextView) getView().findViewById(R.id.step_title);
        step_title.setText(title);

        TextView step_description = (TextView) getView().findViewById(R.id.step_description);
        step_description.setText(desc);

        ImageView step_icon = (ImageView) getView().findViewById(R.id.step_icon);
        picasso.load(img)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(step_icon);
    }


    private void generateImgs(final List<Img> list) {
        recyclerView = getActivity().findViewById(R.id.img_recycler_view);
        adapter = new ImgAdapter(getContext(), list, new ImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Img img) {
                fullscreen(img);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fullscreen(Img img) {

        FullscreenImgFragment fullscreenImgFragment = new FullscreenImgFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "url" , img.getUrl());
        arguments.putString( "caption" , img.getCaption());
        fullscreenImgFragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fullscreenImgFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openGMaps() {

        StepDetailMapFragment stepDetailMapFragment = new StepDetailMapFragment();
        Bundle arguments = new Bundle();
        arguments.putDouble("lat", latitude);
        arguments.putDouble("lng", longitude);
        stepDetailMapFragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, stepDetailMapFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}