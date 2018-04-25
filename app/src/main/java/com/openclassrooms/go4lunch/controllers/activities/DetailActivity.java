package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class DetailActivity extends AppCompatActivity {

    private Disposable mDisposable;
    @BindView(R.id.activity_detail_name) TextView mTextViewName;
    @BindView(R.id.activity_detail_type_and_address) TextView mTextViewTypeAndAddress;
    @BindView(R.id.activity_detail_like_btn) Button likeBtn;
    @BindView(R.id.activity_detail_phone_btn) Button phoneBtn;
    @BindView(R.id.activity_detail_website_btn) Button websiteBtn;
    @BindView(R.id.activity_detail_header_image) ImageView headerImg;
    @BindView(R.id.activity_detail_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String placeId = intent.getStringExtra("PLACE_ID");
        Log.e("DetailActivity", "placeID=" + placeId);
        executeHttpRequestWithRetrofit(placeId);
        this.configureToolBar();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Like Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Phone Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        websiteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Website Button clicked", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if(this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    private void executeHttpRequestWithRetrofit(String placeId){
        this.mDisposable = GMPlacesStreams.streamFetchRestaurant(placeId)
                .subscribeWith(new DisposableObserver<PlacesAPI>(){
                    @Override
                    public void onNext(final PlacesAPI place) {
                        Log.e("DetailActivity", "On Next");
                        showRestaurantDetails(place);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DetailActivity", "On Error"+Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("DetailActivity", "On Complete");
                    }
                });
    }

    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(toolbar);
    }

    private void showRestaurantDetails(PlacesAPI place){
        mTextViewName.setText(place.getResult().getName());
        String typeAndAddress = place.getResult().getTypes().get(0) + " restaurant - " + place.getResult().getFormattedAddress();
        mTextViewTypeAndAddress.setText(typeAndAddress);
        headerImg.setImageResource(R.drawable.blurred_restaurant);
    }
}
