package com.ale.sisenoroscuro.actors;

import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MiradaCardActor extends CardActor {
    private boolean opaque;

    public MiradaCardActor(Drawable drawable, float width, float height) {
        super(drawable, width, height, null);
        opaque = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(opaque){
            batch.setColor(0,0,0,0.5f);
            getDrawable().draw(batch, getX(), getY(), getWidth(), getHeight());
            batch.setColor(1,1,1,1);
        }
    }

    public void showCard(){
        this.opaque = false;
    }

    public void hideCard(){
        this.opaque = true;
    }
}
