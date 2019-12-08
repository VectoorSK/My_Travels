package com.example.myapplication.activity.ui.visited;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;

public class VisitedFragment extends Fragment {

    private VisitedViewModel visitedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        visitedViewModel =
                ViewModelProviders.of(this).get(VisitedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_visited, container, false);
        final TextView textView = root.findViewById(R.id.text_nav_visited);
        visitedViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}