package com.openclassrooms.go4lunch.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import java.util.List;

/**
 * Created by berenger on 06/03/2018.
 */

public class RestoRecyclerAdapter extends RecyclerView.Adapter<RestoViewHolder>{

    //FOR DATA
    private final List<PlacesAPI.Result> mResults;
    private final RequestManager glide;

    //CONSTRUCTOR
    public RestoRecyclerAdapter(List<PlacesAPI.Result> results, RequestManager glide){
        this.mResults = results;
        this.glide = glide;
    }

    public PlacesAPI.Result getResult(int position){
        return this.mResults.get(position);
    }

    @NonNull
    @Override
    public RestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.resto_recycler_view_item, parent, false);

        return new RestoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoViewHolder viewHolder, int position){
        viewHolder.updateWithRestaurant(this.mResults.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return this.mResults.size();
    }
}
