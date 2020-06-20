package com.ale.sisenoroscuro.actors;
import com.ale.sisenoroscuro.CardDeckImageListener;
import com.ale.sisenoroscuro.classes.Card;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class CardBoardActor extends HorizontalGroup {
    private static final int NUM_CARDS = 3;
    private final AssetManager assetManager;
    private final List<CardActor> cardActors;

    private final float baseCardWidth = 95, baseCardHeight = 127;
    private final float cardWidth = Gdx.graphics.getWidth() / 2 / 3;
    private final float cardHeight = cardWidth * baseCardHeight / baseCardWidth;

    public CardBoardActor(AssetManager assetManager){
        this.assetManager = assetManager;
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

    public void addCard(final Card card){
        loadCardTexture(card);
        final CardActor cardActor = new CardActor(new TextureRegionDrawable(assetManager.get(card.getFullName() + ".png", Texture.class)), cardWidth, cardHeight, card);
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

    private void loadCardTexture(Card card){
        if(!assetManager.isLoaded(card.getFullName() + ".png")){
            assetManager.load(card.getFullName() + ".png", Texture.class);
            assetManager.finishLoadingAsset(card.getFullName() + ".png");
        }


        //Sreturn new TextureRegionDrawable(assetManager.get(card.getFullName() + ".png", Texture.class));
    }

    public float getCardWidth() {
        return cardWidth;
    }

    public float getCardHeight() {
        return cardHeight;
    }
}
