package com.example.myapplication.activity.ui.visited;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.DetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.VisitedMapActivity;
import com.example.myapplication.activity.ui.map.MapViewModel;
import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedFragment extends Fragment {

    //private MapViewModel mapViewModel;
    private GoogleMap map;
    private List<Border> borderList;
    private List<Travel> travelList;
    ProgressDialog progressDialog;

    public VisitedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();
                map = mMap;
                loadBorders();
            }
        });
        return root;
    }

    private void loadBorders() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Border>> call = service.getAllBorders();
        call.enqueue(new Callback<List<Border>>() {
            @Override
            public void onResponse(Call<List<Border>> call, Response<List<Border>> response) {
                borderList = response.body();
                loadTravels();
            }

            @Override
            public void onFailure(Call<List<Border>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTravels() {
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
                travelList = response.body();
                drawShapes(travelList, borderList);
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

    private void drawShapes(List<Travel> travelList, List<Border> borderList) {
        for (Travel travel : travelList) {
            for (Border border : borderList) {
                if (border.getCode().matches(travel.getCode())) {
                    PolygonOptions polygon = new PolygonOptions().clickable(true);
                    for (Coord point : border.getBorders()) {
                        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                        polygon.add(latLng);
                        //mMap.addMarker(new MarkerOptions().position(latLng));
                    }
                    if (travel.getContinent().matches("Europe")) {
                        polygon.fillColor(0x809ACD32).strokeColor(0xFF9ACD32);
                    } else if (travel.getContinent().matches("Afrique")) {
                        polygon.fillColor(0x80FFD355).strokeColor(0xFFFFD355);
                    } else if (travel.getContinent().matches("Asie")) {
                        polygon.fillColor(0x80FF9466).strokeColor(0xFFFF9466);
                    } else if (travel.getContinent().matches("Amerique du Sud")) {
                        polygon.fillColor(0x8075C5DC).strokeColor(0xFF75C5DC);
                    } else if (travel.getContinent().matches("Amerique du Nord")) {
                        polygon.fillColor(0x803B5998).strokeColor(0xFF3B5998);
                    } else if (travel.getContinent().matches("Océanie")) {
                        polygon.fillColor(0x80EF476F).strokeColor(0xFFEF476F);
                    }
                    map.addPolygon(polygon);
                    break;
                }
            }
        }
    }
}