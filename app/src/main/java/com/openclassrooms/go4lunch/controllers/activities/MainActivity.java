package com.openclassrooms.go4lunch.controllers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.controllers.fragments.RestoListFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configureAndShowSearchFragment();
        //configureAndShowMapFragment();
    }

    private void configureAndShowSearchFragment() {

        RestoListFragment restoListFragment = new RestoListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_frame_layout, restoListFragment)
                .commit();
    }

    private void configureAndShowMapFragment() {

        MapsFragment mapsFragment = new MapsFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_frame_layout, mapsFragment)
                .commit();
    }
}
