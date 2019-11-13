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

        // Create an Intent to start the second activity
        Intent randomIntent = new Intent(this, SecondActivity.class);

        // Add the count to the extras for the Intent.
        randomIntent.putExtra(TOTAL_COUNT, 10);

        // Start the new activity.
        startActivity(randomIntent);
    }

    public void openView (View view) {
        // Create an Intent to start the second activity
        Intent viewIntent = new Intent(this, ViewActivity.class);
        startActivity(viewIntent);
    }
}
