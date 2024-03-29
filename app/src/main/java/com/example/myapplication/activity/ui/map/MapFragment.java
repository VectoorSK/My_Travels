package com.example.myapplication.activity.ui.map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.activity.ui.travel.DetailFragment;
import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {

    private GoogleMap map;
    private List<Border> borderList;
    private List<Travel> travelList;
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
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();
                map = mMap;
                loadBorders();

                ImageView travelBtn = (ImageView) getView().findViewById(R.id.btn_map);
                travelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawTravels();
                    }
                });
                ImageView countryBtn = (ImageView) getView().findViewById(R.id.btn_visited);
                countryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawCountries();
                    }
                });
                /*Button allCountryBtn = (Button) getView().findViewById(R.id.btn_all);
                allCountryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawAllCountries();
                    }
                });*/
            }
        });
        return root;
    }

    private void loadBorders() {
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
                travelList = response.body();
                //drawAllCountries();
                drawTravels();
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

    private void drawTravels() {
        map.clear();
        for (final Travel travel : travelList) {
            Step origin = travel.getSteps_array().get(0);
            LatLng originLatLng = new LatLng(origin.getLat(), origin.getLng());

            if (travel.getSteps_array().size() == 1) {
                CircleOptions options = new CircleOptions().clickable(true).center(originLatLng).radius(10000).strokeColor(0x80ff0000).fillColor(0x80ff0000);
                Circle circle = map.addCircle(options);
                String id = String.valueOf(travel.getId());
                circle.setTag(id);
                map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                    @Override
                    public void onCircleClick(Circle circle) {
                        openDetails(circle.getTag().toString());
                    }
                });
            } else {
                PolylineOptions options = new PolylineOptions().clickable(true).color(0x80ff0000);
                for (Step step : travel.getSteps_array()) {
                    LatLng latlng = new LatLng(step.getLat(), step.getLng());
                    options.add(latlng);
                }
                options.add(originLatLng);
                Polyline polyline = map.addPolyline(options);
                String id = String.valueOf(travel.getId());
                polyline.setTag(id);
                map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                    @Override
                    public void onPolylineClick(Polyline polyline) {
                        openDetails(polyline.getTag().toString());
                    }
                });
            }
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 0));
    }

    private void drawCountries() {
        map.clear();
        List<String> drawed = new ArrayList<>();
        for (Travel travel : travelList) {
            if (!drawed.contains(travel.getCode())) {
                drawed.add(travel.getCode());
                for (Border border : borderList) {
                    if (border.getCode().matches(travel.getCode())) {
                        PolygonOptions polygon = new PolygonOptions().clickable(true);
                        for (Coord point : border.getBorders()) {
                            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                            polygon.add(latLng);
                            //mMap.addMarker(new MarkerOptions().position(latLng));
                        }
                        if (travel.getContinent().matches("Europe")) {
                            polygon.fillColor(0x803B5998).strokeColor(0xFF3B5998);
                        } else if (travel.getContinent().matches("Afrique")) {
                            polygon.fillColor(0x80FFD355).strokeColor(0xFFFFD355);
                        } else if (travel.getContinent().matches("Asie")) {
                            polygon.fillColor(0x80FF9466).strokeColor(0xFFFF9466);
                        } else if (travel.getContinent().matches("Amerique du Nord")) {
                            polygon.fillColor(0x80F44336).strokeColor(0xFFF44336);
                        } else if (travel.getContinent().matches("Amerique du Sud")) {
                            polygon.fillColor(0x800392CF).strokeColor(0xFF0392CF);
                        } else if (travel.getContinent().matches("Océanie")) {
                            polygon.fillColor(0x80028900).strokeColor(0xFF028900);
                        }
                        map.addPolygon(polygon);
                        break;
                    }
                }
            }
        }
    }


    private void drawAllCountries() {
        map.clear();
        for (Border border : borderList) {
            PolygonOptions polygon = new PolygonOptions().clickable(true);
            for (Coord point : border.getBorders()) {
                LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                polygon.add(latLng);
            }
            polygon.fillColor(0x80F44336).strokeColor(0xFFF44336);
            map.addPolygon(polygon);
        }
    }

    private void openDetails (String id) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "id" , id);
        detailFragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_travel);
    }
}