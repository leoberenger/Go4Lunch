package com.openclassrooms.go4lunch.controllers.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.apis.UserHelper;
import com.openclassrooms.go4lunch.controllers.activities.base.BaseActivity;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.main_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_activity_button_login) Button buttonLogin;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    public int getFragmentLayout() { return R.layout.activity_login; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();
    }


    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.main_activity_button_login)
    public void onClickLoginButton() {
        if (this.isCurrentUserLogged()){
            this.startSettingsActivity();
        } else {
            this.startSignInActivity();
        }
    }

    @OnClick(R.id.main_activity_button_chat)
    public void onClickGoToMainButton() {
        if (this.isCurrentUserLogged()){
            this.startMainActivity();
        } else {
            this.showSnackBar(this.coordinatorLayout, getString(R.string.error_not_connected));
        }
    }

    // --------------------
    // REST REQUEST
    // --------------------

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String userFirstName = this.getCurrentUser().getDisplayName();
            String userFamilyName = this.getCurrentUser().getDisplayName();
            String restoChosenId = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createUser(uid, userFamilyName, userFirstName, restoChosenId, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }




    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build()
                                        ))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // --------------------
    // UI
    // --------------------

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUIWhenResuming(){
        this.buttonLogin.setText(this.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }

    // --------------------
    // UTILS
    // --------------------

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }
}