package com.openclassrooms.go4lunch.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.User;

import java.util.List;

/**
 * Created by berenger on 06/03/2018.
 */

public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesViewHolder>{

    //FOR DATA
    private final List<User> workmates;
    private final RequestManager glide;
    private boolean isMainActivityFragment = false;

    //CONSTRUCTOR
    public WorkmatesRecyclerAdapter(List<User> workmates, RequestManager glide, boolean isMainActivityFragment){
        this.workmates = workmates;
        this.glide = glide;
        this.isMainActivityFragment = isMainActivityFragment;
    }

    public User getUser(int position){
        return this.workmates.get(position);
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workmates_recycler_view_item, parent, false);

        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder viewHolder, int position){
        viewHolder.updateWithUser(this.workmates.get(position), this.glide, this.isMainActivityFragment);
    }

    @Override
    public int getItemCount() {
        return this.workmates.size();
    }
}
