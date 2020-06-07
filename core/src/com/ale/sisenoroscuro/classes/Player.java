package com.ale.sisenoroscuro.classes;


public class Player {
    private String id;
    private String name;
    private byte numTurn;
    private boolean isPlaying;
    private boolean isAvailable;
    private boolean isSelected;
    private boolean isPleading;
    private byte numExcuseCards;
    private byte numPasarMarronCards;
    private byte numInterrumpirCards;
    private byte numAmbasCards;
    private byte numOutCards;

    public Player(){
        this.isPlaying = false;
        this.isAvailable = true;
        this.numPasarMarronCards = 0;
        this.numAmbasCards = 0;
        this.numInterrumpirCards = 0;
        this.numExcuseCards = 0;
        this.numTurn = 0;
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

    public boolean isPleading(){
        return isPleading;
    }

    public void setIsPleading(boolean isPleading){
        this.isPleading = isPleading;
    }

    public byte getNumExcuseCards() {
        return numExcuseCards;
    }

    public void setNumExcuseCards(byte numExcuseCards) {
        this.numExcuseCards = numExcuseCards;
    }

    public byte getNumActionCards() {
        return (byte)(numAmbasCards + numInterrumpirCards + numPasarMarronCards);
    }

    public byte getNumPasarMarronCards(){
        return numPasarMarronCards;
    }

    public byte getNumInterrumpirCards(){
        return numInterrumpirCards;
    }

    public byte getNumAmbasCards(){
        return numAmbasCards;
    }

    public byte getNumOutCards() {
        return numOutCards;
    }

    public void setNumOutCards(byte numOutCards) {
        this.numOutCards = numOutCards;
    }

    public byte getTotalCards(){
        return (byte)(getNumActionCards() + getNumExcuseCards());
    }

    public void addExcuseCard(){
        numExcuseCards++;
    }

    public void addPasarMarronCard(){
        numPasarMarronCards++;
    }

    public void addInterrumpirCard(){
        numInterrumpirCards++;
    }

    public void addAmbasCard(){
        numAmbasCards++;
    }

    public void removeExcuseCard(){
        numExcuseCards--;
    }

    public void removeInterrumpirCard(){
        numInterrumpirCards--;
    }

    public void removePasarMarronCard(){
        numPasarMarronCards--;
    }

    public void removeAmbasCard(){
        numAmbasCards--;
    }

    public void removeAllActionCards(){
        numAmbasCards = 0;
        numInterrumpirCards = 0;
        numPasarMarronCards = 0;
    }

    public void removeAllExcuseCards(){
        numExcuseCards = 0;
    }

    public void removeAllCards(){
        removeAllActionCards();
        removeAllExcuseCards();
    }

    public void addOutCard(){
        numOutCards++;
    }

    public void incrementTurnNumber(){
        numTurn++;
    }

    public byte getNumTurn(){
        return numTurn;
    }

    public void resetTurnNumber(){
        numTurn = 0;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", numTurn=" + numTurn +
                ", isPlaying=" + isPlaying +
                ", isAvailable=" + isAvailable +
                ", isSelected=" + isSelected +
                ", numExcuseCards=" + numExcuseCards +
                ", numActionCards=" + getNumActionCards() +
                ", numOutCards=" + numOutCards +
                '}';
    }
}
