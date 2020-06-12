package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.actors.CardActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

public class CardDeckImageListener extends InputListener {
    private Image popUpImage;
    private Stage stage;
    private boolean wasDragged;
    private boolean isShowing;

    public CardDeckImageListener(Stage stage, Drawable cardDrawable){
        this.stage = stage;
        popUpImage = new Image(cardDrawable);
        popUpImage.setHeight(Gdx.graphics.getHeight() * 0.9f);
        popUpImage.setScaling(Scaling.fit);
        popUpImage.setOrigin(Align.center);
        popUpImage.setPosition(Gdx.graphics.getWidth()/2 - popUpImage.getWidth()/2, Gdx.graphics.getHeight()/2 - popUpImage.getHeight()/2);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(!wasDragged){
                    isShowing = true;
                    stage.addActor(popUpImage);
                    popUpImage.addAction(Actions.scaleBy(1.4f, 1.4f));
                }
                wasDragged = false;
            }
        }, 0.8f);

        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if(isShowing){
            wasDragged = false;
        } else {
            wasDragged = true;
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        isShowing = false;
        popUpImage.remove();
        popUpImage.addAction(Actions.scaleTo(1, 1));
    }


}
