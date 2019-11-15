package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.RetroPhoto;
import com.example.myapplication.model.RetroPokemon;
import com.example.myapplication.model.Step;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewActivity extends AppCompatActivity {


    private MyAdapter adapter;
    private RecyclerView recyclerView;
    public Integer sortBy = 0;
    private List<Country> datalist;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        progressDialog = new ProgressDialog(ViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        load();
    }

    private void load () {
        // Create handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Country>> call = service.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                progressDialog.dismiss();
                datalist = response.body();
                generateDataList(datalist);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ViewActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(final List<Country> list) {
        recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new MyAdapter(this, list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // put this after your definition of your recyclerview
        // input in your data mode in this example a java.util.List, adjust if necessary
        // adapter is your adapter
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    if(swipeDir == 4) {
                        Country country = list.get(viewHolder.getAdapterPosition());
                        openDetails(country);
                    } else {
                        list.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                    adapter.notifyDataSetChanged();

                }
            };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void openDetails (Country country) {

        Intent detailsIntent = new Intent(this, DetailsActivity.class);

        detailsIntent.putExtra("name", country.getCountry());
        detailsIntent.putExtra("flag", country.getFlag());
        detailsIntent.putExtra("from", country.getDate_from());
        detailsIntent.putExtra("to", country.getDate_to());
        detailsIntent.putExtra("desc", country.getDesc());
        detailsIntent.putExtra("steps", country.getSteps());
        //detailsIntent.putExtra("steps_array", (Serializable) country.getSteps_array());
        int i = 0;
        for (Step step: country.getSteps_array()) {
            detailsIntent.putExtra("city" + i, step.getCity());
            detailsIntent.putExtra("img" + i, step.getImg());
            detailsIntent.putExtra("desc" + i, step.getDesc());
            i++;
        }
        detailsIntent.putExtra("nb_steps", i);

        startActivity(detailsIntent);
    }

    public void sortByDate (View view) {
        Collections.sort(datalist, new Comparator<Country>() {
            @Override
            public int compare(Country lhs, Country rhs) {
                return lhs.getDate_from().compareTo(rhs.getDate_from());
            }
        });
        generateDataList(datalist);
        adapter.notifyDataSetChanged();
    }

    public void sortByName (View view) {
        Collections.sort(datalist, new Comparator<Country>() {
            @Override
            public int compare(Country lhs, Country rhs) {
                return lhs.getCountry().compareTo(rhs.getCountry());
            }
        });
        generateDataList(datalist);
        adapter.notifyDataSetChanged();
    }
}
