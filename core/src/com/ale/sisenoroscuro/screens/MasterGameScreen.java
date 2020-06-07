package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.List;

public class MasterGameScreen extends GameScreen implements Screen, ActionListener {

    private static float HEIGHT_UNIT = SCREEN_HEIGHT / 7;
    private static float TABLE_HEIGHT = HEIGHT_UNIT * 4;
    private static float HEADER_HEIGHT = HEIGHT_UNIT * 1;
    private static float FOOTER_HEIGHT = HEIGHT_UNIT * 2;
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128

    private TextureAtlas textureAtlas;

    private TextButton btnMiradaAsesina;

    public MasterGameScreen(PlatformFactory platformFactory, Group group, Player me){
        super(platformFactory, group, me);
        this.textureAtlas = new TextureAtlas("cards.atlas");
    }

    @Override
    public void show() {
        super.show();

        loadAssets();

        generateUIComponents();

        activePlayerLabel = new VisLabel("Active player");
        activePlayerLabel.setAlignment(Align.center);
        Label.LabelStyle labelStyle = activePlayerLabel.getStyle();
        labelStyle.font = fontManager.getBlackCastleFont(50, false);
        activePlayerLabel.setStyle(labelStyle);

        mainTable.add(activePlayerLabel).growX().height(HEADER_HEIGHT - 20).pad(10);
        mainTable.row();
        mainTable.add(playerListView.getMainTable()).size(WIDTH_UNIT * 3, TABLE_HEIGHT);
        mainTable.row();

        btnMiradaAsesina = new TextButton("Mirada asesina", skin);
        TextButton.TextButtonStyle btnStyle = btnMiradaAsesina.getStyle();
        btnStyle.font = fontManager.getBlackCastleFont(25, false);
        btnMiradaAsesina.setStyle(btnStyle);

        btnMiradaAsesina.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("MIRADA ASESINA");
            }
        });

        mainTable.add(btnMiradaAsesina).size(WIDTH_UNIT * 2, FOOTER_HEIGHT - 20).pad(10);

        background = new TextureRegionDrawable(assetManager.get(Assets.wood, Texture.class));
        mainTable.setBackground(background);
        mainTable.debug();
        stage.addActor(mainTable);

        platformFactory.getPlayers(group.getId(), this);
    }

    protected void generateUIComponents(){
        super.generateUIComponents();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        /*debug.begin(ShapeRenderer.ShapeType.Line);
        for(int i = (int)(WIDTH_UNIT - (WIDTH_UNIT * 2)); i < 1000; i+=WIDTH_UNIT){
            debug.line(i, -10, i,1000);
        }
        debug.end();*/
    }

    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
    }

    private void sendStartAction(String selectedPlayerId){
        platformFactory.sendStartAction(group.getId(), selectedPlayerId);
    }

    private void loadAssets(){
        assetManager.load(Assets.wood, Texture.class);
        assetManager.update();
        assetManager.finishLoading();
    }

    @Override
    public void onActionReceived(Action action) {
        System.out.println(action);
        if (action.getAction() == ActionType.GET_CARD){
            playerManager.giveCardToPlayer(action.getCard(), action.getPlayer());
            numPlays++;
            if(numPlays >= minNumPlays){
                playerListAdapter.itemsChanged();
            }
        }
    }

    @Override
    public void onPlayersRetrieved(List<Player> players) {
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getId().equals(player.getId())){
                this.player = players.get(i);
            }
        }

        this.players.addAll(players);
        playerListAdapter.itemsChanged();

        playerManager = new PlayerManager(players, player.getId(), group.getMasterId());
        platformFactory.listenForAction(group.getId(), this);
    }
}
