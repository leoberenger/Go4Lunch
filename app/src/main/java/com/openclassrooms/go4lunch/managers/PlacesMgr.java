package com.openclassrooms.go4lunch.managers;

import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

public class PlacesMgr {

    private static final PlacesMgr ourInstance = new PlacesMgr();
    private PlacesAPI mPlacesAPI;

    public static PlacesMgr getInstance() {
        return ourInstance;
    }

    private PlacesMgr() {
    }

    public PlacesAPI getPlaces(){
        return mPlacesAPI;
    }

    public void setPlaces(PlacesAPI places){
        this.mPlacesAPI = places;
    }

}
