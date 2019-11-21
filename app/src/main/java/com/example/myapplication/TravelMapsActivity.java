package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int id;
    private List<LatLng> listPoint;
    private PolylineOptions polyline;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_maps);
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
        polyline = new PolylineOptions().clickable(true);

        id = getIntent().getIntExtra("id", 0);
        loadPoints(id);
    }

    private void loadPoints(final int id) {
        progressDialog = new ProgressDialog(TravelMapsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Travel>> call = service.getAllTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                progressDialog.dismiss();
                List<Step> steps = response.body().get(id).getSteps_array();
                showCircuit(steps);
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(TravelMapsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCircuit (List<Step> steps) {
        Step origin = steps.get(0);
        LatLng originLatLng = new LatLng(origin.getLat(), origin.getLng());
        for (Step step : steps) {
            LatLng latlng = new LatLng(step.getLat(), step.getLng());
            polyline.add(latlng);
            mMap.addMarker(new MarkerOptions().position(latlng)
                    .title(step.getCity())
                    .snippet(step.getDesc())
                ).showInfoWindow();
        }
        polyline.add(originLatLng);
        mMap.addPolyline(polyline);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 6));
    }
}
