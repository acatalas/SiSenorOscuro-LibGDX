package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.actors.CardBoardActor;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardType;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;
import java.util.List;

public class SlaveGameScreen implements Screen, ActionListener {

    private static float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128
    private static float LIST_WIDTH = SCREEN_WIDTH / 2;
    private static float MIRADA_CARD_TOTAL_WIDTH = (WIDTH_UNIT * 2) / 4; //85
    private static float MIRADA_CARD_PADDING = MIRADA_CARD_TOTAL_WIDTH / 8; //28
    private static float MIRADA_CARD_WIDTH = MIRADA_CARD_TOTAL_WIDTH - MIRADA_CARD_PADDING; //85
    private static float STACK_CARD_WIDTH = WIDTH_UNIT / 5 * 3; //64

    private PlatformFactory platformFactory;

    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Skin skin;
    private Table mainTable;
    private Texture background;

    private AssetManager assetManager;
    private ShapeRenderer debug;
    private TextureAtlas textureAtlas;

    private VerticalGroup cardStackGroup;
    private CardBoardActor actionCardsBoard, excuseCardsBoard;
    private ListView<Player> playerListView;
    private PlayerVisAdapter playerListAdapter;
    private ArrayList<Player> players;
    private Image imageM1;
    private Image imageM2;
    private Image imageM3;
    private VisLabel activePlayerLabel;
    private DragAndDrop dnd;

    private FontManager fontManager;

    private PlayerManager playerManager;
    private Group group;
    private Player player;

    private byte totalFirstPlays;
    private byte numFirstPlays = 0;

