package com.ale.sisenoroscuro.actors;

import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CardActor extends Image {
    private Drawable drawable;
    private Card card;
    private float width, height;


    public CardActor(Drawable drawable, float width, float height, Card card){
        this.card = card;
        this.drawable = drawable;
        this.width = width;
        this.height = height;
        this.drawable.setMinWidth(this.width);
        this.drawable.setMinHeight(this.height);
        setDrawable(this.drawable);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawable.draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    public Card getCard(){
        return card;
    }

    public String getCardFullName(){
        return card.getFullName();
    }

    @Override
    public String toString() {
        return "CardActor{" +
                "drawable=" + drawable +
                ", card=" + card +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
