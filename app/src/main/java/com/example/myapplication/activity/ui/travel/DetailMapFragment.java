package com.example.myapplication.activity.ui.travel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.StepsAdapter;
import com.example.myapplication.model.Border;
import com.example.myapplication.model.Coord;
import com.example.myapplication.model.Step;
import com.example.myapplication.model.Travel;
import com.example.myapplication.network.GetDataService;
import com.example.myapplication.network.RetrofitClientInstance;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMapFragment extends Fragment {
    private GoogleMap map;
    private double[] listLatLng;

    public DetailMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detail_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_detail);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();
                map = mMap;
                listLatLng = getArguments().getDoubleArray("list_lat_lng");
                if(listLatLng.length == 2) {
                    drawCircle();
                } else {
                    drawCircuit();
                }
            }
        });
        return root;
    }

    private void drawCircle() {
        LatLng position = new LatLng(listLatLng[0], listLatLng[1]);
        map.addCircle(new CircleOptions().clickable(true).center(position).radius(10000).strokeColor(0x80ff0000).fillColor(0x80ff0000));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 6));
    }

    private void drawCircuit() {
        PolylineOptions polyline = new PolylineOptions().color(0x80ff0000);
        for (int i = 0; i < listLatLng.length; i+=2) {
            double lat = listLatLng[i];
            double lng = listLatLng[i+1];
            polyline.add(new LatLng(lat, lng));
        }
        LatLng origin = new LatLng(listLatLng[0], listLatLng[1]);
        polyline.add(origin);
        map.addPolyline(polyline);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 4));
    }
}