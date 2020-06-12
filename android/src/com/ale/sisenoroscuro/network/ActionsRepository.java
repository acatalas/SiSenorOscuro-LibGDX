package com.ale.sisenoroscuro.network;

import android.util.Log;

import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionDTO;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardDTO;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ActionsRepository {

    private final FirebaseHelper firebaseHelper;
    private final String groupId;
    private ListenerRegistration actionListenerRegistration;

    public ActionsRepository(FirebaseHelper firebaseHelper, String groupId){
        this.firebaseHelper = firebaseHelper;
        this.groupId = groupId;
    }

    public void listenActions(EventListener<QuerySnapshot> listener){
        CollectionReference collectionReference = firebaseHelper.getDatabase()
                .collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection("actions");
        actionListenerRegistration = collectionReference.addSnapshotListener(listener);
    }

    public void stopListeningForActions(){
        if(actionListenerRegistration != null){
            actionListenerRegistration.remove();
        }
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

    public void sendPleadAccepted(String pleadingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD_ACCEPTED, "", pleadingPlayerId));
    }

    public void sendPleadDenied(String pleadingPlayerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD_NOT_ACCEPTED, "", pleadingPlayerId));
    }

    public void sendPlayExcuseCard(String currentPlayerId, ExcuseCard excuseCard){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLAY_EXCUSE, CardDTO.getCardJson(excuseCard), currentPlayerId));
    }

    public void sendPasarElMarron(String playerId, ActionCard actionCard){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLAY_PASAR, CardDTO.getCardJson(actionCard), playerId));
    }

    public void sendInterrumpir(String playerId, ExcuseCard excuseCard, ActionCard actionCard){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLAY_INTERRUMPIR, CardDTO.getCardsJson(excuseCard, actionCard), playerId));
    }

    public void sendPlead(String playerId){
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.PLEAD, "", playerId));
    }

    public void sendGameOver(String playerId) {
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.GAME_OVER, "", playerId));
    }

    public void sendGameNotOver(String playerId) {
        firebaseHelper.getDatabase().collection(FirebaseHelper.GROUP_COLLECTION).document(groupId)
                .collection(FirebaseHelper.ACTIONS_COLLECTION)
                .add(new ActionDTO(ActionType.GAME_NOT_OVER, "", playerId));
    }

    public void getPleadCard(String playerId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://us-central1-sisenoroscuro.cloudfunctions.net/webApi/card").newBuilder();
        urlBuilder.addQueryParameter("groupId", groupId);
        urlBuilder.addQueryParameter("playerId", playerId);
        urlBuilder.addQueryParameter("type", "plead");
        String url = urlBuilder.build().toString();


        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.err.println(request.toString() + ": " + e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println(response.toString());
            }
        });
    }

    public void getCards(String playerId, int numExcuseCards, int numActionCards){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://us-central1-sisenoroscuro.cloudfunctions.net/webApi/cards").newBuilder();
        urlBuilder.addQueryParameter("groupId", groupId);
        urlBuilder.addQueryParameter("playerId", playerId);
        urlBuilder.addQueryParameter("excuse", numExcuseCards + "");
        urlBuilder.addQueryParameter("action", numActionCards + "");
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("APP_GET_CARD", request.toString(), e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("APP_GET_CARD", response.toString());
            }
        });
    }

    public void getCard(String playerId, String type) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://us-central1-sisenoroscuro.cloudfunctions.net/webApi/card").newBuilder();
        urlBuilder.addQueryParameter("groupId", groupId);
        urlBuilder.addQueryParameter("playerId", playerId);
        urlBuilder.addQueryParameter("type", type);
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("APP_GET_CARD", request.toString(), e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("APP_GET_CARD", response.toString());
            }
        });
    }
}
