package com.ale.sisenoroscuro.classes;

public class Action {
    private ActionType action;
    private Card card;
    private String player;

    public Action(ActionType action, Card card, String player){
        this.action = action;
        this.card = card;
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
