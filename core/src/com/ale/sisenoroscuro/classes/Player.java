package com.ale.sisenoroscuro.classes;


public class Player {
    private String id;
    private String name;
    private boolean isPlaying;
    private boolean isAvailable;
    private byte numExcuseCards;
    private byte numActionCards;
    private byte numOutCards;

    public Player(){
        this.isPlaying = false;
        this.isAvailable = true;
        this.numActionCards = 0;
        this.numExcuseCards = 0;
    }

    public Player(String name){
        this();
        this.name = name;
    }

    public Player(String id, String name){
        this(name);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying(){
        return isPlaying;
    }


    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }


    public boolean isAvailable(){
        return isAvailable;
    }


    public void setIsAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public byte getNumExcuseCards() {
        return numExcuseCards;
    }

    public void setNumExcuseCards(byte numExcuseCards) {
        this.numExcuseCards = numExcuseCards;
    }

    public byte getNumActionCards() {
        return numActionCards;
    }

    public void setNumActionCards(byte numActionCards) {
        this.numActionCards = numActionCards;
    }

    public byte getNumOutCards() {
        return numOutCards;
    }

    public void setNumOutCards(byte numOutCards) {
        this.numOutCards = numOutCards;
    }

    public byte getTotalCards(){
        return (byte)(numActionCards + numExcuseCards);
    }

    public void addExcuseCard(){
        numExcuseCards++;
    }

    public void removeExcuseCard(){
        numExcuseCards--;
    }

    public void addActionCard(){
        numActionCards++;
    }

    public void removeActionCard(){
        numActionCards--;
    }

    public void addOutCard(){
        numOutCards++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }
}
