package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.activity.NavBarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openView (View view) {
        Intent viewIntent = new Intent(this, ViewActivity.class);
        startActivity(viewIntent);
    }

    public void openTravelMaps (View view) {
        Intent mapsIntent = new Intent(this, AllTravelMapsActivity.class);
        startActivity(mapsIntent);
    }

    public void openCountryMaps (View view) {
        Intent mapsIntent = new Intent(this, VisitedMapActivity.class);
        startActivity(mapsIntent);
    }

    public void randomMe (View view) {
        //Intent mapsIntent = new Intent(this, MapsActivity.class);
        //startActivity(mapsIntent);
        Intent intent = new Intent(this, NavBarActivity.class);
        startActivity(intent);
    }
}
