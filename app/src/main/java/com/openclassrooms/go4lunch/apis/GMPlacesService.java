package com.openclassrooms.go4lunch.apis;


import com.openclassrooms.go4lunch.models.googlemaps.AutocompleteAPI;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by berenger on 01/03/2018.
 */

interface GMPlacesService {

    String apiKey = "key=AIzaSyCQo0hGB4Wbb7r59_vuqQ5Aksk6MM8_St0";

    @GET("nearbysearch/json?" + apiKey)
    Observable<PlacesAPI> getPlaces(
            @Query("location") String location,
            @Query("type") String type,
            @Query("radius") int radius
    );


    @GET("autocomplete/json?&strictbounds&" + apiKey)
    Observable<AutocompleteAPI> getSearchedPlaces(
            @Query("input") String input,
            @Query("types") String types,
            @Query("location") String location,
            @Query("radius") int radius
    );

    @GET("details/json?" + apiKey)
    Observable<PlacesAPI> getRestaurant(
            @Query("placeid") String placeID        //ChIJybapZV-5FkgRL8PS5TmfV-U
    );

    //GLOBAL RETROFIT BUILDER
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
