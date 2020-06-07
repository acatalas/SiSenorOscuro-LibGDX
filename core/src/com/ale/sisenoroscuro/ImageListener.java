package com.ale.sisenoroscuro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ImageListener extends ClickListener {

    private Image image;
    private float centerX;
    private float centerY;

    public ImageListener(Image image){
        this.image = image;
        this.centerX = Gdx.graphics.getWidth()/2 - image.getWidth()/2;
        this.centerY = Gdx.graphics.getHeight()/2 - image.getHeight()/2;
    }

    public ImageListener(Image image, float centerX, float centerY){
        this.image = image;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        image.addAction(getShowCardAction());
    }

    private com.badlogic.gdx.scenes.scene2d.Action getShowCardAction(){
        return Actions.parallel(
                Actions.show(),
                Actions.scaleBy(8.5f, 8.5f, 0.3f),
                Actions.moveTo(centerX, centerY, 0.3f));
    }
}
