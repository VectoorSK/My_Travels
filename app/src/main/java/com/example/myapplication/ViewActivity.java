package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewActivity extends AppCompatActivity {


    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<Travel> datalist;
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
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
                datalist = response.body();

                Collections.sort(datalist, new Comparator<Travel>() {
                    @Override
                    public int compare(Travel lhs, Travel rhs) {
                        return lhs.getDate_from().compareTo(rhs.getDate_from());
                    }
                });
                generateDataList(datalist);
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(ViewActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        //return view;
    }

    //Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(final List<Travel> list) {
        recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new MyAdapter(this, list, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Travel travel) {
                openDetails(travel);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewActivity.this);
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
                        Travel travel = list.get(viewHolder.getAdapterPosition());
                        openDetails(travel);
                    } else {
                        list.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                    adapter.notifyDataSetChanged();
                }
            };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }

    private void openDetails (Travel travel) {

        Intent detailsIntent = new Intent(this, DetailsActivity.class);

        detailsIntent.putExtra("id", travel.getId());
        startActivity(detailsIntent);
    }

    private int sort = 0;
    private String sort_cat = "date";

    public void sort_by (View view) {
        if (sort == 0) {
            Collections.sort(datalist, new Comparator<Travel>() {
                @Override
                public int compare(Travel lhs, Travel rhs) {
                    return lhs.getCountry().compareTo(rhs.getCountry());
                }
            });
            sort_cat = "  country";
            sort++;
        } else if (sort == 1) {
            Collections.sort(datalist, new Comparator<Travel>() {
                @Override
                public int compare(Travel lhs, Travel rhs) {
                    return lhs.getContinent().compareTo(rhs.getContinent());
                }
            });
            sort_cat = "  continent";
            sort++;
        } else {
            Collections.sort(datalist, new Comparator<Travel>() {
                @Override
                public int compare(Travel lhs, Travel rhs) {
                    return lhs.getDate_from().compareTo(rhs.getDate_from());
                }
            });
            sort_cat = "  date";
            sort = 0;
        }
        generateDataList(datalist);
        adapter.notifyDataSetChanged();
        TextView sort_text = (TextView) findViewById(R.id.sort_button);
        sort_text.setText(sort_cat);
    }
}
