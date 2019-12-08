package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Border> borderList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
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
        loadBorders();
    }


    private void loadBorders() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Border>> call = service.getAllBorders();
        call.enqueue(new Callback<List<Border>>() {
            @Override
            public void onResponse(Call<List<Border>> call, Response<List<Border>> response) {
                progressDialog.dismiss();
                borderList = response.body();
                drawShapes(borderList);
            }

            @Override
            public void onFailure(Call<List<Border>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawShapes(List<Border> borderList) {
        for (Border border : borderList) {
            PolygonOptions polygon = new PolygonOptions().clickable(true);
            for (Coord point : border.getBorders()) {
                LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                polygon.add(latLng);
                //mMap.addMarker(new MarkerOptions().position(latLng));
            }
            polygon.fillColor(0x8079ADDC).strokeColor(0xFF79ADDC);
            mMap.addPolygon(polygon);
        }
    }
}
