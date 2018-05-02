package com.openclassrooms.go4lunch.managers;

import android.location.Location;
import android.util.Log;

import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlacesMgr {

    private static final PlacesMgr ourInstance = new PlacesMgr();
    private PlacesAPI.Result mRestaurant;
    private List<PlacesAPI.Result> mNearbyRestaurants;
    private Location currentLocation;
    private Disposable mDisposable;


    public static PlacesMgr getInstance() {
        return ourInstance;
    }

    private PlacesMgr() {
    }

    // --------------------------
    // GETTERS
    // --------------------------

    public PlacesAPI.Result getRestaurant() {
        return mRestaurant;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<PlacesAPI.Result> getNearbyRestaurants() {
        return mNearbyRestaurants;
    }

    // --------------------------
    // SETTERS
    // --------------------------

    public void setRestaurant(PlacesAPI.Result restaurant) {
        mRestaurant = restaurant;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setNearbyRestaurants(List<PlacesAPI.Result> nearbyRestaurants) {
        mNearbyRestaurants = nearbyRestaurants;
    }

    // --------------------------
    // REQUESTS
    // --------------------------

    public void executeHttpRequestToFindNearbyRestaurants(String position, DisposableObserver<PlacesAPI> observer){
        Log.e("MainActivity Request", "position = " + position);
        this.mDisposable = GMPlacesStreams.streamFetchNearbyRestaurants(position)
                .subscribeWith(observer);
    }

    public void executeHttpRequestToGetRestaurantDetails(final String placeId, DisposableObserver<PlacesAPI> observer){
        this.mDisposable = GMPlacesStreams.streamFetchRestaurant(placeId)
                .subscribeWith(observer);
    }
}
