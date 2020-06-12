package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;

public interface PlatformFactory {

    void getCards(String groupId, String playerId, int numActionCards, int numExcuseCards);
    void getCard(String groupId, String playerId, CardType type);
    void getAllNewCards(String groupId, String playerId);

    void getPleadCard(String groupId, String playerId);

    void listenForAction(String groupId, ActionListener actionListener);
    void getPlayers(String groupId, ActionListener actionListener);
    void sendStartAction(String groupId, String playerId);
    void sendPlayExcuseCard(String groupId, String playerId,  ExcuseCard excuseCard);
    void sendPasarElMarron(String groupId, String playerId, ActionCard actionCard);
    void sendInterrumpir(String groupId, String playerId, ExcuseCard excuseCard, ActionCard actionCard);

    void sendPlead(String groupId, String playerId);
    void sendGameOverAction(String groupId, String playerId);
    void sendGameNotOverAction(String groupId, String playerId);
    void sendPleadAcceptedAction(String groupId, String playerId);
    void sendPleadDeniedAction(String groupId, String playerId);

    void sendMiradaAction(String groupId, String playerId);

    void goBackToMainMenu();

    void stopListeningForActions();
}
