package com.example.myapplication.activity.ui.travel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MyAdapter;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelFragment extends Fragment {

    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<Travel> datalist;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loadTravelList();

        View root = inflater.inflate(R.layout.fragment_travel, container, false);
        Button button = (Button) root.findViewById(R.id.sort_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sort_by();
            }
        });
        return root;
    }

    private void loadTravelList() {

        progressDialog = new ProgressDialog(getContext());
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
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method to generate List of data using RecyclerView with custom adapter
    private void generateDataList(final List<Travel> list) {
        recyclerView = getActivity().findViewById(R.id.my_recycler_view);
        adapter = new MyAdapter(getContext(), list, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Travel travel) {
                openDetails(travel);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void openDetails (Travel travel) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "id" , travel.getId().toString());
        detailFragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private int sort = 0;
    private String sort_cat = "  date";

    public void sort_by () {
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
        TextView sort_text = (TextView) getActivity().findViewById(R.id.sort_button);
        sort_text.setText(sort_cat);
    }
}