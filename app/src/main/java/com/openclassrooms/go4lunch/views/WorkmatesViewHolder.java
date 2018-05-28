package com.openclassrooms.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by berenger on 06/03/2018.
 */

public class WorkmatesViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.workmates_item_text) TextView mTitle;
    @BindView(R.id.workmates_item_img) ImageView mImage;

    public WorkmatesViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateWithUser(User workmate, RequestManager glide, boolean isMainActivityFragment){

        //Name
        String text;
        if(isMainActivityFragment){
            text = (workmate.getSelectedRestoId()== null)?
                    workmate.getUsername() + " hasn't decided yet":
                    workmate.getUsername() + " is eating at " + workmate.getSelectedRestoName();
        }else{
            text = workmate.getUsername() + " is joining!";
        }

        this.mTitle.setText(text);

        //Photo
        glide.load(workmate.getUrlPicture()).into(mImage);
    }

}
