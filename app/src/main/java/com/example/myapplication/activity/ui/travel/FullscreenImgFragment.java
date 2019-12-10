package com.example.myapplication.activity.ui.travel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CityMapsActivity;
import com.example.myapplication.FullscreenImg;
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

public class FullscreenImgFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loadDetails();

        View root = inflater.inflate(R.layout.fragment_fullscreen_img, container, false);
        return root;
    }

    private void loadDetails() {
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();

        String url = getArguments().getString("url");
        //ImageView imgView = (ImageView) getView().findViewById(R.id.full_img);
        /*picasso.load(url)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(imgView);*/

        String caption = getArguments().getString("caption");
        //TextView descView = (TextView) getView().findViewById(R.id.full_caption);
        //descView.setText(caption);
    }

}