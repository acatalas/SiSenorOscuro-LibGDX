package com.ale.sisenoroscuro.classes;

import com.google.gson.Gson;

public class CardDTO {
    private String type;
    private String modifier;
    private boolean siSenorOscuro;

    public CardDTO(){}

    public CardDTO(Card card){
        this.type = card.getType();
        this.modifier = card.getModifier();
        if(type.equals(CardType.ACTION.getLetter())){
            this.siSenorOscuro = ((ActionCard)card).getSiSenorOscuro();
        }
    }

    public CardDTO(String type, String modifier, boolean siSenorOscuro){
        this.type = type;
        this.modifier = modifier;
        this.siSenorOscuro = siSenorOscuro;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public boolean isSiSenorOscuro() {
        return siSenorOscuro;
    }

    public void setSiSenorOscuro(boolean siSenorOscuro) {
        this.siSenorOscuro = siSenorOscuro;
    }

    public CardSubType getCardSubType(){
        for(CardSubType subType : CardSubType.values()){
            if(subType.getLetter().equals(modifier)){
                return subType;
            }
        }
        return null;
    }

    public CardType getCardType() {
        for(CardType cardType : CardType.values()){
            if (cardType.getLetter().equals(type)){
                return cardType;
            }
        }
        return null;
    }

    public Card getCard(){
        if(getCardType() == CardType.ACTION){
            return new ActionCard(getCardSubType(), siSenorOscuro);
        } else {
            return new ExcuseCard(Integer.parseInt(getModifier()));
        }
    }

    private static CardDTO getCardDTO(Card card){
        return new CardDTO(card.getType(),
                card.getModifier(),
                card.getCardType() == CardType.ACTION ? ((ActionCard)card).getSiSenorOscuro() : false);
    }

    public static String getCardJson(Card card){
        Gson gson = new Gson();
        return gson.toJson(getCardDTO(card));
    }

    public static String getCardsJson(Card card1, Card card2){
        Gson gson = new Gson();
        return gson.toJson(new CardDTO[]{getCardDTO(card1), getCardDTO(card2)});
    }
}