    public SlaveGameScreen(PlatformFactory platformFactory, Group group, Player me){
        this.assetManager = new AssetManager();
        this.fontManager = new FontManager(assetManager);
        this.textureAtlas = new TextureAtlas("cards.atlas");

        this.platformFactory = platformFactory;
        this.group = group;
        this.player = me;
        players = new ArrayList<>();
        totalFirstPlays = (byte)((group.getNumPlayers()-1) * 6);
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

        imageM1 = getImageFromDrawable(Assets.mirada1, Scaling.fit, Align.top);
        imageM2 = getImageFromDrawable(Assets.mirada2, Scaling.fit, Align.top);
        imageM3 = getImageFromDrawable(Assets.mirada3, Scaling.fit, Align.top);

        mainTable = new VisTable();
        mainTable.setFillParent(true);
        mainTable.add(imageM1).width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).growY();//78
        mainTable.add(imageM2).width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).growY();
        mainTable.add(imageM3).width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).growY();
        mainTable.add(generateCardStackGroup()).padLeft(10).padRight(10).padTop(MIRADA_CARD_PADDING / 2).growY();
        mainTable.add(generatePlayerListView().getMainTable()).height(SCREEN_HEIGHT / 2).growX().top().padTop(MIRADA_CARD_PADDING / 2);
        mainTable.row();

        mainTable.add(generateCardBoardTable()).colspan(5);

        background = assetManager.get(Assets.wood, Texture.class);
        mainTable.setBackground(new TextureRegionDrawable(background));
        mainTable.debug();

        stage.addActor(mainTable);

        platformFactory.getPlayers(group.getId(), this);

        configureDragAndDrop();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();

        //debug.begin(ShapeRenderer.ShapeType.Line);
        /*for(int i = (int)(WIDTH_UNIT - (WIDTH_UNIT * 2)); i < 1000; i+=WIDTH_UNIT){
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

    private void loadAssets(){
        assetManager.load(Assets.wood, Texture.class);
        assetManager.update();
        assetManager.finishLoading();
    }

    private Table generateCardBoardTable(){
        Table cardTable = new Table();

        actionCardsBoard = new CardBoardActor(textureAtlas);
        excuseCardsBoard = new CardBoardActor(textureAtlas);

        cardTable.add(actionCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        cardTable.add(excuseCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        Drawable drawable = skin.getTiledDrawable("felt_tile");
        cardTable.setBackground(drawable);

        return cardTable;
    }

    private VerticalGroup generateCardStackGroup(){

        //Loads the active player label
        activePlayerLabel = new VisLabel("Acknowledgement");
        Label.LabelStyle labelStyle = activePlayerLabel.getStyle();
        labelStyle.font = fontManager.getBlackCastleFont(25);
        activePlayerLabel.setStyle(labelStyle);

        //Loads the reverse card
        Drawable visReverseDrawable = changeDrawableSize(new TextureRegionDrawable(textureAtlas.findRegion(Assets.reverso)), STACK_CARD_WIDTH);
        VisImage visReverse = new VisImage(visReverseDrawable);
        visReverse.setScaling(Scaling.fit);

        cardStackGroup = new VerticalGroup();
        cardStackGroup.space(MIRADA_CARD_PADDING / 2);
        cardStackGroup.align(Align.top);
        cardStackGroup.addActor(activePlayerLabel);
        cardStackGroup.addActor(new Image(visReverseDrawable));

        return cardStackGroup;
    }

    private ListView<Player> generatePlayerListView(){
        playerListAdapter = new PlayerVisAdapter(fontManager, players);
        playerListView = new ListView<>(playerListAdapter);
        playerListView.setItemClickListener(new ListView.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                System.out.println(player.getId());
                playerListAdapter.itemsDataChanged();
            }
        });

        playerListView.getScrollPane().setFlickScroll(true);
        playerListView.getScrollPane().setScrollbarsOnTop(true);
        playerListView.getScrollPane().setVariableSizeKnobs(false);
        playerListView.getScrollPane().setScrollingDisabled(true, false);
        return playerListView;
    }


    private void configureDragAndDrop(){
        dnd = new DragAndDrop();
        dnd.setDragActorPosition(actionCardsBoard.getCardWidth() / 2, -actionCardsBoard.getCardHeight() / 2);
        dnd.addSource(new DragAndDrop.Source(actionCardsBoard) {
            private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                Actor selectedActor = event.getTarget();
                payload.setObject(selectedActor);
                payload.setDragActor(selectedActor);
                actionCardsBoard.removeActor(selectedActor);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if(target == null){
                    actionCardsBoard.addActor((Actor) payload.getObject());
                }
            }
        });

        dnd.addSource(new DragAndDrop.Source(excuseCardsBoard) {
            private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                Actor selectedActor = event.getTarget();
                payload.setObject(selectedActor);
                payload.setDragActor(selectedActor);
                excuseCardsBoard.removeActor(selectedActor);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if(target == null){
                    excuseCardsBoard.addActor((Actor)payload.getObject());
                }

            }
        });

        dnd.addTarget(new DragAndDrop.Target(cardStackGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor selectedActor = (Actor) payload.getObject();
                actionCardsBoard.removeActor(selectedActor);
                excuseCardsBoard.removeActor(selectedActor);
            }
        });
    }

    private Image getImageFromDrawable(String assetName, Scaling scaling, int alignment){
        Image image = new Image(textureAtlas.findRegion(assetName));
        image.setScaling(scaling);
        image.setAlign(alignment);
        return image;
    }

    private Drawable changeDrawableSize(Drawable drawable, float width){
        float minWidth = drawable.getMinWidth();
        drawable.setMinWidth(width);
        drawable.setMinHeight(drawable.getMinHeight() * width / minWidth);
        return drawable;
    }

    @Override
    public void onActionReceived(Action action) {
        System.out.println(action);
        if (action.getAction() == ActionType.GET_CARD){
            playerManager.giveCardToPlayer(action.getCard(), action.getPlayer());
            if(action.getPlayer().equals(player.getId())){
                if(action.getCard().getCardType() == CardType.ACTION){
                    actionCardsBoard.addCard(action.getCard());
                    System.out.println("ACTION");
                } else {
                    excuseCardsBoard.addCard(action.getCard());
                    System.out.println("EXCUSE");
                }
            }


            numFirstPlays++;
            if(numFirstPlays >= totalFirstPlays){
                playerListAdapter.itemsChanged();
            }
        }
    }

    @Override
    public void onPlayersRetrieved(List<Player> players) {
        for(Player player : players){
            if(!player.getId().equals(group.getMasterId())){
                this.players.add(player);
            }
        }

        playerListAdapter.itemsChanged();
        playerManager = new PlayerManager(players, group.getId(), group.getMasterId());
        platformFactory.listenForAction(group.getId(), this);
    }
}
