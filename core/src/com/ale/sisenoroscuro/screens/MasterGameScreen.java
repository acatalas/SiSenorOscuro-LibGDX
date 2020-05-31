package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.actors.ModalDialog;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.PlayerVisAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;
import java.util.List;

public class MasterGameScreen implements Screen, ActionListener {
    private static float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private static float HEIGHT_UNIT = SCREEN_HEIGHT / 7;
    private static float TABLE_HEIGHT = HEIGHT_UNIT * 4;
    private static float HEADER_HEIGHT = HEIGHT_UNIT * 1;
    private static float FOOTER_HEIGHT = HEIGHT_UNIT * 2;
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128

    private PlatformFactory platformFactory;

    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Skin skin;
    private Table mainTable;
    private Drawable background;

    private TextureAtlas textureAtlas;
    private AssetManager assetManager;
    private ShapeRenderer debug;

    private ArrayListAdapter<Player, VisTable> playerListAdapter, playerListDetailAdapter;
    private ListView<Player> playerListView, playerListDetailView;
    private ArrayList<Player> players;
    private VisLabel activePlayerLabel;
    private TextButton btnMiradaAsesina;

    private FontManager fontManager;

    private PlayerManager playerManager;
    private Group group;
    private Player player;

    private byte totalFirstPlays;
    private byte numFirstPlays = 0;

    public MasterGameScreen(PlatformFactory platformFactory, Group group, Player me){
        this.assetManager = new AssetManager();
        this.fontManager = new FontManager(assetManager);
        this.textureAtlas = new TextureAtlas("cards.atlas");

        this.platformFactory = platformFactory;
        this.group = group;
        this.player = me;
        players = new ArrayList<>();
        totalFirstPlays = (byte)((group.getNumPlayers() -1) * 6);
    }

    @Override
    public void show() {
        debug = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage(viewport);
        skin = VisUI.getSkin();

        Gdx.input.setInputProcessor(stage);

        loadAssets();

        mainTable = new VisTable();
        mainTable.setFillParent(true);

        activePlayerLabel = new VisLabel("Active player");
        activePlayerLabel.setAlignment(Align.center);
        Label.LabelStyle labelStyle = activePlayerLabel.getStyle();
        labelStyle.font = fontManager.getBlackCastleFont(50);
        activePlayerLabel.setStyle(labelStyle);

        mainTable.add(activePlayerLabel).growX().height(HEADER_HEIGHT - 20).pad(10);
        mainTable.row();
        mainTable.add(generatePlayerListView().getMainTable()).size(WIDTH_UNIT * 3, TABLE_HEIGHT);
        mainTable.row();

        btnMiradaAsesina = new TextButton("Mirada asesina", skin);
        TextButton.TextButtonStyle btnStyle = btnMiradaAsesina.getStyle();
        btnStyle.font = fontManager.getBlackCastleFont(25);
        btnMiradaAsesina.setStyle(btnStyle);

        btnMiradaAsesina.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerListDetailAdapter.itemsChanged();
                TextButton playButton = new TextButton("Play", skin);
                final ModalDialog modalDialog = new ModalDialog(skin, playButton);
                playButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        modalDialog.hide();
                    }
                });
                modalDialog.add(playerListDetailView.getMainTable()).height(200);
                modalDialog.row();
                modalDialog.add(playButton).size(200, 75);
                modalDialog.show(stage);
            }
        });

        mainTable.add(btnMiradaAsesina).size(WIDTH_UNIT * 2, FOOTER_HEIGHT - 20).pad(10);

        background = new TextureRegionDrawable(assetManager.get(Assets.wood, Texture.class));
        mainTable.setBackground(background);
        mainTable.debug();
        stage.addActor(mainTable);

        platformFactory.getPlayers(group.getId(), this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,  0,  0,  1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();

        /*debug.begin(ShapeRenderer.ShapeType.Line);
        for(int i = (int)(WIDTH_UNIT - (WIDTH_UNIT * 2)); i < 1000; i+=WIDTH_UNIT){
            debug.line(i, -10, i,1000);
        }
        debug.end();*/
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        assetManager.dispose();
        textureAtlas.dispose();
        skin.dispose();
    }

    private void senStartAction(String selectedPlayerId){
        platformFactory.sendStartAction(group.getId(), selectedPlayerId);
    }

    private ListView<Player> generatePlayerListView(){

        playerListAdapter = new PlayerVisAdapter(fontManager, players);
        playerListView = new ListView<>(playerListAdapter);
        playerListView.setItemClickListener(new ListView.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                System.out.println("ORIGINAL");
            }
        });
        playerListView.getScrollPane().setFlickScroll(true);
        playerListView.getScrollPane().setScrollbarsOnTop(true);
        playerListView.getScrollPane().setVariableSizeKnobs(false);
        playerListView.getScrollPane().setScrollingDisabled(true, false);

        playerListDetailAdapter = new PlayerVisAdapter(fontManager, players);
        playerListDetailView = new ListView<Player>(playerListDetailAdapter);
        playerListDetailView.setItemClickListener(new ListView.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                System.out.println("DETAILS");
            }
        });
        playerListDetailView.getScrollPane().setFlickScroll(true);
        playerListDetailView.getScrollPane().setScrollbarsOnTop(true);
        playerListDetailView.getScrollPane().setVariableSizeKnobs(false);
        playerListDetailView.getScrollPane().setScrollingDisabled(true, false);

        return playerListView;
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
            numFirstPlays++;
            if(numFirstPlays >= totalFirstPlays){
                playerListAdapter.itemsChanged();
            }
        }
    }

    @Override
    public void onPlayersRetrieved(List<Player> players) {
        this.players.addAll(players);
        playerListAdapter.itemsChanged();

        playerManager = new PlayerManager(players, player.getId(), group.getMasterId());
        platformFactory.listenForAction(group.getId(), this);
    }
}
