package com.ale.sisenoroscuro;

import android.content.Context;
import android.util.Log;

import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionDTO;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.ActionsRepository;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AndroidPlatformFactory implements PlatformFactory {
    private Context context;
    private ActionsRepository actionsRepository;

    public AndroidPlatformFactory(Context context){
        this.context = context;
    }

    @Override
    public void getPlayers(String groupId, ActionListener actionListener){
        new GroupRepository(FirebaseHelper.getInstance(context)).getPlayersInGroupTask(groupId).addOnSuccessListener((queryDocumentSnapshots) -> {
            List<Player> players = new ArrayList<>();
            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
            for (int i = 0; i < docs.size(); i++) {
                PlayerDTO playerDTO = docs.get(i).toObject(PlayerDTO.class);
                players.add(new Player(playerDTO.getId(), playerDTO.getName()));
            }

            actionListener.onPlayersRetrieved(players);
        });
    }

    @Override
    public void sendStartAction(String groupId, String playerId) {
        getActionsRepository(groupId).sendStartAction(playerId);
    }

    @Override
    public void sendPlayExcuseCard(String groupId, String playerId, ExcuseCard excuseCard) {
        getActionsRepository(groupId).sendPlayExcuseCard(playerId, excuseCard);
    }

    @Override
    public void sendPasarElMarron(String groupId, String playerId, ActionCard actionCard) {
        getActionsRepository(groupId).sendPasarElMarron(playerId, actionCard);
    }

    @Override
    public void sendInterrumpir(String groupId, String playerId, ExcuseCard excuseCard, ActionCard actionCard) {
        getActionsRepository(groupId).sendInterrumpir(playerId, excuseCard, actionCard);
    }

    @Override
    public void sendPlead(String groupId, String playerId) {
        getActionsRepository(groupId).sendPlead(playerId);
    }

    @Override
    public void sendGameOverAction(String groupId, String playerId) {
        getActionsRepository(groupId).sendGameOver(playerId);
    }

    @Override
    public void sendGameNotOverAction(String groupId, String playerId) {
        getActionsRepository(groupId).sendGameNotOver(playerId);
    }

    @Override
    public void sendPleadAcceptedAction(String groupId, String playerId) {
        getActionsRepository(groupId).sendPleadAccepted(playerId);
    }

    @Override
    public void sendPleadDeniedAction(String groupId, String playerId) {
        getActionsRepository(groupId).sendPleadDenied(playerId);
    }

    @Override
    public void sendMiradaAction(String groupId, String playerId) {

    }

    @Override
    public void getCards(String groupId, String playerId, int numActionCards, int numExcuseCards) {
        getActionsRepository(groupId).getCards(playerId, numExcuseCards, numActionCards);
    }

    @Override
    public void getCard(String groupId, String playerId, CardType type) {
        getActionsRepository(groupId).getCard(playerId, type.getLetter());
    }

    @Override
    public void getAllNewCards(String groupId, String playerId) {
        getActionsRepository(groupId).getCards(playerId, 3, 3);
    }

    @Override
    public void getPleadCard(String groupId, String playerId){
        getActionsRepository(groupId).getPleadCard(playerId);
    }

    @Override
    public void listenForAction(String groupId, ActionListener actionListener) {
        getActionsRepository(groupId).listenActions((queryDocumentSnapshots, e) -> {
            if (e != null) {
                System.out.println(e.toString());
                return;
            }

            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                if(dc.getType() == DocumentChange.Type.ADDED){
                    ActionDTO action = dc.getDocument().toObject(ActionDTO.class);

                    //Check if action has no cards
                    if(action.getCard().isEmpty()){
                        actionListener.onActionReceived(new Action(action.getAction(), action.getPlayer()));
                    } else if(action.getAction() == ActionType.PLAY_INTERRUMPIR){
                        actionListener.onActionReceived(new Action(action.getAction(), action.getCardObjects(), action.getPlayer()));
                    } else {
                        actionListener.onActionReceived(new Action(action.getAction(), action.getCardObject(), action.getPlayer()));
                    }
                }
            }
        });
    }

    private ActionsRepository getActionsRepository(String groupId){
        if(actionsRepository == null){
            actionsRepository = new ActionsRepository(FirebaseHelper.getInstance(context), groupId);
        }
        return actionsRepository;
    }
}
