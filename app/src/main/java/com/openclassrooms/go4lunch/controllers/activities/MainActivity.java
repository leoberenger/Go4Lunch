package com.openclassrooms.go4lunch.controllers.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.controllers.fragments.RestoRecyclerFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configureAndShowSearchFragment();
    }

    private void configureAndShowSearchFragment() {

        RestoRecyclerFragment mRestoRecyclerFragment = new RestoRecyclerFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_frame_layout, mRestoRecyclerFragment)
                .commit();
    }
}
