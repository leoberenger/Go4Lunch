package com.openclassrooms.go4lunch.views;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
import com.openclassrooms.go4lunch.models.googlemaps.Geometry;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by berenger on 06/03/2018.
 */

public class RestoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.resto_recycler_view_item_name) TextView mName;
    @BindView(R.id.resto_recycler_view_item_img) ImageView mImage;
    @BindView(R.id.resto_recycler_view_item_nb_workmates)TextView mNbWorkmates;
    @BindView(R.id.resto_recycler_view_item_address) TextView mAddress;
    @BindView(R.id.resto_recycler_view_item_distance) TextView mDistance;
    @BindView(R.id.resto_recycler_view_item_opening_hours) TextView mOpeningHours;
    @BindView(R.id.resto_recycler_view_item_star_1) ImageView star1;
    @BindView(R.id.resto_recycler_view_item_star_2) ImageView star2;
    @BindView(R.id.resto_recycler_view_item_star_3) ImageView star3;


    WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();
    PlacesMgr placesMgr = PlacesMgr.getInstance();

    public RestoViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateWithRestaurant(PlacesAPI.Result result, RequestManager glide){
        this.mName.setText(result.getName());

        String typeAndAddress = result.getTypes().get(0) + " - " + result.getFormattedAddress();
        this.mAddress.setText(typeAndAddress);

        String openingHours = (result.getOpeningHours().getOpenNow())? "Open" : "Closed";
        this.mOpeningHours.setText(openingHours);

        Location currentLocation = placesMgr.getCurrentLocation();
        Double currentLng = currentLocation.getLongitude();
        Double currentLat = currentLocation.getLatitude();
        Double restoLng = result.getGeometry().getLocation().getLng();
        Double restoLat = result.getGeometry().getLocation().getLat();
        int distanceInt = getDistance(currentLng, currentLat, restoLng, restoLat);
        String distance = String.valueOf(distanceInt) + " m";
        this.mDistance.setText(distance);

        int nbWorkmatesGoingToThisRestaurant = workmatesMgr.getNbWorkmatesGoing(result.getPlaceId());
        String nbWorkmates = "(" + String.valueOf(nbWorkmatesGoingToThisRestaurant) + ")";
        this.mNbWorkmates.setText(nbWorkmates);

        if(result.getRating() != null){
            double rating = result.getRating();
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
        }

        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference="
                + result.getPhotos().get(0).getPhotoReference()
                + "&key=AIzaSyAh9AJj4r9pgwUL86nz1pCX6DVpHf9T5PE";

        glide.load(photoUrl).into(mImage);
    }

    public static int getDistance(double startLati, double startLongi, double goalLati, double goalLongi){
        float[] resultArray = new float[99];
        Location.distanceBetween(startLati, startLongi, goalLati, goalLongi, resultArray);
        return (int) resultArray[0];
    }

}
