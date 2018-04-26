package com.openclassrooms.go4lunch.models;

import android.support.annotation.Nullable;

public class User {

    private String userId;
    private String username;
    @Nullable private String selectedRestoId;
    @Nullable private String urlPicture;

    //----------------------
    // CONSTRUCTORS
    //----------------------

    public User() { }

    public User(String userId, String username, @Nullable String urlPicture, @Nullable String selectedRestoId) {
        this.userId = userId;
        this.username = username;
        this.selectedRestoId = selectedRestoId;
        this.urlPicture = urlPicture;
    }


    //----------------------
    // GETTERS
    //----------------------

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    @Nullable
    public String getSelectedRestoId() {
        return selectedRestoId;
    }


    //----------------------
    // SETTERS
    //----------------------

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setSelectedRestoId(@Nullable String selectedRestoId) {
        this.selectedRestoId = selectedRestoId;
    }
}
