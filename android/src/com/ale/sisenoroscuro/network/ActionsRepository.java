package com.ale.sisenoroscuro.network;

import android.util.Log;

import com.ale.sisenoroscuro.classes.ActionDTO;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class ActionsRepository {

    private final FirebaseHelper firebaseHelper;
    private String groupId;

    public ActionsRepository(FirebaseHelper firebaseHelper, String groupId){
        this.firebaseHelper = firebaseHelper;
        this.groupId = groupId;
    }

    public void listenActions(EventListener<QuerySnapshot> listener){
        CollectionReference collectionReference = firebaseHelper.getDatabase()
                .collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection("actions");
        collectionReference.addSnapshotListener(listener);
    }

    public void sendStartAction(String startingPlayerId){
        Log.d("APP_START", startingPlayerId);
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.START, "", startingPlayerId));
    }

    public void sendStartAction(String startingPlayerId, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.START, "", startingPlayerId))
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener);
    }

    public void sendMiradaAsesina(String receivingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.MIRADA, "", receivingPlayerId));
    }

    public void sendMiradaAsesina(String receivingPlayerId, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.MIRADA, "", receivingPlayerId))
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void sendPleadToMaster(String pleadingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD, "", pleadingPlayerId));
    }

    public void sendPleadAccepted(String pleadingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD_ACCEPTED, "", pleadingPlayerId));
    }

    public void sendPleadDenied(String pleadingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD_DENIED, "", pleadingPlayerId));
    }

    public void sendPlayExcuseCard(String currentPlayerId, ExcuseCard excuseCard){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLAY_EXCUSE, excuseCard.getModifier(), currentPlayerId));
    }
}
