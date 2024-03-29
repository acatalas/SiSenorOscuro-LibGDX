package com.ale.sisenoroscuro;

import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/*public class PlayerListAdapter extends ArrayListAdapter<Player, VisTable> {
    private Color selectedColor;
    private Color availableColor;
    private Color unavailableColor;
    private BitmapFont font;
    private Label.LabelStyle labelStyle;
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

    private int alignment = Align.left;

    public PlayerListAdapter(FontManager fontManager, ArrayList<Player> array) {
        super(array);
        font = fontManager.getGreatVibesFont(FontManager.PLAYER_LABEL_FONT_SIZE);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        GlyphLayout layout = new GlyphLayout(); //don't do this every frame! Store it as member
        layout.setText(font, "Acknowledgement");
        labelWidth = layout.width;
        layout.setText(font,"0");
        fontWidth = layout.width;

        imageHeight = font.getLineHeight();
        imageWidth = imageHeight * baseImageWidth / baseImageHeight;
        spacing = -(imageWidth / 4);
        totalSpacing = spacing * numImages - spacing;
        groupWidth = imageWidth * numImages + totalSpacing;

        selectedColor = VisUI.getSkin().getColor("gold");
        availableColor = Color.WHITE;
        unavailableColor = Color.GRAY;
    }

    public PlayerListAdapter(FontManager fontManager, ArrayList<Player> array, int alignment) {
        this(fontManager, array);
        this.alignment = alignment;
    }

    @Override
    protected VisTable createView(Player player) {
        VisTable table = new VisTable();
        VisLabel nameLabel = new VisLabel(player.getName(), labelStyle);
        if(player.isSelected() || player.isPlaying()){
            nameLabel.setColor(selectedColor);
        } else if(player.isAvailable()){
            nameLabel.setColor(availableColor);
        } else {
            nameLabel.setColor(unavailableColor);
        }
        table.add(nameLabel).width(labelWidth).padRight(20).left();

        HorizontalGroup group = new HorizontalGroup();
        group.space(spacing);

        for(int i = 0; i < player.getTotalCards(); i++){
            group.addActor(getCardImage());
        }
        table.add(group).width(groupWidth);
        Label livesLabel = new VisLabel(player.getNumOutCards() + "", labelStyle);
        livesLabel.setAlignment(Align.center);
        table.add(livesLabel).width(fontWidth * 1.5f).padRight(5).padLeft(5);
        if(alignment == Align.right){
            table.right();
        }
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
        if(player.isSelected() || player.isPlaying()){
            view.getCells().get(0).getActor().setColor(selectedColor);
        } else if(player.isAvailable()){
            view.getCells().get(0).getActor().setColor(availableColor);
        } else {
            view.getCells().get(0).getActor().setColor(unavailableColor);
        }

        //Update card number
        HorizontalGroup cardGroup = (HorizontalGroup) view.getCells().get(1).getActor();
        int numCards = cardGroup.getChildren().size;
        if(numCards > player.getTotalCards()){

            //FIRST ITERATION
            // cards in group = 6 = numCards
            // cards in hand  = 4 = getTotalCards()

            // cards in group = 5 = numCards
            // cards in hand  = 4 = getTotalCards(

            while (numCards > player.getTotalCards()){
                cardGroup.getChild(numCards - 1).remove();
                numCards--;
            }

        } else if(numCards < player.getTotalCards()){
            while (numCards < player.getTotalCards()){
                cardGroup.addActor(getCardImage());
                numCards++;
            }
        }

        //Update lives
        VisLabel miradasLabel = (VisLabel) view.getCells().get(2).getActor();
        int numMiradas = Integer.parseInt(miradasLabel.getText().toString());
        if(numMiradas != player.getNumOutCards()){
            miradasLabel.setText( player.getNumOutCards() + "");
        }
        if(player.getNumOutCards() == 2){
            miradasLabel.setColor(Color.RED);
        }
    }
}*/
