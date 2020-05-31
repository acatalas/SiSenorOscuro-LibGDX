package com.ale.sisenoroscuro;

import android.content.Context;
import android.util.Log;

import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardSubType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AndroidPlatformFactory implements PlatformFactory {
    private Context context;

    public AndroidPlatformFactory(Context context){
        this.context = context;
    }

    public void getPlayers(String groupId, ActionListener actionListener){
        GroupRepository groupRepository = new GroupRepository(FirebaseHelper.getInstance(context));
        groupRepository.getPlayersInGroupTask(groupId).addOnSuccessListener((queryDocumentSnapshots) -> {
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

    }

    @Override
    public void listenForAction(String groupId, ActionListener actionListener) {
        /*ActionsRepository actionsRepository = new ActionsRepository(FirebaseHelper.getInstance(context),groupId);
        actionsRepository.listenActions((queryDocumentSnapshots, e) -> {
            if (e != null) {
                System.out.println(e.toString());
                return;
            }

            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                if(dc.getType() == DocumentChange.Type.ADDED){
                    ActionDTO action = dc.getDocument().toObject(ActionDTO.class);
                    actionListener.onActionReceived(new Action(action.getAction(), action.getCardObject(), action.getPlayer()));
                }
            }

        });*/
        for(int i = 0; i < 3; i++){
            actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ExcuseCard((int)(Math.round(Math.random() * 30) +1)), "0GZPirk9kokusfVCgLFV"));
        }
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.AMBAS, true), "0GZPirk9kokusfVCgLFV"));
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.INTERRUMPIR, false), "0GZPirk9kokusfVCgLFV"));
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.MARRON, true), "0GZPirk9kokusfVCgLFV"));

    }
}
