package com.ale.sisenoroscuro.classes;

import java.util.List;

public class Action {
    private ActionType action;
    private Card card;
    private Card secondCard;
    private String player;

    public Action(ActionType action, String player){
        this.action = action;
        this.player = player;
    }

    public Action(ActionType action, Card card, String player){
        this.action = action;
        this.card = card;
        this.player = player;
    }

    public Action(ActionType action, List<Card> card, String player){
        this.action = action;
        this.card = card.get(0);
        this.secondCard = card.get(1);
        this.player = player;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getPlayer() {
        return player;
    }

    public Card getCard(){
        return card;
    }

    public Card getSecondCard(){
        return secondCard;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Action{" +
                "action=" + action +
                ", card='" + card + '\'' +
                ", player='" + player + '\'' +
                '}';
    }


}
