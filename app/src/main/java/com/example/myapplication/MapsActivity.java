package com.example.myapplication;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
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
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else {
            //mMap.addCircle(new CircleOptions().center(new LatLng(0,0)).radius(1000000));
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Localisation désactivé")
                    .setMessage("Voulez-vous l'activer ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS); // TODO: test avec ACTION_APP_NOTIFICATION_SETTINGS
                            startActivityForResult(callGPSSettingIntent,0);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
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
