package com.ale.sisenoroscuro.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ModalDialog extends Window {
    private Skin skin;
    public ModalDialog(Skin skin, Button button) {
        super("", skin.get(WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
    }

    private void initialize () {
        setModal(true);
        setBackground(skin.getTiledDrawable("felt_tile"));
    }

    public void hide(){
        remove();
    }

    public void show (Stage stage) {
        stage.addActor(this);
        pack();
        stage.cancelTouchFocus();
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
    }
}
