package com.openclassrooms.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by berenger on 06/03/2018.
 */

public class WorkmatesViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.workmates_item_text)
    TextView mTitle;
    @BindView(R.id.workmates_item_img)
    ImageView mImage;

    public WorkmatesViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateWithUser(User workmate, RequestManager glide, boolean isMainActivityFragment){

        String text = (isMainActivityFragment)?
                workmate.getUsername() + " is eating at " + workmate.getSelectedRestoName():
                workmate.getUsername() + " is joining!";
        this.mTitle.setText(text);
        glide.load(workmate.getUrlPicture()).into(mImage);
    }

}
