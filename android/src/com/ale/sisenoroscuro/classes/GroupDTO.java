package com.ale.sisenoroscuro.classes;

import com.google.firebase.firestore.DocumentId;

import org.jetbrains.annotations.NotNull;

public class GroupDTO {
    @DocumentId
    private String id;
    private String name;
    private String masterId;
    private int numPlayers;
    private int maxPlayers;
    private GroupState state;

    public GroupDTO(){
    }

    public GroupDTO(String name){
        this.state = GroupState.WAITING;
        this.name = name;
    }

    public GroupDTO(String name, int numPlayers){
        this(name);
        this.numPlayers = numPlayers;
        this.maxPlayers = numPlayers;

    }

    public GroupDTO(String name, int numPlayers, int maxPlayers){
        this(name, numPlayers);
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public GroupState getState() {
        return state;
    }

    public void setState(GroupState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    @Override
    public String toString() {
        return "Group{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", masterId='" + masterId + '\'' +
                ", numPlayers=" + numPlayers +
                ", maxPlayers=" + maxPlayers +
                ", state=" + state +
                '}';
    }
}
