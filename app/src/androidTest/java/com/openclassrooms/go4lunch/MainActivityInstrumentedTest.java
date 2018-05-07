package com.openclassrooms.go4lunch;

import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.models.googlemaps.AutocompleteAPI;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Test
    public void nearbySearchReturnResultsTest() throws Exception {

        //1 - Get the stream
        Observable<PlacesAPI> observableNearbyPlaces = GMPlacesStreams.streamFetchNearbyRestaurants("48.396396,-4.477549");

        //2 - Create a new TestObserver
        TestObserver<PlacesAPI> testObserver = new TestObserver<>();

        //3 - Launch observable
        observableNearbyPlaces.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get list of restaurants
        List<PlacesAPI.Result> restaurants = testObserver.values().get(0).getResults();

        // 5 - Verify if Restaurant n°0 has a name
        assertThat("Restaurants are returned", !restaurants.get(0).getName().equals(""));
    }

    @Test
    public void customSearchReturnResultsTest() throws Exception {

        //1 - Get the stream
        Observable<AutocompleteAPI> observableSearchPlaces = GMPlacesStreams.streamFetchSearchedRestaurants("japonais", "48.396396,-4.477549");

        //2 - Create a new TestObserver
        TestObserver<AutocompleteAPI> testObserver = new TestObserver<>();

        //3 - Launch observable
        observableSearchPlaces.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get list of restaurants
        List<AutocompleteAPI.Prediction> restaurants = testObserver.values().get(0).getPredictions();

        // 5 - Verify if Restaurant n°0 has a name
        assertThat("Restaurants are returned", !restaurants.get(0).getPlaceId().equals(""));
    }

    @Test
    public void getRestaurantReturnResultsTest() throws Exception {

        //1 - Get the stream
        Observable<PlacesAPI> observableGetRestaurant = GMPlacesStreams.streamFetchRestaurant("ChIJybapZV-5FkgRL8PS5TmfV-U");

        //2 - Create a new TestObserver
        TestObserver<PlacesAPI> testObserver = new TestObserver<>();

        //3 - Launch observable
        observableGetRestaurant.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get restaurant
        PlacesAPI.Result restaurant = testObserver.values().get(0).getResult();

        // 5 - Verify if restaurant has a title
        assertThat("Restaurant is returned", !restaurant.getName().equals(""));
    }


}
