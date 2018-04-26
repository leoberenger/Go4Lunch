package com.openclassrooms.go4lunch.models;

import android.support.annotation.Nullable;

public class User {

    private String userId;
    private String userName;
    @Nullable private String urlPicture;

    //----------------------
    // CONSTRUCTORS
    //----------------------

    public User() { }

    public User(String userId, String userName, String urlPicture) {
        this.userId = userId;
        this.userName = userName;
        this.urlPicture = urlPicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
