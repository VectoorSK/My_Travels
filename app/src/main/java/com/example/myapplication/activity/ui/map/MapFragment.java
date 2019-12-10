package com.example.myapplication.activity.ui.map;

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

import com.example.myapplication.AllTravelMapsActivity;
import com.example.myapplication.DetailsActivity;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private GoogleMap map;
    private List<Travel> datalist;
    ProgressDialog progressDialog;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                /*CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(37.4219999,-122.0862462))
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();*/

                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
                map = mMap;
                loadData();
            }
        });
        return root;
    }



    private void loadData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
                datalist = response.body();
                loadCircuits(datalist);
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

    private void loadCircuits(List<Travel> datalist) {
        for (final Travel travel : datalist) {
            PolylineOptions polyline = new PolylineOptions().clickable(true);
            Step origin = travel.getSteps_array().get(0);
            LatLng originLatLng = new LatLng(origin.getLat(), origin.getLng());
            for (Step step : travel.getSteps_array()) {
                LatLng latlng = new LatLng(step.getLat(), step.getLng());
                polyline.add(latlng);
            }
            polyline.add(originLatLng);
            map.addPolyline(polyline);
            map.addMarker(new MarkerOptions().position(originLatLng).title(travel.getCountry()));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String country = marker.getTitle();
                    openDetails(country);
                    return false;
                }
            });
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 0));
    }

    private void openDetails (String country) {
        int id = 0;
        for (Travel travel : datalist) {
            if (travel.getCountry().matches(country)) {
                id = travel.getId();
            }
        }

        Intent detailsIntent = new Intent(getContext(), DetailsActivity.class);
        detailsIntent.putExtra("id", id);
        startActivity(detailsIntent);
    }
}