package com.ale.sisenoroscuro.actors;
import com.ale.sisenoroscuro.CardDeckImageListener;
import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class CardBoardActor extends HorizontalGroup {
    private static final int NUM_CARDS = 3;
    private final TextureAtlas textureAtlas;
    private final List<CardActor> cardActors;

    private final float baseCardWidth = 95, baseCardHeight = 127;
    private final float cardWidth = Gdx.graphics.getWidth() / 2 / 3;
    private final float cardHeight = cardWidth * baseCardHeight / baseCardWidth;

    public CardBoardActor(TextureAtlas textureAtlas){
        this.textureAtlas = textureAtlas;
        cardActors = new ArrayList<>(NUM_CARDS);
        space(-20);
        align(Align.center);
    }

    public void addCardActor(final CardActor cardActor){
        cardActors.add(cardActor);
        addActor(cardActor);
    }

    public void removeAllCards(){
        for(int i = 0; i < cardActors.size(); i++){
            removeActor(cardActors.get(i));
        }
        cardActors.clear();
    }

    public void removeCardActor(CardActor cardActor){
        cardActors.remove(cardActor);
        removeActor(cardActor);
        System.out.println(cardActors.toString());
    }

    public void addCard(Card card){
        final CardActor cardActor = new CardActor(getCardTexture(card), cardWidth, cardHeight, card);
        cardActors.add(cardActor);
        addActor(cardActor);

        cardActor.addListener(new CardDeckImageListener(getStage(), cardActor.getDrawable()));
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
