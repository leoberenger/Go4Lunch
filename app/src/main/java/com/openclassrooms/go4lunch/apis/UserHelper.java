package com.openclassrooms.go4lunch.apis;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.User;

public class UserHelper {

        private static final String COLLECTION_NAME = "users";

        // --- COLLECTION REFERENCE ---

        public static CollectionReference getUsersCollection(){
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        }

        // --- CREATE ---

        public static Task<Void> createUser(String uid, String username, String urlPicture) {
            User userToCreate = new User(uid, username, urlPicture, null, null);
            return UserHelper.getUsersCollection().document(uid).set(userToCreate);
        }

        // --- GET ---

        public static Task<DocumentSnapshot> getUser(String uid){
            return UserHelper.getUsersCollection().document(uid).get();
        }

        public static  Task<QuerySnapshot> getAllUsers(){
            return UserHelper.getUsersCollection().get();

        }

        // --- UPDATE ---

        public static Task<Void> updateUsername(String username, String uid) {
            return UserHelper.getUsersCollection().document(uid).update("username", username);
        }

        public static Task<Void> updateSelectedRestoId(String selectedRestoId, String uid) {
            return UserHelper.getUsersCollection().document(uid).update("selectedRestoId", selectedRestoId);
        }

        public static Task<Void> updateSelectedRestoName(String selectedRestoName, String uid) {
            return UserHelper.getUsersCollection().document(uid).update("selectedRestoName", selectedRestoName);
        }

        // --- DELETE ---

        public static Task<Void> deleteUser(String uid) {
            return UserHelper.getUsersCollection().document(uid).delete();
        }
}