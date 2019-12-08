package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.StepsAdapter;
import com.example.myapplication.model.Travel;
import com.example.myapplication.model.Step;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private StepsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Step> datalist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int id_step = getIntent().getIntExtra("id", 0);
        loadStep(id_step);
    }

    private void loadStep(final int id) {
        progressDialog = new ProgressDialog(DetailsActivity.this);
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
                loadDetails(name, flag, from, to, desc);
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(DetailsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDetails (String name, String flag, String from, String to, String desc) {
        TextView nameView = (TextView) findViewById(R.id.det_name);
        nameView.setText(name);

        ImageView flagView = (ImageView) findViewById(R.id.det_flag);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        Picasso picasso = builder.build();
        picasso.load(flag)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(flagView);

        TextView dateView = (TextView) findViewById(R.id.det_date);
        String date = "From: " + from + "\nTo: " + to;
        dateView.setText(date);

        TextView descView = (TextView) findViewById(R.id.det_desc);
        descView.setText(desc);
    }

    private void generateSteps(final List<Step> list) {
        recyclerView = findViewById(R.id.step_recycler_view);
        adapter = new StepsAdapter(this, list, new StepsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {
                openDetails(step);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        /*ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    if(swipeDir == ItemTouchHelper.LEFT) {
                        Step step = list.get(viewHolder.getAdapterPosition());
                        openDetails(step);
                    } else {
                        //list.remove(viewHolder.getAdapterPosition());
                        //adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                    adapter.notifyDataSetChanged();
                }
            };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }

    private void openDetails (Step step) {

        Intent detailsIntent = new Intent(this, StepDetailsActivity.class);

        int travel_id = getIntent().getIntExtra("id", 0);
        detailsIntent.putExtra("travel_id", travel_id);
        detailsIntent.putExtra("step_id", step.getId());
        startActivity(detailsIntent);
    }

    public void openTravelMaps (View view) {

        Intent travelMapsIntent = new Intent(this, TravelMapsActivity.class);
        int travel_id = getIntent().getIntExtra("id", 0);
        travelMapsIntent.putExtra("id", travel_id);
        startActivity(travelMapsIntent);
    }

}
