package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardSubType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Player;

import java.util.List;

public class PlayerManager {
    private String masterId;
    private String playerId;
    private List<Player> players;

    public PlayerManager(List<Player> players, String playerId, String masterId){
        this.players = players;
        this.playerId = playerId;
        this.masterId = masterId;
    }

    private int findPlayerIndex(String id){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

    public boolean amIPlaying(){
        return getPlayer(playerId).isPlaying();
    }

    public void makeMeAvailable(){
        getPlayer(playerId).setIsAvailable(true);
    }

    public void makeMeNotAvailable(){
        getPlayer(playerId).setIsAvailable(false);
    }

    public void makeAllPlayersAvailable(){
        for(Player player : players){
            player.setIsAvailable(true);
        }
    }

    public void makeAllButMeAndMasterAvailable(){
        for(Player player : players){
            if(player.getId().equals(masterId) || player.getId().equals(playerId)){
                player.setIsAvailable(false);
            } else {
                player.setIsAvailable(true);
            }
        }
    }

    public void makeAllPlayersNotAvailable(){
        for(Player player: players){
            player.setIsAvailable(false);
        }
    }

    public void playExcuseCard(String playerId){
        getPlayer(playerId).removeExcuseCard();
        getPlayer(playerId).incrementTurnNumber();
    }

    public void playInterrumpirCard(String playerId, ActionCard actionCard){
        getPlayer(playerId).removeExcuseCard();
        if(actionCard.getCardSubType() == CardSubType.INTERRUMPIR){
            getPlayer(playerId).removeInterrumpirCard();
        } else {
            getPlayer(playerId).removeAmbasCard();
        }
    }

    public void playPasarElMarronCard(String playerId, ActionCard actionCard){
        if(actionCard.getCardSubType() == CardSubType.MARRON){
            getPlayer(playerId).removePasarMarronCard();
        } else {
            getPlayer(playerId).removeAmbasCard();
        }

    }

    public void giveCardToPlayer(Card card, String playerId){
        if(card.getCardType() == CardType.ACTION){
            switch (((ActionCard)card).getCardSubType()){
                case MARRON:
                    getPlayer(playerId).addPasarMarronCard();
                    break;
                case INTERRUMPIR:
                    getPlayer(playerId).addInterrumpirCard();
                    break;
                default: //AMBAS
                    getPlayer(playerId).addAmbasCard();
            }
        } else {
            getPlayer(playerId).addExcuseCard();
        }
    }

    public byte getNumberOfCardsOfPlayer(String playerId){
        return getPlayer(playerId).getTotalCards();
    }

    public byte getNumberOfActionCards(String playerId){
        return getPlayer(playerId).getNumActionCards();
    }

    public byte getNumberOfExcuseCards(String playerId){
        return getPlayer(playerId).getNumExcuseCards();
    }

    public void setActivePlayer(String id){
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            if(player.getId().equals(id)){
                player.setIsPlaying(true);
                player.resetTurnNumber();
            } else {
                player.setIsPlaying(false);
            }
        }
    }

    public void selectPlayer(String playerId){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getId().equals(playerId)){
                players.get(i).setSelected(true);
            } else {
                players.get(i).setSelected(false);
            }
        }
    }

    public void giveOutCardToPlayer(String playerId){
        getPlayer(playerId).addOutCard();
    }

    public void deselectPlayer(String playerId){
        getPlayer(playerId).setSelected(false);
    }

    public boolean isPlayerActive(String playerId){
        return getPlayer(playerId).isPlaying();
    }

    public Player getPlayer(String playerId){
        return players.get(findPlayerIndex(playerId));
    }

    public String getPlayerName(String playerId) {
        return getPlayer(playerId).getName();
    }

    public String getActivePlayerId() {
        return getActivePlayer().getId();
    }

    public String getActivePlayerName() {
        return getActivePlayer().getName();
    }

    public Player getActivePlayer() {
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).isPlaying()){
                return players.get(i);
            }
        }
        return null;
    }

    public void resetPlayerTurn(String playerId){
        getPlayer(playerId).resetTurnNumber();
    }


    public byte getNumOutCards(String playerId){
        return getPlayer(playerId).getNumOutCards();
    }

    public void getRidOfAllCards(String playerId){
        getPlayer(playerId).removeAllCards();
    }

    public Player getSelectedPlayer(){
        for(Player player : players){
            if(player.isSelected()){
                return player;
            }
        }
        return null;
    }

    public boolean isPlayerPleading(String playerId){
        return getPlayer(playerId).isPleading();
    }

    public void setPleadingPlayer(String playerId, boolean isPleading){
        getPlayer(playerId).setIsPleading(isPleading);
    }

    public boolean isSomeonePleading(){
        for(Player player : players){
            if(player.isPleading()){
                return true;
            }
        }
        return false;
    }

    public boolean canPlayerPlayCard(String playerId, Card card, Card standbyCard){
        Player player = getPlayer(playerId);

        //if the player is active, must play excuse on first turn
        //excuse or pasar | ambas on second and third
        //and pasar on fourth

        if(isSomeonePleading()){
            return false;
        }

        if(player.isPlaying()){
            if(player.getNumTurn() == 0){
                return card.getCardType() == CardType.EXCUSE;
            } else if(player.getNumTurn() == 3){
                return card.getCardType() == CardType.ACTION &&
                        ((ActionCard)card).getCardSubType() != CardSubType.INTERRUMPIR;
            } else {
                return card.getCardType() == CardType.EXCUSE ||
                        (card.getCardType() == CardType.ACTION &&
                                ((ActionCard)card).getCardSubType() != CardSubType.INTERRUMPIR);
            }
        } else if(getActivePlayer() != null) { //If the player is not active then they can only play interrumpir

            if((player.getNumAmbasCards() + player.getNumInterrumpirCards()) == 0 ||
                    player.getNumExcuseCards() == 0){
                return false;
            }

            if(standbyCard == null){
                return card.getCardType() == CardType.EXCUSE ||
                        (card.getCardType() == CardType.ACTION &&
                                ((ActionCard)card).getCardSubType() != CardSubType.MARRON);
            } else if(standbyCard.getCardType() == CardType.EXCUSE){
                return card.getCardType() == CardType.ACTION &&
                        ((ActionCard)card).getCardSubType() != CardSubType.MARRON;
            } else {
                return card.getCardType() == CardType.EXCUSE;
            }
        }
        return false;
    }
}
