package com.openclassrooms.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.go4lunch.models.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String userFamilyName, String userFirstName, String restoChosenId, String urlPicture) {
        User userToCreate = new User(uid, userFamilyName, userFirstName, restoChosenId, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserFamilyName(String userFamilyName, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("userFamilyName", userFamilyName);
    }

    public static Task<Void> updateUserFirstName(String userFirstName, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("userFamilyName", userFirstName);
    }

    public static Task<Void> updateRestoChosenId(String restoChosenId, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restoChosenId", restoChosenId);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}