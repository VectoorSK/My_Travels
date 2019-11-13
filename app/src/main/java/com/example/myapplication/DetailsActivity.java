package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Step;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private StepsAdapter adapter;
    private RecyclerView recyclerView;

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
        String stepsStr = getIntent().getStringExtra("steps");
        String[] stepsArr = stepsStr.split("&&");
        String steps = "";
        for (String step: stepsArr) {
            steps += step + " --> ";
        }
        steps = steps.substring(0, steps.length() - 4);
        stepsView.setText(steps);

        //List<Step> stepsList = ((List<Step>) getIntent().getExtras().getSerializable("steps_array"));
        //List<String> stepsList = Arrays.asList( stepsArr );
        List<Step> stepsList = new ArrayList<>();
        int nb_steps = getIntent().getIntExtra("nb_steps", 0);
        for (int i = 0; i < nb_steps; i++) {
            String curr_city = getIntent().getStringExtra("city" + i);
            String curr_img = getIntent().getStringExtra("img" + i);
            Step curr_step = new Step(curr_city, curr_img);
            stepsList.add(curr_step);
        }
        generateSteps(stepsList);
    }

    private void generateSteps(List<Step> list) {
        recyclerView = findViewById(R.id.step_recycler_view);
        adapter = new StepsAdapter(this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
