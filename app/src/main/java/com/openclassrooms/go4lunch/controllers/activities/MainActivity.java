package com.openclassrooms.go4lunch.controllers.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.UserHelper;
import com.openclassrooms.go4lunch.controllers.fragments.WorkmatesListFragment;
import com.openclassrooms.go4lunch.managers.PlacesMgr;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.googlemaps.AutocompleteAPI;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;
import com.openclassrooms.go4lunch.controllers.fragments.RestoListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener{

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigation;

    private static final String TAG = MainActivity.class.getSimpleName();

    // FOR NAVIGATION DRAWER
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    //FOR HTTP REQUEST
    private String placeId;

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

    private PlacesMgr placesMgr = PlacesMgr.getInstance();
    private final WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();
    private SupportMapFragment mapFragment = SupportMapFragment.newInstance();


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
        getMap(mapFragment);
        replaceCurrentFragment(mapFragment);

        //Set Workmates list
        setWorkmatesList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    //------------------------
    // NAVIGATION
    //------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mMap.clear();
                getAndShowSearchedRestaurants(query);
                searchView.clearFocus();

                //update RestoList
                RestoListFragment restoListFragment = (RestoListFragment)
                        getSupportFragmentManager().findFragmentById(R.id.recycler_view);

                if(restoListFragment != null) {
                    restoListFragment.showNearbyRestaurants();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getAndShowNearbyRestaurants();
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        //Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_restaurant :
                goToDetailActivity();
                break;
            case R.id.activity_main_drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.activity_main_drawer_logout:
                logoutUser();
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

        String userName = getCurrentUser().getDisplayName();
        String userEmail = getCurrentUser().getEmail();
        Uri userPhotoUrl = getCurrentUser().getPhotoUrl();

        View hView =  navigationView.getHeaderView(0);
        ImageView nav_picture = (ImageView)hView.findViewById(R.id.nav_header_profile_img);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_header_username);
        TextView nav_email = (TextView)hView.findViewById(R.id.nav_header_user_email);

        Glide.with(getApplicationContext())
                .load(userPhotoUrl)
                .into(nav_picture);
        nav_user.setText(userName);
        nav_email.setText(userEmail);
    }

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
                    case R.id.workmates_list:
                        selectedFragment = new WorkmatesListFragment();
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


    // -----------------
    // SET DATA
    // -----------------

    private void getAndShowNearbyRestaurants(){
        placesMgr.executeHttpRequestToFindNearbyRestaurants(new DisposableObserver<PlacesAPI>(){
            @Override
            public void onNext(PlacesAPI places) {
                Log.e("MapsActivity", "On Next");

                mMap.clear();

                String [] placesIds = new String[places.getResults().size()];

                for(int i = 0; i<places.getResults().size(); i++){
                    showRestaurantOnMapWithMarker(places.getResults().get(i));
                    placesIds[i] = places.getResults().get(i).getPlaceId();
                }
                setRestaurantsList(placesIds);
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
    }

    private void getAndShowSearchedRestaurants(String search){

        placesMgr.executeHttpRequestToFindSearchedRestaurants(search, new DisposableObserver<AutocompleteAPI>(){

            @Override
            public void onNext(AutocompleteAPI places) {

                String [] placesIds = new String[places.getPredictions().size()];

                for (int i = 0; i<places.getPredictions().size(); i++){
                    placesIds[i] = places.getPredictions().get(i).getPlaceId();

                    placesMgr.executeHttpRequestToGetRestaurantDetails(placesIds[i], new DisposableObserver<PlacesAPI>() {
                        @Override
                        public void onNext(PlacesAPI placesAPI) {
                            showRestaurantOnMapWithMarker(placesAPI.getResult());
                        }

                        @Override
                        public void onError(Throwable e) {}

                        @Override
                        public void onComplete() {}
                    });
                }
                setRestaurantsList(placesIds);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("MA getSearchedResto", "On Error"+Log.getStackTraceString(e));
            }
            @Override
            public void onComplete() {}
        });
    }

    private void setRestaurantsList(String [] placesIds){
        final List<PlacesAPI.Result> nearbyRestaurants = new ArrayList<>();

        for(int i = 0; i<placesIds.length; i++){

            placesMgr.executeHttpRequestToGetRestaurantDetails(placesIds[i], new DisposableObserver<PlacesAPI>(){
                @Override
                public void onNext(PlacesAPI place) {
                    Log.e("DetailActivity", "On Next");
                    nearbyRestaurants.add(place.getResult());
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("DetailActivity", "On Error"+Log.getStackTraceString(e));
                }

                @Override
                public void onComplete() {
                    Log.e("DetailActivity", "On Complete");
                }
            });
        }
        placesMgr.setNearbyRestaurants(nearbyRestaurants);
    }

    private void setWorkmatesList(){
        UserHelper.getAllUsers().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> workmates = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        //Add only if different from current authenticated user
                        if(! user.getUserId().equals(getCurrentUser().getUid())){
                            workmates.add(user);
                        }
                    }
                    workmatesMgr.setWorkmates(workmates);

                } else {
                    Log.e("MA getAllUsers", "Error getting documents: ", task.getException());
                }
            }
        });
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
                getDeviceLocationToShowNearbyRestaurants();

                //Position Location Button in the bottom right corner
                positionLocationButton();

                mMap.setOnInfoWindowClickListener(MainActivity.this);
            }
        });
    }

    private void getDeviceLocationToShowNearbyRestaurants() {
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

                            placesMgr.setCurrentLocation(mLastKnownLocation);

                            //Get and show restaurants from httpRequest if not done yet
                            if(placesMgr.getNearbyRestaurants() == null){
                                getAndShowNearbyRestaurants();
                            }else{
                                for(int i = 0; i<placesMgr.getNearbyRestaurants().size(); i++){
                                    showRestaurantOnMapWithMarker(placesMgr.getNearbyRestaurants().get(i));
                                }
                            }

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

    private void positionLocationButton(){
        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 100);
    }

    private void showRestaurantOnMapWithMarker(PlacesAPI.Result restaurant){

            Double lat = restaurant.getGeometry().getLocation().getLat();
            Double lng = restaurant.getGeometry().getLocation().getLng();
            String name = restaurant.getName();
            String tag = restaurant.getPlaceId();

            int workmates = workmatesMgr.getNbWorkmatesGoing(restaurant.getPlaceId());
            float hue = (workmates != 0)?
                    BitmapDescriptorFactory.HUE_GREEN:
                    BitmapDescriptorFactory.HUE_ORANGE;

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(hue)))
                    .setTag(tag);
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


    // -----------------
    // USER MANAGEMENT
    // -----------------

    @Nullable
    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    private void logoutUser(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
    }


    // -----------------
    // INTENTS
    // -----------------

    private void goToDetailActivity(){
        UserHelper.getUser(this.getCurrentUser().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User currentUser = documentSnapshot.toObject(User.class);
                        placeId = currentUser.getSelectedRestoId();
                        if(placeId == null){
                            Toast.makeText(getApplicationContext(), "No Restaurant Selected yet", Toast.LENGTH_LONG).show();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                            intent.putExtra("PLACE_ID", placeId);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        placeId = marker.getTag().toString();

        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("PLACE_ID", placeId);
        startActivity(intent);
    }
}
