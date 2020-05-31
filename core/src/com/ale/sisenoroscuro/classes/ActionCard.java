package com.ale.sisenoroscuro.classes;

public class ActionCard extends Card {
    private final CardType cardType = CardType.ACTION;
    private final CardSubType subType;
    private final boolean siSenorOscuro;

    public ActionCard(CardSubType subType, boolean siSenorOscuro) {
        this.subType = subType;
        this.siSenorOscuro = siSenorOscuro;
    }

    public boolean getSiSenorOscuro(){
        return this.siSenorOscuro;
    }

    @Override
    public String getFullName() {
        return cardType.getLetter() + subType.getLetter() + (siSenorOscuro ? "s" : "n");
    }

    @Override
    public String getType() {
        return cardType.getLetter();
    }

    @Override
    public String getModifier() {
        return subType.getLetter();
    }

    public CardSubType getCardSubType(){
        return subType;
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return "ActionCard{" +
                "cardType=" + cardType +
                ", subType=" + subType +
                ", siSenorOscuro=" + siSenorOscuro +
                '}';
    }
}
