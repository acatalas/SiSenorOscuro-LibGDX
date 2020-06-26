package com.ale.sisenoroscuro.desktop;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardSubType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.utils.Timer;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Handler;

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
    public void listenForAction(String groupId, final ActionListener actionListener) {
        for(int playerNum = 0; playerNum < 11; playerNum++){
            for(int i = 0; i < 3; i++){
                actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ExcuseCard((int)(Math.round(Math.random() * 30) +1)), "Player" + playerNum));
            }
            actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.AMBAS, true), "Player" + playerNum));
            actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.INTERRUMPIR, false), "Player" + playerNum));
            actionListener.onActionReceived(new Action(ActionType.GET_CARD, new ActionCard(CardSubType.MARRON, true), "Player" + playerNum));
        }

        //START ACTION
        actionListener.onActionReceived(new Action(ActionType.START, "Player10"));

        /*actionListener.onActionReceived(new Action(ActionType.MIRADA, "NMitIXWWEiXkHh2ZAzTO"));

        //MIRADA ACTION
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                actionListener.onActionReceived(new Action(ActionType.MIRADA, "NMitIXWWEiXkHh2ZAzTO"));
            }
        }, 3);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                actionListener.onActionReceived(new Action(ActionType.MIRADA, "NMitIXWWEiXkHh2ZAzTO"));
            }
        }, 6);*/

        //actionListener.onActionReceived(new Action(ActionType.PLEAD, "NMitIXWWEiXkHh2ZAzTO"));

        //INTERRUMPIR ACTION
        //final List<Card> cards = new ArrayList<>();
        //cards.add(new ActionCard(CardSubType.AMBAS, true));
        //cards.add(new ExcuseCard(33));
        //actionListener.onActionReceived(new Action(ActionType.PLAY_INTERRUMPIR, cards, "NMitIXWWEiXkHh2ZAzTO"));
    }

    @Override
    public void getPlayers(String groupId, ActionListener actionListener) {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 11; i++){
            players.add(new Player("Player" + i, "PlayerNumber" + i));
        }
        players.add(new Player("Player11", "PlayerNumber11"));
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

    @Override
    public void finnishGame(String groupId, String playerId) {

    }

    @Override
    public void stopListeningForActions() {

    }
}
