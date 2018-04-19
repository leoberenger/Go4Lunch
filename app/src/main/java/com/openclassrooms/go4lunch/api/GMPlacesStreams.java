package com.openclassrooms.go4lunch.api;


import com.openclassrooms.go4lunch.api.models.PlacesAPI;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by berenger on 02/03/2018.
 */

public class GMPlacesStreams {

    public static Observable<PlacesAPI> streamFetchNearbyRestaurants(String location){
        GMPlacesService service = GMPlacesService.retrofit.create(GMPlacesService.class);
        return service.getPlaces(location, "restaurant", 1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<PlacesAPI> streamFetchRestaurant(String placeID){
        GMPlacesService service = GMPlacesService.retrofit.create(GMPlacesService.class);
        return service.getRestaurant(placeID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
