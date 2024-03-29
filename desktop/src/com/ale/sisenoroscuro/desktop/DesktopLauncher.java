package com.ale.sisenoroscuro.desktop;

import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.GroupState;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ale.sisenoroscuro.GameTable;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 360;

		Group group = new Group("3NorzbzSWpCDGublbBLD", "PLIS", "Player11", 12, 12, GroupState.COMPLETE);
		Player me = new Player("Player10", "PlayerNumber10");

		new LwjglApplication(new GameTable(new DesktopPlatformFactory(), group, me), config);
	}
}
