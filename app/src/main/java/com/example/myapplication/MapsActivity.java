package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Country> datalist;
    private List<Travel> travelList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        loadMarkers();

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        //mMap.setMyLocationEnabled(true);
        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        /* polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(-35.016, 143.321),
                        new LatLng(-34.747, 145.592),
                        new LatLng(-34.364, 147.891),
                        new LatLng(-33.501, 150.217),
                        new LatLng(-32.306, 149.248),
                        new LatLng(-32.491, 147.309),
                        new LatLng(-58.729,-34.634),
                        new LatLng(-58.728,-34.634),
                        new LatLng(-58.727,-34.633)));*/
        //KmlLayer layer = new KmlLayer(mMap, R.raw.autriche, this);

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));

    }



    private void loadMarkers() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        // Create handle for the RetrofitInstance interface
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Country>> call = service.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                datalist = response.body();
                //generateMarkers(datalist);
                Call<List<Travel>> call2 = service.getAllTravels();
                call2.enqueue(new Callback<List<Travel>>() {
                    @Override
                    public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {

                        progressDialog.dismiss();
                        travelList = response.body();
                        generateMarkers(datalist, travelList);

                    }

                    @Override
                    public void onFailure(Call<List<Travel>> call, Throwable t) {
                        System.out.println(call);
                        System.out.println(t);
                        progressDialog.dismiss();
                        Toast.makeText(MapsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                System.out.println(call);
                System.out.println(t);
                progressDialog.dismiss();
                Toast.makeText(MapsActivity.this, "Something went wrong... Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateMarkers(List<Country> list, List<Travel> user_list) {
        for (Country country : list) {

            for (Travel trav : user_list) {
                if (trav.getCode().matches(country.getCountry())) {

                    String name = country.getName();
                    double lat = country.getLatitude();
                    double lng = country.getLongitude();

                    LatLng current = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(current).title(name));

                }
            }
        }
    }
}
