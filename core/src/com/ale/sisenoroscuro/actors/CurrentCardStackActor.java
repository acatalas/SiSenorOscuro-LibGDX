package com.ale.sisenoroscuro.actors;

import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;


public class CurrentCardStackActor extends Image {
    private final float imageWidth;
    private final float imageHeight;
    private final Drawable drawable;
    private Vector2 position;

    public CurrentCardStackActor(float imageHeight){
        this.imageHeight = imageHeight;
        drawable = VisUI.getSkin().getDrawable("card_icon");
        this.imageWidth = this.imageHeight * drawable.getMinWidth() / drawable.getMinHeight();
        drawable.setMinWidth(this.imageWidth);
        drawable.setMinHeight(this.imageHeight);
        setDrawable(drawable);
        setAlign(Align.center);
    }

    public void setAbsolutePosition(Vector2 position){
        this.position = position;
    }

    public Vector2 getAbsolutePosition(){
        return position;
    }
}
