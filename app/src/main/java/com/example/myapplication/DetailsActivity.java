package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        loadDetails();
    }

    private void loadDetails () {
        TextView nameView = (TextView) findViewById(R.id.det_name);
        String name = getIntent().getStringExtra("name");
        nameView.setText(name);

        ImageView flagView = (ImageView) findViewById(R.id.det_flag);
        String flag = getIntent().getStringExtra("flag");
        nameView.setText(name);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        Picasso picasso = builder.build();
        picasso.load(flag)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(flagView);

        TextView dateView = (TextView) findViewById(R.id.det_date);
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String date = "From: " + from + "\nTo: " + to;
        dateView.setText(date);

        TextView descView = (TextView) findViewById(R.id.det_desc);
        String desc = getIntent().getStringExtra("desc");
        descView.setText(desc);

        TextView stepsView = (TextView) findViewById(R.id.det_steps);
        String steps = getIntent().getStringExtra("steps");
        stepsView.setText(steps);
    }
}
