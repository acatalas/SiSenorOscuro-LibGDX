package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.Player;

import java.util.List;

public interface ActionListener {

    void onActionReceived(Action action);
    void onPlayersRetrieved(List<Player> players);
}
