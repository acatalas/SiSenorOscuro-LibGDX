package com.ale.sisenoroscuro.classes;


public class ExcuseCard extends Card {
    private final int imageNumber;
    private final CardType cardType = CardType.EXCUSE;

    public ExcuseCard(int imageNumber){
        this.imageNumber = imageNumber;
    }

    @Override
    public String getFullName() {
        return cardType.getLetter() + imageNumber;
    }

    @Override
    public String getType() {
        return cardType.getLetter();
    }

    @Override
    public String getModifier() {
        return imageNumber + "";
    }

    @Override
    public CardType getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return "ExcuseCard{" +
                "imageNumber=" + imageNumber +
                ", cardType=" + cardType +
                '}';
    }
}
