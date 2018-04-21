package com.openclassrooms.go4lunch.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.api.GMPlacesStreams;
import com.openclassrooms.go4lunch.api.models.PlacesAPI;
import com.openclassrooms.go4lunch.controllers.activities.DetailActivity;
import com.openclassrooms.go4lunch.utils.ItemClickSupport;
import com.openclassrooms.go4lunch.views.RestoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestoListFragment extends Fragment {

    // FOR DESIGN
    @BindView(R.id.fragment_main_recycler_view)
    protected RecyclerView mRecyclerView;

    //FOR DATA
    private Disposable mDisposable;
    private RestoRecyclerAdapter adapter;
    private List<PlacesAPI.Result> places;

    public RestoListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        //Retrieve current position from MainActivity
        Bundle args = getArguments();
        String currentPosition = args.getString("CURRENT_POSITION");

        this.executeHttpRequestWithRetrofit(currentPosition);

        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    void configureRecyclerView(){
        this.places = new ArrayList<>();
        this.adapter = new RestoRecyclerAdapter(this.places, Glide.with(this));
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_main_item)
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
    // HTTP REQUEST (RxJava)
    // -----------------

    private void executeHttpRequestWithRetrofit(String location){
        this.mDisposable = GMPlacesStreams.streamFetchNearbyRestaurants(location)
                .subscribeWith(new DisposableObserver<PlacesAPI>(){
                    @Override
                    public void onNext(PlacesAPI places) {
                        Log.e("RestoListFragment", "On Next");
                        updateUI(places);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RestoListFragment", "On Error"+Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("RestoListFragment", "On Complete");
                    }
                });
    }

    private void disposeWhenDestroy(){
        if(this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    // -----------------
    // UPDATE UI
    // -----------------

    void updateUI(PlacesAPI results){
        places.clear();
        places.addAll(results.getResults());
        adapter.notifyDataSetChanged();
    }
}
