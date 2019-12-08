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

public class VisitedMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Border> borderList;
    private List<Travel> travelList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        progressDialog = new ProgressDialog(VisitedMapActivity.this);
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
                Toast.makeText(VisitedMapActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(VisitedMapActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
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
                        polygon.fillColor(0x8075C5DC).strokeColor(0xFF75C5DC);
                    } else if (travel.getContinent().matches("Afrique")) {
                        polygon.fillColor(0x80FFD355).strokeColor(0xFFFFD355);
                    } else if (travel.getContinent().matches("Asie")) {
                        polygon.fillColor(0x80FF9466).strokeColor(0xFFFF9466);
                    } else if (travel.getContinent().matches("Amerique du Sud")) {
                        polygon.fillColor(0x80588F27).strokeColor(0xFF588F27);
                    } else if (travel.getContinent().matches("Amerique du Nord")) {
                        polygon.fillColor(0x80A9CF54).strokeColor(0xFFA9CF54);
                    } else if (travel.getContinent().matches("Océanie")) {
                        polygon.fillColor(0x80EF476F).strokeColor(0xFFEF476F);
                    }
                    mMap.addPolygon(polygon);
                    break;
                }
            }
        }
    }
}
