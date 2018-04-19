package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.api.GMPlacesStreams;
import com.openclassrooms.go4lunch.api.models.PlacesAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class DetailActivity extends AppCompatActivity {

    private Disposable mDisposable;
    @BindView(R.id.activity_detail_textview)TextView mTextViewName;
    @BindView(R.id.activity_detail_phone)TextView mTextViewPhone;
    @BindView(R.id.activity_detail_address)TextView mTextViewAddress;
    @BindView(R.id.activity_detail_type)TextView mTextViewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final String placeId = intent.getStringExtra("PLACE_ID");
        Log.e("DetailActivity", "placeID=" + placeId);
        executeHttpRequestWithRetrofit(placeId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void disposeWhenDestroy(){
        if(this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }

    private void executeHttpRequestWithRetrofit(String placeId){
        this.mDisposable = GMPlacesStreams.streamFetchRestaurant(placeId)
                .subscribeWith(new DisposableObserver<PlacesAPI>(){
                    @Override
                    public void onNext(final PlacesAPI place) {
                        Log.e("DetailActivity", "On Next");
                        showRestaurantDetails(place);
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

    private void showRestaurantDetails(PlacesAPI place){
        mTextViewName.setText(place.getResult().getName());
        mTextViewAddress.setText(place.getResult().getFormattedAddress());
        mTextViewPhone.setText(place.getResult().getFormattedPhoneNumber());
        mTextViewType.setText(place.getResult().getTypes().get(0));
        Log.e("DetailActivity UpdateUI", "Restaurant id = " + place.getResult().getPlaceId());
        Log.e("DetailActivity UpdateUI", "Restaurant name = " + place.getResult().getName());
    }
}
