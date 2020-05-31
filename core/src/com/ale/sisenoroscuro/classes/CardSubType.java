package com.ale.sisenoroscuro.classes;

public enum CardSubType {

    MARRON("p", CardType.ACTION),
    INTERRUMPIR( "i", CardType.ACTION),
    AMBAS("a", CardType.ACTION);

    private final String letter;
    private final CardType type;

    CardSubType(String letter, CardType type) {
        this.letter = letter;
        this.type = type;
    }

    public String getLetter(){
        return letter;
    }

    public CardType getCardType(){
        return type;
    }
}