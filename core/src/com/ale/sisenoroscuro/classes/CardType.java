package com.ale.sisenoroscuro.classes;

public enum  CardType {
    ACTION("a"),
    EXCUSE("e");
    private final String letter;

    CardType(String letter) {
        this.letter = letter;
    }

    public String getLetter(){
        return letter;
    }
}

