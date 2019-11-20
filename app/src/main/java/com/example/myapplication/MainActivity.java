package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final String TOTAL_COUNT = "total_count";

    public void randomMe (View view) {
        Intent randomIntent = new Intent(this, SecondActivity.class);
        randomIntent.putExtra(TOTAL_COUNT, 10);
        startActivity(randomIntent);
    }

    public void openCountryMaps (View view) {
        Intent mapsIntent = new Intent(this, MapsActivity.class);
        startActivity(mapsIntent);
    }

    public void openTravelMaps (View view) {
        Intent mapsIntent = new Intent(this, AllTravelMapsActivity.class);
        startActivity(mapsIntent);
    }

    public void openView (View view) {
        // Create an Intent to start the second activity
        Intent viewIntent = new Intent(this, ViewActivity.class);
        startActivity(viewIntent);
    }
}
