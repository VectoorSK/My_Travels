package com.example.myapplication.activity.ui.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class FullscreenImgFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fullscreen_img, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDetails();
    }

    private void loadDetails() {
        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttp3Downloader(getContext()));
        Picasso picasso = builder.build();

        String title = getArguments().getString("title");
        TextView titleView = (TextView) getView().findViewById(R.id.full_title);
        titleView.setText(title);

        String url = getArguments().getString("url");
        ImageView imgView = (ImageView) getView().findViewById(R.id.full_img);
        picasso.load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgView);

        String caption = getArguments().getString("caption");
        TextView descView = (TextView) getView().findViewById(R.id.full_caption);
        descView.setText(caption);
    }

}