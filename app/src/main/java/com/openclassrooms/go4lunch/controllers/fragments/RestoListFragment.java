package com.openclassrooms.go4lunch.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.controllers.activities.DetailActivity;
import com.openclassrooms.go4lunch.utils.ItemClickSupport;
import com.openclassrooms.go4lunch.views.RestoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestoListFragment extends Fragment {

    // FOR DESIGN
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    private RestoRecyclerAdapter adapter;
    private List<PlacesAPI.Result> nearbyRestaurants;
    PlacesMgr placesMgr = PlacesMgr.getInstance();

    public RestoListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        showNearbyRestaurants();

        return view;
    }


    // -----------------
    // CONFIGURATION
    // -----------------

    void configureRecyclerView(){
        this.nearbyRestaurants = new ArrayList<>();
        this.adapter = new RestoRecyclerAdapter(this.nearbyRestaurants, Glide.with(this));
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.resto_recycler_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String placeId = adapter.getResult(position).getPlaceId();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("PLACE_ID", placeId);
                        startActivity(intent);
                    }
                });
    }

    // -----------------
    // UPDATE UI
    // -----------------

    void showNearbyRestaurants(){
        List<PlacesAPI.Result> places = placesMgr.getNearbyRestaurants();
        this.nearbyRestaurants.clear();
        this.nearbyRestaurants.addAll(places);
        adapter.notifyDataSetChanged();
    }
}
