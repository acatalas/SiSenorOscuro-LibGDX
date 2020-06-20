package com.ale.sisenoroscuro.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ModalDialog extends Window {
    public ModalDialog(Skin skin) {
        super("", skin.get(WindowStyle.class));
        setSkin(skin);
        setModal(true);
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
