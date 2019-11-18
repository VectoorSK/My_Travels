package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Img;
import com.example.myapplication.model.Step;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepDetailsActivity extends AppCompatActivity {

    private ImgAdapter adapter;
    private RecyclerView recyclerView;
    private List<Img> datalist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        int id_travel = getIntent().getIntExtra("travel_id", 0);
        int id_step = getIntent().getIntExtra("step_id", 0);
        loadImgs(id_travel, id_step);

    }

    private void loadImgs(final int id_travel, final int id_step) {
        progressDialog = new ProgressDialog(StepDetailsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Country>> call = service.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                progressDialog.dismiss();
                Step step = response.body().get(id_travel).getSteps_array().get(id_step);
                datalist = step.getPictures();
                generateImgs(datalist);

                String name = step.getCity();
                String img = step.getImg();
                String desc = step.getDesc();
                loadDetails(name, img, desc);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(StepDetailsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDetails (String title, String img, String desc) {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        Picasso picasso = builder.build();

        TextView step_title = (TextView) findViewById(R.id.step_title);
        step_title.setText(title);

        TextView step_description = (TextView) findViewById(R.id.step_description);
        step_description.setText(desc);

        ImageView step_icon = (ImageView) findViewById(R.id.step_icon);
        picasso.load(img)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(step_icon);
    }


    private void generateImgs(final List<Img> list) {
        recyclerView = findViewById(R.id.img_recycler_view);
        adapter = new ImgAdapter(this, list, new ImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Img img) {
                fullscreen(img);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(StepDetailsActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fullscreen(Img img) {
        Intent fullIntent = new Intent(this, FullscreenImg.class);
        fullIntent.putExtra("url", img.getUrl());
        fullIntent.putExtra("caption", img.getCaption());
        startActivity(fullIntent);
    }
}
