package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class PlayerVisAdapter extends ArrayListAdapter<Player, VisTable> {
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
    private final int fontSize = 22;
    private final int numImages = 6;
    private final float baseImageWidth = 15;
    private final float baseImageHeight = 22;
    private final float imageWidth;
    private final float imageHeight;
    private final float spacing;
    private final float totalSpacing ;
    private final float groupWidth;
    private final float labelWidth;

    //public PlayerVisAdapter(FontManager fontManager, ArrayList<Player> array) {
    public PlayerVisAdapter(FontManager fontManager, ArrayList<Player> array) {
        super(array);
        font = fontManager.getGreatVibesFont( fontSize);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(font, "Acknowledgement");
        labelWidth = layout.width;

        imageHeight = font.getLineHeight();
        imageWidth = imageHeight * baseImageWidth / baseImageHeight;
        spacing = imageWidth / 7;
        totalSpacing = spacing * numImages + spacing;
        groupWidth = imageWidth * numImages + totalSpacing;
    }

    @Override
    protected VisTable createView(Player player) {
        HorizontalGroup group = new HorizontalGroup();
        group.space(spacing);
        VisTable table = new VisTable();
        table.add(new VisLabel(player.getName(), labelStyle)).width(labelWidth).padRight(5).left();
        //table.add(new VisLabel(player.getName())).width(labelWidth).padRight(5).left();
        for(int i = 0; i < player.getTotalCards(); i++){
            group.addActor(getCardImage());
        }
        table.add(group).width(groupWidth);
        table.add(new VisLabel("(" + player.getNumOutCards() + ")", labelStyle)).padRight(5);
        //table.add(new VisLabel("(" + player.getNumOutCards() + ")")).padRight(5);
        table.debug();
        return table;
    }

    private Image getCardImage(){
        Drawable drawable = VisUI.getSkin().getDrawable("card_icon");
        drawable.setMinWidth(imageWidth);
        drawable.setMinHeight(imageHeight);
        return new Image(drawable);
    }

    @Override
    protected void updateView(VisTable view, Player player) {
        super.updateView(view, player);

        //Update card number
        HorizontalGroup cardGroup = (HorizontalGroup) view.getCells().get(1).getActor();
        int numCards = cardGroup.getChildren().size;
        if(numCards > player.getTotalCards()){
            cardGroup.getChild(numCards - 1).remove();
        } else if(numCards < player.getTotalCards()){
            cardGroup.addActor(getCardImage());
        }

        //Update lives
        VisLabel miradasLabel = (VisLabel) view.getCells().get(2).getActor();
        int numMiradas = Integer.parseInt(miradasLabel.getText().substring(1,2));
        if(numMiradas != player.getNumOutCards()){
            if(player.getNumOutCards() == 2){
                miradasLabel.setColor(Color.RED);
            }
            miradasLabel.setText("(" + player.getNumOutCards() + ")");
        }
    }
}
