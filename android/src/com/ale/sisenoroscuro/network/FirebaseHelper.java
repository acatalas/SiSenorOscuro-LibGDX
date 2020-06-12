package com.ale.sisenoroscuro.network;

import android.content.Context;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirebaseHelper {
    public static final String GROUP_COLLECTION = "groups";
    public static final String PLAYER_COLLECTION = "players";
    public static final String ACTIONS_COLLECTION = "actions";
    private static FirebaseHelper instance;
    private final FirebaseFirestore db;

    private FirebaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

    }

    public static FirebaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new FirebaseHelper(context);
        }
        return instance;
    }

    public FirebaseFirestore getDatabase() {
        return db;
    }
}
