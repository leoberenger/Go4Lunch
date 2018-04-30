package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.UserHelper;
import com.openclassrooms.go4lunch.controllers.activities.base.BaseActivity;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.views.WorkmatesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class DetailActivity extends BaseActivity {

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
    private WorkmatesRecyclerAdapter adapter;

    WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();
    PlacesMgr placesMgr = PlacesMgr.getInstance();
    private List<User> workmates;
    private String placeId;
    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //Retrieve Place ID
        Intent intent = getIntent();
        placeId = intent.getStringExtra("PLACE_ID");

        this.configureToolBar();
        this.configureRecyclerView();

        getRestaurantDetails(placeId);
        getWorkmatesGoing(placeId);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_detail;
    }

    //-------------------------
    // CONFIGURATION
    //-------------------------

    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureRecyclerView(){
        this.workmates = new ArrayList<>();
        this.adapter = new WorkmatesRecyclerAdapter(this.workmates, Glide.with(this), false);
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //-------------------------
    // GET DATE
    //-------------------------

    private void getRestaurantDetails(String placeId){
        placesMgr.executeHttpRequestToGetRestaurantDetails(placeId, new DisposableObserver<PlacesAPI>(){
            @Override
            public void onNext(PlacesAPI place) {
                Log.e("DetailActivity", "On Next");
                placesMgr.setRestaurant(place.getResult());
                placeName = place.getResult().getName();
                showRestaurantDetails();
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

    private void getWorkmatesGoing(String placeId){
        List<User> users = workmatesMgr.getWorkmates();
        List<User> usersGoingToThisRestaurant = new ArrayList<>();
        for(int i = 0; i<users.size(); i++){
            User user = users.get(i);
            if(user.getSelectedRestoId() != null && user.getSelectedRestoId().equals(placeId)){
                usersGoingToThisRestaurant.add(user);
            }
        }
        showWorkmatesGoing(usersGoingToThisRestaurant);
    }


    //-------------------------
    // UPDATE UI
    //-------------------------

    private void showRestaurantDetails(){
        PlacesAPI.Result place = placesMgr.getRestaurant();

        mTextViewName.setText(place.getName());
        String typeAndAddress = place.getTypes().get(0) + " restaurant - " + place.getFormattedAddress();
        mTextViewTypeAndAddress.setText(typeAndAddress);
        headerImg.setImageResource(R.drawable.blurred_restaurant);
    }

    private void showWorkmatesGoing(List<User> users){
        workmates.clear();
        workmates.addAll(users);
        adapter.notifyDataSetChanged();
    }


    //-------------------------
    // ACTIONS
    //-------------------------

    @OnClick(R.id.activity_detail_like_btn)
    public void onClickLikeButton() {
        Toast.makeText(getApplication(), "Like Button clicked", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.activity_detail_phone_btn)
    public void onClickPhoneButton() {
        Toast.makeText(getApplication(), "Phone Button clicked", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.activity_detail_website_btn)
    public void onClickWebsiteButton() {
        Toast.makeText(getApplication(), "Website Button clicked", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.activity_detail_select_resto_fab)
    public void onClickSelectRestoButton() {
        if (getCurrentUser() != null){
            UserHelper.updateSelectedRestoId(placeId, getCurrentUser().getUid())
                    .addOnFailureListener(onFailureListener());

            UserHelper.updateSelectedRestoName(placeName, getCurrentUser().getUid())
                    .addOnFailureListener(onFailureListener());
        }
        Toast.makeText(getApplication(), "Resto selected : " + placeName, Toast.LENGTH_LONG).show();
    }

}
