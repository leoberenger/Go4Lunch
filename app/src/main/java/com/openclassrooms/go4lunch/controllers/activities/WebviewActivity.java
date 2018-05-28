package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.controllers.fragments.WebviewFragment;

import butterknife.ButterKnife;


public class WebviewActivity extends AppCompatActivity {

    private final String RESTAURANT_URL_KEY = "articleUrl";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        this.configureAndShowWebviewFragment();
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    private void configureAndShowWebviewFragment(){

        WebviewFragment webviewFragment = (WebviewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_webview_frame_layout);

        if (webviewFragment == null) {
            webviewFragment = new WebviewFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_webview_frame_layout, webviewFragment)
                    .commit();

            Intent intent = getIntent();
            String articleUrl = intent.getStringExtra("EXTRA_WEBSITE_URL");

            Bundle args = new Bundle();
            args.putString(RESTAURANT_URL_KEY, articleUrl);
            webviewFragment.setArguments(args);
        }
    }
}
