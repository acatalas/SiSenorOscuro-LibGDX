package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.PlayerVisAdapter;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class GameScreen implements Screen {
    protected static float SCREEN_WIDTH = Gdx.graphics.getWidth();
    protected static float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    protected PlatformFactory platformFactory;

    protected Stage stage;
    protected Viewport viewport;
    protected Camera camera;
    protected Skin skin;
    protected Table mainTable;
    protected Drawable background;

    protected FontManager fontManager;
    protected AssetManager assetManager;
    protected ShapeRenderer debug;

    protected ArrayListAdapter<Player, VisTable> playerListAdapter;
    protected ListView<Player> playerListView;
    protected ArrayList<Player> players;
    protected VisLabel activePlayerLabel;

    protected PlayerManager playerManager;
    protected Group group;
    protected Player player;

    protected byte minNumPlays;
    protected byte numPlays = 0;

    public GameScreen(PlatformFactory platformFactory, Group group, Player me){

        if(!VisUI.isLoaded()){
            VisUI.load(Gdx.files.internal("uiskin.json"));
        }

        this.platformFactory = platformFactory;
        this.assetManager = new AssetManager();
        this.fontManager = new FontManager(assetManager);
        this.skin = VisUI.getSkin();
        this.platformFactory = platformFactory;
        this.group = group;
        this.player = me;
        players = new ArrayList<>();
        minNumPlays = (byte)((group.getNumPlayers() -1) * 6);
    }
    @Override
    public void show() {
        debug = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        mainTable = new VisTable();
        mainTable.setFillParent(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,  0,  0,  1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();
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
        dispose();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        if(VisUI.isLoaded()){
            VisUI.dispose();
        }
        stage.dispose();
    }

    protected void generateUIComponents(){
        generatePlayerListView();
    }

    protected void generatePlayerListView(){
        playerListAdapter = new PlayerVisAdapter(fontManager, players);
        playerListView = new ListView<>(playerListAdapter);
        playerListView.getScrollPane().setFlickScroll(true);
        playerListView.getScrollPane().setScrollbarsOnTop(true);
        playerListView.getScrollPane().setVariableSizeKnobs(false);
        playerListView.getScrollPane().setScrollingDisabled(true, false);
    }

}
