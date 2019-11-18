package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class FullscreenImg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_img);

        loadDetails();
    }

    private void loadDetails() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        Picasso picasso = builder.build();

        String url = getIntent().getStringExtra("url");
        ImageView imgView = (ImageView) findViewById(R.id.full_img);
        picasso.load(url)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(imgView);

        String caption = getIntent().getStringExtra("caption");
        TextView descView = (TextView) findViewById(R.id.full_caption);
        descView.setText(caption);
    }
}
