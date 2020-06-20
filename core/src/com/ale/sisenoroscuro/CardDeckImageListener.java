package com.ale.sisenoroscuro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

public class CardDeckImageListener extends InputListener {
    private Image popUpImage;
    private Stage stage;
    private volatile boolean dragged = false;
    private volatile boolean showing = false;

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
        System.out.println("CLICKED");
        stage.addAction(Actions.delay(0.5f, Actions.run(new Runnable() {
            @Override
            public void run() {
                System.out.println("WAS DRAGGED?" + dragged);
                if(!dragged){
                    stage.addActor(popUpImage);
                    popUpImage.addAction(Actions.scaleBy(1.4f, 1.4f));
                    showing = true;
                }
                dragged = false;
            }
        })));
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        boolean inX = x >= 0 && x < popUpImage.getWidth();
        boolean inY = y >= 0 && y < popUpImage.getHeight();
        if(inX && inY){
            System.out.println("IMAGE WAS NOT DRAGGED");
            dragged = false;
        } else {
            if(showing){
                System.out.println("IMAGE WAS DRAGGED WHEN SHOWING");
                dragged = false;
            } else {
                System.out.println("IMAGE WAS DRAGGED WHEN NOT SHOWING");
                dragged = true;
            }
        }
        popUpImage.remove();
        popUpImage.addAction(Actions.scaleTo(1, 1));
        System.out.println("TOUCH UP | dragged is " + dragged);
        showing = false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        dragged = true;
        System.out.println("DRAGGED | dragged is " + dragged);
    }
}
