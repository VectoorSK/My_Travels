package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.myapplication.activity.NavBarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, NavBarActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    public void letsStart (View view) {
        Intent intent = new Intent(this, NavBarActivity.class);
        startActivity(intent);
    }
}
