package com.ale.sisenoroscuro.desktop;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardSubType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Player;

import java.util.ArrayList;
import java.util.List;

public class DesktopPlatformFactory implements PlatformFactory {

    @Override
    public void getCards(String groupId, String playerId, int numActionCards, int numExcuseCards) {

    }

    @Override
    public void getCard(String groupId, String playerId, CardType type) {

    }

    @Override
    public void getAllNewCards(String groupId, String playerId) {

    }

    @Override
    public void getPleadCard(String groupId, String playerId) {

    }

    @Override
    public void listenForAction(String groupId, ActionListener actionListener) {
        for(int i = 0; i < 3; i++){
            actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ExcuseCard((int)(Math.round(Math.random() * 30) +1)), "NMitIXWWEiXkHh2ZAzTO"));
        }
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.AMBAS, true), "NMitIXWWEiXkHh2ZAzTO"));
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.INTERRUMPIR, false), "NMitIXWWEiXkHh2ZAzTO"));
        actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.MARRON, true), "NMitIXWWEiXkHh2ZAzTO"));
    }

    @Override
    public void getPlayers(String groupId, ActionListener actionListener) {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            players.add(new Player(i + "", "PlayerNumber" + 1));
        }
        players.add(new Player("NMitIXWWEiXkHh2ZAzTO", "Gyro"));
        actionListener.onPlayersRetrieved(players);
    }

    @Override
    public void sendStartAction(String groupId, String playerId) {

    }

    @Override
    public void sendPlayExcuseCard(String groupId, String playerId, ExcuseCard excuseCard) {

    }

    @Override
    public void sendPasarElMarron(String groupId, String playerId, ActionCard actionCard) {

    }

    @Override
    public void sendInterrumpir(String groupId, String playerId, ExcuseCard excuseCard, ActionCard actionCard) {

    }

    @Override
    public void sendPlead(String groupId, String playerId) {

    }

    @Override
    public void sendGameOverAction(String groupId, String playerId) {

    }

    @Override
    public void sendGameNotOverAction(String groupId, String playerId) {

    }

    @Override
    public void sendPleadAcceptedAction(String groupId, String playerId) {

    }

    @Override
    public void sendPleadDeniedAction(String groupId, String playerId) {

    }

    @Override
    public void sendMiradaAction(String groupId, String playerId) {

    }
}
