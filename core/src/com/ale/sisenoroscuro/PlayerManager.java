package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardType;
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
        return findPlayer(playerId).isPlaying();
    }

    public void makeMeAvailable(){
        findPlayer(playerId).setIsAvailable(true);
    }

    public void makeMeNotAvailable(){
        findPlayer(playerId).setIsAvailable(false);
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

    public void giveCardToPlayer(Card card, String playerId){
        if(card.getCardType() == CardType.ACTION){
            findPlayer(playerId).addActionCard();
        } else {
            findPlayer(playerId).addExcuseCard();
        }
    }

    public byte getNumberOfCardsOfPlayer(String playerId){
        return findPlayer(playerId).getTotalCards();
    }

    public void setActivePlayer(String id){
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);

            if(player.getId().equals(id)){
                player.setIsPlaying(true);
            } else if(player.isPlaying()){
                player.setIsPlaying(false);
            }
        }
    }

    public boolean isPlayerActive(String playerId){
        return findPlayer(playerId).isPlaying();
    }

    private Player findPlayer(String playerId){
        return players.get(findPlayerIndex(playerId));
    }

}
