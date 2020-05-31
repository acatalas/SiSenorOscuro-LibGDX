package com.ale.sisenoroscuro.classes;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String name;
    private String masterId;
    private int numPlayers;
    private int maxPlayers;
    private GroupState state;
    private List<Player> players;

    public Group(String name, int numPlayers, int maxPlayers) {
        this.name = name;
        this.numPlayers = numPlayers;
        this.maxPlayers = maxPlayers;
        this.state = GroupState.WAITING;
        this.players = new ArrayList<>();
    }

    public Group(String name, int numPlayers, int maxPlayers, GroupState state) {
        this(name, numPlayers, maxPlayers);
        this.state = state;
    }

    public Group(String id, String name, int numPlayers, int maxPlayers, GroupState state) {
        this(name, numPlayers, maxPlayers, state);
        this.id = id;
    }

    public Group(String id, String name, String masterId, int numPlayers, int maxPlayers, GroupState state) {
        this(id, name, numPlayers, maxPlayers, state);
        this.masterId = masterId;
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

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
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

    public GroupState getState() {
        return state;
    }

    public void setState(GroupState state) {
        this.state = state;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        players.add(player);
    }



}
