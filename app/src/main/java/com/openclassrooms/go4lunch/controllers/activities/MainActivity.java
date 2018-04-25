package com.openclassrooms.go4lunch.controllers.activities;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.GMPlacesStreams;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.controllers.fragments.RestoListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigation;

    private static final String TAG = MainActivity.class.getSimpleName();

    //------------------------
    // VARIABLES FOR MAP FRAGMENT
    //------------------------
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // FOR NAVIGATION DRAWER
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR HTTP REQUEST
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prompt the user for permission.
        getLocationPermission();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigation(bottomNavigation);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Show Map Fragment by default
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getMap(mapFragment);
        replaceCurrentFragment(mapFragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_restaurant :
                Toast.makeText(this, "Your Lunch Page", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_settings:
                Toast.makeText(this, "Settings Page", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureBottomNavigation(BottomNavigationView btmNav){

       btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.map:
                        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                        getMap(mapFragment);
                        selectedFragment = mapFragment;
                        break;
                    case R.id.restos_list:
                        selectedFragment = new RestoListFragment();
                        break;
                }
                replaceCurrentFragment(selectedFragment);
                return true;
            }
        });
    }

    private void replaceCurrentFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_frame_layout, fragment);
        transaction.commit();
    }

    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // -----------------
    // MAP FRAGMENT
    // -----------------

    private void getMap(SupportMapFragment mapFragment){

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Turn on the My Location layer and the related control on the map.
                showCurrentLocationAndEnableControls();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                //Position Location Button in the bottom right corner
                positionLocationButton();
            }
        });
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            //Get Nearby Restaurants
                            String currentPosition = mLastKnownLocation.getLatitude() + ", " + + mLastKnownLocation.getLongitude();
                            executeHttpRequestToFindNearbyRestaurants(currentPosition, new DisposableObserver<PlacesAPI>(){
                                @Override
                                public void onNext(PlacesAPI places) {
                                    Log.e("MapsActivity", "On Next");
                                    PlacesMgr placesMgr = PlacesMgr.getInstance();
                                    placesMgr.setPlaces(places);
                                    showRestaurantsOnMapWithMarkers(places);
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.e("MapsActivity", "On Error"+Log.getStackTraceString(e));
                                }
                                @Override
                                public void onComplete() {
                                    Log.e("MapsActivity", "On Complete");
                                }
                            });
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showCurrentLocationAndEnableControls() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showRestaurantsOnMapWithMarkers(PlacesAPI results){
        mMap.clear();
        // This loop will go through all the results and add marker on each location.
        for (int i = 0; i < results.getResults().size(); i++) {

            Double lat = results.getResults().get(i).getGeometry().getLocation().getLat();
            Double lng = results.getResults().get(i).getGeometry().getLocation().getLng();
            String placeName = results.getResults().get(i).getName();
            String vicinity = results.getResults().get(i).getVicinity();

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(lat, lng);

            // Position of Marker on Map
            markerOptions.position(latLng);
            // Adding Title to the Marker
            markerOptions.title(placeName + " : " + vicinity);
            // Adding colour to the marker
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            // Adding Marker to the Camera.
            Marker m = mMap.addMarker(markerOptions);

            // move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    private void positionLocationButton(){
        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 100);
    }

    // --------------------------
    // NEARBY SEARCH HTTP REQUEST
    // --------------------------

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    private void executeHttpRequestToFindNearbyRestaurants(String position, DisposableObserver<PlacesAPI> observer){
        Log.e("MainActivity Request", "position = " + position);
        this.mDisposable = GMPlacesStreams.streamFetchNearbyRestaurants(position)
                .subscribeWith(observer);
    }

    // -----------------
    // PERMISSIONS
    // -----------------
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        showCurrentLocationAndEnableControls();
    }

}
