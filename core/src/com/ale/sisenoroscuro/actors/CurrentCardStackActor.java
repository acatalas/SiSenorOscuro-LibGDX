package com.ale.sisenoroscuro.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.kotcrab.vis.ui.VisUI;


public class CurrentCardStackActor extends Image {
    private Vector2 position;
    private float imageWidth;
    private float imageHeight;
    private Drawable drawable;

    public CurrentCardStackActor(float imageHeight){
        drawable = new NinePatchDrawable(VisUI.getSkin().getPatch("card_icon"));
        this.imageHeight = imageHeight;
        imageWidth = this.imageHeight * drawable.getMinWidth() / drawable.getMinHeight();
        drawable.setMinWidth(this.imageWidth);
        drawable.setMinHeight(this.imageHeight);
        setDrawable(drawable);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void setAbsolutePosition(Vector2 position){
        this.position = position;
    }

    public Vector2 getAbsolutePosition(){
        return position;
    }
}
