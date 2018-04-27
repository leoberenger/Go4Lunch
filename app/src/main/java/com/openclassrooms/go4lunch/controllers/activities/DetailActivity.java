package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.apis.UserHelper;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.views.RestoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.activity_detail_select_resto_fab) FloatingActionButton selectRestoFab;


    //RECYCLER VIEW
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private RestoRecyclerAdapter adapter;

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


        //RECYCLER VIEW TEST
        this.configureRecyclerView();
        PlacesMgr placesMgr = PlacesMgr.getInstance();
        PlacesAPI placesAPI = placesMgr.getPlaces();
        updateUI(placesAPI);

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

        selectRestoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentUser() != null){
                    UserHelper.updateSelectedRestoId(placeId, getCurrentUser().getUid())
                            .addOnFailureListener(onFailureListener());
                }
                Toast.makeText(getApplication(), "Resto selected : " + placeId, Toast.LENGTH_LONG).show();
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

    @Nullable
    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    void configureRecyclerView(){
        this.places = new ArrayList<>();
        this.adapter = new RestoRecyclerAdapter(this.places, Glide.with(this));
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void updateUI(PlacesAPI results){
        places.clear();
        places.addAll(results.getResults());
        adapter.notifyDataSetChanged();
    }
}
