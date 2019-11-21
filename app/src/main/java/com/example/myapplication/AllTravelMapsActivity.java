package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTravelMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Travel> datalist;
    private int currId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_travel_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadData();
    }

    private void loadData() {
        progressDialog = new ProgressDialog(AllTravelMapsActivity.this);
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
                Toast.makeText(AllTravelMapsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCircuits(List<Travel> datalist) {
        for (final Travel travel : datalist) {
            currId = travel.getId();
            PolylineOptions polyline = new PolylineOptions().clickable(true);
            Step origin = travel.getSteps_array().get(0);
            LatLng originLatLng = new LatLng(origin.getLat(), origin.getLng());
            for (Step step : travel.getSteps_array()) {
                LatLng latlng = new LatLng(step.getLat(), step.getLng());
                polyline.add(latlng);
            }
            polyline.add(originLatLng);
            mMap.addPolyline(polyline);
            mMap.addMarker(new MarkerOptions().position(originLatLng).title(travel.getId().toString()));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int id = Integer.parseInt(marker.getTitle());
                    openDetails(id);
                    return false;
                }
            });
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 0));
    }

    private void openDetails (int id) {

        Intent detailsIntent = new Intent(this, DetailsActivity.class);

        detailsIntent.putExtra("id", id);
        startActivity(detailsIntent);
    }
}
