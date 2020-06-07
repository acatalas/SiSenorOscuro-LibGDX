package com.ale.sisenoroscuro.classes;

import com.google.firebase.firestore.Exclude;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActionDTO {
    private ActionType action;
    private String card;
    private String player;

    public ActionDTO(){}

    public ActionDTO(ActionType action, String info, String player){
        this.action = action;
        this.card = info;
        this.player = player;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @NotNull
    @Override
    public String toString() {
        return "Action{" +
                "action=" + action +
                ", card='" + card + '\'' +
                ", player='" + player + '\'' +
                '}';
    }

    @Exclude
    public CardDTO getCardDTO(){
        Gson gson = new Gson();
        return gson.fromJson(card, CardDTO.class);
    }

    @Exclude
    public List<CardDTO> getCardDTOs(){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CardDTO>>() {}.getType();
        return gson.fromJson(card, listType);
    }

    @Exclude
    public Card getCardObject(){
        CardDTO cardDTO = getCardDTO();
        /*if(cardDTO == null) {
            return null;
        }*/
        return cardDTO.getCard();
    }

    @Exclude
    public List<Card> getCardObjects() {
        List<Card> cards = new ArrayList<>();
        for(CardDTO cardDTO : getCardDTOs()){
            cards.add(cardDTO.getCard());
        }
        return cards;
    }
}
