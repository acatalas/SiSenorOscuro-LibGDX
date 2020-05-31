package com.ale.sisenoroscuro.classes;

public abstract class Card {

    private CardType cardType;

    public abstract String getFullName();

    public abstract String getType();

    public abstract String getModifier();

    public abstract CardType getCardType();

    @Override
    public String toString() {
        return "Card{" +
                "cardType=" + cardType +
                '}';
    }
}