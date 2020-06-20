package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.screens.MasterGameScreen;
import com.ale.sisenoroscuro.screens.SlaveGameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameTable extends Game {
	private final PlatformFactory platformFactory;
	private Group group;
	private Player me;
	private Screen screen;

	public GameTable(PlatformFactory platformFactory, Group group, Player me){
		this.platformFactory = platformFactory;
		this.group = group;
		this.me = me;
	}

	@Override
	public void create() {
		if(group.getMasterId().equals(me.getId())){
			screen = new MasterGameScreen(platformFactory, group, me);
		} else {
			screen = new SlaveGameScreen(platformFactory, group, me);
		}
		setScreen(screen);
	}


	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
	}
}
