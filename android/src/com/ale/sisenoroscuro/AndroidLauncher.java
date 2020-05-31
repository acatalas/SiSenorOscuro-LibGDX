package com.ale.sisenoroscuro;

import android.os.Bundle;

import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.GroupState;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ale.sisenoroscuro.GameTable;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		//String id, String name, String masterId, int numPlayers, int maxPlayers, GroupState state
		Group group = new Group("XbhBIbkFsPmB0qp4zjZQ", "Aaaa", 4, 4, GroupState.COMPLETE);
		group.setMasterId("i5ogRmhGHU0W4RVqfFQf");
		Player me = new Player("0GZPirk9kokusfVCgLFV", "Alex3");
		initialize(new GameTable(new AndroidPlatformFactory(getApplicationContext()), group, me), config);
	}
}
