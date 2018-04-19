package com.openclassrooms.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.api.models.PlacesAPI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by berenger on 06/03/2018.
 */

public class RestoViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.fragment_main_item_title)
    TextView mTitle;
    @BindView(R.id.fragment_main_item_img)
    ImageView mImage;

    public RestoViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void updateWithArticle(PlacesAPI.Result result, RequestManager glide){
        this.mTitle.setText(result.getName());

        glide.load(result.getIcon()).into(mImage);
    }

}
