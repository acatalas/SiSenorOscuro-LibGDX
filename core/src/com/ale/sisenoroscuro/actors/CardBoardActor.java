package com.ale.sisenoroscuro.actors;


import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;
import java.util.List;

public class CardBoardActor extends HorizontalGroup {
    private static final int NUM_CARDS = 3;
    private TextureAtlas textureAtlas;
    private List<CardActor> cardActors;

    private float baseCardWidth = 95;
    private float baseCardHeight = 127;
    private float cardWidth = Gdx.graphics.getWidth() / 2 / 3;
    private float cardHeight = cardWidth * baseCardHeight / baseCardWidth;

    public CardBoardActor(TextureAtlas textureAtlas){
        this.textureAtlas = textureAtlas;
        cardActors = new ArrayList<>(NUM_CARDS);
        space(-20);
        align(Align.center);
    }

    public void addCard(Card card){
        CardActor cardActor = new CardActor(getCardTexture(card), cardWidth, cardHeight, card);
        cardActors.add(cardActor);
        addActor(cardActor);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void removeCard(Card card){
        int cardIndex = findCardIndex(card);
        CardActor cardActor = cardActors.get(cardIndex);
        removeActor(cardActor);
        cardActors.remove(cardIndex);
    }

    private int findCardIndex(Card card){
        for(int i = 0; i < cardActors.size(); i++){
            if(cardActors.get(i).getCardFullName().equals(card.getFullName())){
                return i;
            }
        }
        return -1;
    }

    private Drawable getCardTexture(Card card){
        return new TextureRegionDrawable(textureAtlas.findRegion(card.getFullName()));
    }

    public float getCardWidth() {
        return cardWidth;
    }

    public float getCardHeight() {
        return cardHeight;
    }
}
