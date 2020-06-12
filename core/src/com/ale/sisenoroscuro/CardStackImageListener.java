package com.ale.sisenoroscuro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class CardStackImageListener extends ClickListener {

    private Image image;
    private float centerX;
    private float centerY;

    public CardStackImageListener(Image image){
        this.image = image;
        this.centerX = Gdx.graphics.getWidth()/2 - image.getWidth()/2;
        this.centerY = Gdx.graphics.getHeight()/2 - image.getHeight()/2;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        image.addAction(getShowCardAction());
    }

    private com.badlogic.gdx.scenes.scene2d.Action getShowCardAction(){
        return Actions.parallel(
                Actions.show(),
                Actions.scaleBy(ActionGenerator.CARD_SCALING_FACTOR, ActionGenerator.CARD_SCALING_FACTOR, 0.3f),
                Actions.moveTo(centerX, centerY, 0.3f));
    }
}
