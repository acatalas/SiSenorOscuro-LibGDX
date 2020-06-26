package com.ale.sisenoroscuro.recyclerView;

import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class PlayerViewHolder implements ViewHolder<Player, PlayerViewHolder.View>, Disposable {
    private Color selectedColor;
    private Color availableColor;
    private Color unavailableColor;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private final GlyphLayout layout;
    private final int numImages = 6;
    private final float baseImageWidth = 15;
    private final float baseImageHeight = 22;
    private final float imageWidth;
    private final float imageHeight;
    private final float spacing;
    private final float totalSpacing ;
    private final float groupWidth;
    private final float fontWidth;
    private final float labelWidth;

    private Skin skin;

    public PlayerViewHolder(FontManager fontManager, Skin skin){
        this.skin = skin;
        font = fontManager.getGreatVibesFont(FontManager.PLAYER_LABEL_FONT_SIZE);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        layout = new GlyphLayout(); //don't do this every frame! Store it as member
        layout.setText(font, "Acknowledgement");    //just a random word
        labelWidth = layout.width;
        layout.setText(font,"0");
        fontWidth = layout.width;

        imageHeight = font.getLineHeight();
        imageWidth = imageHeight * baseImageWidth / baseImageHeight;
        spacing = -(imageWidth / 4);
        totalSpacing = spacing * numImages - spacing;
        groupWidth = imageWidth * numImages + totalSpacing;

        selectedColor = skin.getColor(Assets.Skin.Color.gold);
        availableColor = Color.WHITE;
        unavailableColor = Color.GRAY;
    }

    @Override
    public Actor generateView(Player player) {
        View view = new View(player);
        return view;
    }

    @Override
    public Actor updateView(View view, Player player) {
        System.out.println(player.getId() + ":" + player.getTotalCards());
        int numCards = view.cardGroup.getChildren().size;   //number of cards in listview

        //if more images than real cards, delete images
        if(numCards > player.getTotalCards()){

            //FIRST ITERATION
            // cards in group = 6 = numCards
            // cards in hand  = 4 = getTotalCards()

            // cards in group = 5 = numCards
            // cards in hand  = 4 = getTotalCards(

            while (numCards > player.getTotalCards()){
                view.cardGroup.getChild(numCards - 1).remove();
                numCards--;
            }

        //if more cards than images, add images to view
        } else if(numCards < player.getTotalCards()){
            while (numCards < player.getTotalCards()){
                view.cardGroup.addActor(getCardImage());
                numCards++;
            }
        }
        return view;
    }

    public class View extends Table {
        public Label playerName, numOutCards;
        public HorizontalGroup cardGroup;

        public View(Player player){
            playerName = new Label(player.getName(), labelStyle);
            numOutCards = new Label(player.getNumOutCards() + "", labelStyle);
            cardGroup = new HorizontalGroup();

            //add player name to view
            add(playerName).width(labelWidth).padRight(20).left();

            cardGroup.space(spacing);
            for(int i = 0; i < player.getTotalCards(); i++){
                cardGroup.addActor(getCardImage());
            }
            add(cardGroup).width(groupWidth);

            numOutCards = new Label(player.getNumOutCards() + "", labelStyle);
            numOutCards.setAlignment(Align.center);
            add(numOutCards).width(fontWidth * 1.5f).padRight(5).padLeft(5);
        }
    }

    private Image getCardImage(){
        Drawable drawable = skin.getDrawable("card_icon");
        drawable.setMinWidth(imageWidth);
        drawable.setMinHeight(imageHeight);
        return new Image(drawable);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
