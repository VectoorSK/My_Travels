package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        showRandomNumber();
    }

    private static final String TOTAL_COUNT = "total_count";

    private void showRandomNumber () {
        // Get the text view where the heading is displayed
        TextView headingView = (TextView) findViewById(R.id.textview_label);

        // Get the count from the intent extras
        int count = getIntent().getIntExtra(TOTAL_COUNT, 0);

        View view = (View) findViewById(android.R.id.content);
        generateRandom(view);

        // Substitute the max value into the string resource
        // for the heading, and update the heading
        headingView.setText(getString(R.string.rand_text, count));
    }

    public void generateRandom (View view) {
        // Get the text view where the random number will be displayed
        TextView randomView = (TextView) findViewById(R.id.textview_random);

        int max = getIntent().getIntExtra(TOTAL_COUNT, 0);
        // Generate the random number
        Random random = new Random();
        int randomInt = 0;
        if (max>0) {
            randomInt = random.nextInt(max);
        }
        // Display the random number.
        randomView.setText(Integer.toString(randomInt));
    }
}
