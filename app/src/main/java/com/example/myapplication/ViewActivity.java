package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Pokemon;
import com.example.myapplication.model.RetroPhoto;
import com.example.myapplication.model.RetroPokemon;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewActivity extends AppCompatActivity {


    private MyAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        progressDialog = new ProgressDialog(ViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Create handle for the RetrofitInstance interface
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Country>> call = service.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ViewActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to generate List of data using RecyclerView with custom adapter
    // private void generateDataList(final List<RetroPhoto> list) {
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
                        Country country = list.get(viewHolder.getAdapterPosition());
                        openDetails(country);
                        //list.remove(viewHolder.getAdapterPosition());
                        //adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
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

        startActivity(detailsIntent);
    }
}
