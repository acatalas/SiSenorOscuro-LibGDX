package com.ale.sisenoroscuro;

public interface PlatformFactory {

    void listenForAction(String groupId, ActionListener actionListener);
    void getPlayers(String groupId, ActionListener actionListener);
    void sendStartAction(String groupId, String playerId);
}
