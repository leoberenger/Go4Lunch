package com.openclassrooms.go4lunch.models;

import android.support.annotation.Nullable;

public class User {

    private String uid;
    private String userFamilyName;
    private String userFirstName;
    private String restoChosenId;

    @Nullable
    private String urlPicture;

    //----------------------
    // CONSTRUCTORS
    //----------------------

    public User() { }

    public User(String uid, String userFamilyName, String userFirstName, String restoChosenId, String urlPicture) {
        this.uid = uid;
        this.userFamilyName = userFamilyName;
        this.userFirstName = userFirstName;
        this.restoChosenId = restoChosenId;
        this.urlPicture = urlPicture;
    }

    //----------------------
    // GETTERS
    //----------------------

    public String getUid() {
        return uid;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getRestoChosenId() {
        return restoChosenId;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }


    //----------------------
    // SETTERS
    //----------------------

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setRestoChosenId(String restoChosenId) {
        this.restoChosenId = restoChosenId;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
