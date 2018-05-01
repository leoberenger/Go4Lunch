package com.openclassrooms.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
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



    WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();

    public RestoViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateWithRestaurant(PlacesAPI.Result result, RequestManager glide){
        this.mName.setText(result.getName());

        int nbWorkmatesGoingToThisRestaurant = workmatesMgr.getNbWorkmatesGoing(result.getPlaceId());
        String nbWorkmates = "(" + String.valueOf(nbWorkmatesGoingToThisRestaurant) + ")";
        this.mNbWorkmates.setText(nbWorkmates);

        glide.load(result.getIcon()).into(mImage);
    }

}
