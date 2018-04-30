package com.openclassrooms.go4lunch.managers;

import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlacesMgr {

    private static final PlacesMgr ourInstance = new PlacesMgr();
    private PlacesAPI.Result mRestaurant;
    private List<PlacesAPI.Result> mNearbyRestaurants;
    private Disposable mDisposable;


    public static PlacesMgr getInstance() {
        return ourInstance;
    }

    private PlacesMgr() {
    }


    public PlacesAPI.Result getRestaurant() {
        return mRestaurant;
    }

    public List<PlacesAPI.Result> getNearbyRestaurants() {
        return mNearbyRestaurants;
    }


    public void setRestaurant(PlacesAPI.Result restaurant) {
        mRestaurant = restaurant;
    }

    public void setNearbyRestaurants(List<PlacesAPI.Result> nearbyRestaurants) {
        mNearbyRestaurants = nearbyRestaurants;
    }


    public void executeHttpRequestToGetRestaurantDetails(final String placeId, DisposableObserver<PlacesAPI> observer){
        this.mDisposable = GMPlacesStreams.streamFetchRestaurant(placeId)
                .subscribeWith(observer);
    }
}
