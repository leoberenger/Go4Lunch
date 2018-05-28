package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.evernote.android.job.JobManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.UserHelper;
import com.openclassrooms.go4lunch.controllers.activities.base.BaseActivity;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.notifications.MyJobCreator;
import com.openclassrooms.go4lunch.notifications.NotificationJob;
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
    @BindView(R.id.activity_detail_header_image) ImageView headerImg;
    @BindView(R.id.activity_detail_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_detail_select_resto_fab) FloatingActionButton selectRestoFab;
    @BindView(R.id.activity_detail_star_1) ImageView star1;
    @BindView(R.id.activity_detail_star_2) ImageView star2;
    @BindView(R.id.activity_detail_star_3) ImageView star3;


    //RECYCLER VIEW
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private WorkmatesRecyclerAdapter adapter;

    private WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();
    private PlacesMgr placesMgr = PlacesMgr.getInstance();
    private List<User> workmates;
    private String placeId;
    private String placeName;
    private PlacesAPI.Result place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //Retrieve Place ID
        Intent intent = getIntent();
        placeId = intent.getStringExtra("PLACE_ID");

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

    private void configureRecyclerView(){
        this.workmates = new ArrayList<>();
        this.adapter = new WorkmatesRecyclerAdapter(this.workmates, Glide.with(this), false);
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //-------------------------
    // GET DATA
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
        place = placesMgr.getRestaurant();

        if(place.getRating() != null){
            double rating = place.getRating();
            int nbStars = (int)(((rating)/5)*3);

            switch (nbStars){
                case 1:
                    star1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(getApplication(), "No rating available", Toast.LENGTH_LONG).show();
        }

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

    @OnClick(R.id.activity_detail_like_relative_layout)
    public void onClickLikeButton() {
        String restoLikeUrl = place.getUrl();
        if(restoLikeUrl == null){
            Toast.makeText(getApplication(), "Liking not possible", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("EXTRA_WEBSITE_URL", restoLikeUrl);
            startActivity(intent);
        }
    }

    @OnClick(R.id.activity_detail_call_relative_layout)
    public void onClickPhone() {
        String phoneNumber = "tel:" + place.getInternationalPhoneNumber();
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);

    }

    @OnClick(R.id.activity_detail_website_relative_layout)
    public void onClickWebsite() {
        String websiteUrl = place.getWebsite();
        if(websiteUrl == null){
            Toast.makeText(getApplication(), "No website", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, WebviewActivity.class);
            intent.putExtra("EXTRA_WEBSITE_URL", websiteUrl);
            startActivity(intent);
            Log.e("DetAct", "website : " + websiteUrl);
        }
    }

    @OnClick(R.id.activity_detail_select_resto_fab)
    public void onClickSelectRestoButton() {
        if (getCurrentUser() != null){
            UserHelper.updateSelectedRestoId(placeId, getCurrentUser().getUid())
                    .addOnFailureListener(onFailureListener());

            UserHelper.updateSelectedRestoName(placeName, getCurrentUser().getUid())
                    .addOnFailureListener(onFailureListener());

            createJob(placeId);
        }
        Toast.makeText(getApplication(), "Resto selected : " + placeName, Toast.LENGTH_LONG).show();
    }

    private void createJob(String placeId){
        JobManager.create(getApplicationContext())
                .addJobCreator(new MyJobCreator());
        NotificationJob.showAtNoon(placeId);
    }

}
