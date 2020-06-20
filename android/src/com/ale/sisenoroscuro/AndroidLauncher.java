package com.ale.sisenoroscuro;

import android.os.Bundle;

import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.GroupState;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	private Group group;
	private Player player;
	private Game gameTable;
	private PlatformFactory platformFactory;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		platformFactory = new AndroidPlatformFactory(getApplicationContext());

		Bundle extras = getIntent().getExtras();

		group = new Group(extras.getString("groupId"),
				extras.getString("playerName"),
				extras.getString("masterId"),
				extras.getInt("numPlayers"),
				extras.getInt("numPlayers"),
				GroupState.COMPLETE);

		player = new Player(extras.getString("playerId"),
				extras.getString("playerName"));

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;

		//String id, String name, String masterId, int numPlayers, int maxPlayers, GroupState state
		//Group group = new Group("XbhBIbkFsPmB0qp4zjZQ", "Aaaa", 4, 4, GroupState.COMPLETE);
		//group.setMasterId("i5ogRmhGHU0W4RVqfFQf");
		//Player player = new Player("9HvZNqOCjhnH86oG7dcv", "Acknowledgement");
		gameTable = new GameTable(platformFactory, group, player);
		initialize(gameTable, config);
	}

	@Override
	protected void onStop() {
		gameTable.dispose();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
	}
}
