package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionGenerator;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.CardStackImageListener;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.PlayerVisAdapter;
import com.ale.sisenoroscuro.actors.CurrentCardStackActor;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public abstract class GameScreen implements Screen {
    protected static float SCREEN_WIDTH = Gdx.graphics.getWidth();
    protected static float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    protected static float MESSAGE_PAN_HEIGHT = SCREEN_HEIGHT / 5;
    protected static float CARD_STACK_HEIGHT = SCREEN_HEIGHT / 5 * 0.75f;
    protected static float BUTTON_PADDING_TOP = SCREEN_HEIGHT / 5 / 2;
    protected static float BUTTON_PADDING_BOTTOM = SCREEN_HEIGHT / 5 / 2;
    protected static float BUTTON_PADDING_LEFT = SCREEN_WIDTH / 5 / 2;
    protected static float BUTTON_PADDING_RIGHT = SCREEN_WIDTH / 5 / 2;
    protected static float BUTTON_HEIGHT = SCREEN_HEIGHT / 5 * 1.25f;

    protected PlatformFactory platformFactory;

    protected TextureAtlas textureAtlas;

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

    //STYLES
    protected VisLabel.LabelStyle messageLabelStyle;
    protected TextButton.TextButtonStyle textButtonStyle;

    protected CurrentCardStackActor currentCardStackActor;
    protected Image currentCardImage;
    protected Image cardImage;
    protected Image transparentOverlay;

    protected PlayerManager playerManager;
    protected Group group;
    protected Player player;

    protected byte minNumPlays;
    protected byte numPlays = 0;

    public GameScreen(PlatformFactory platformFactory, Group group, Player me){

        if(!VisUI.isLoaded()){
            VisUI.load(Gdx.files.internal("uiskin.json"));
        }

        this.textureAtlas = new TextureAtlas("cards.atlas");
        this.currentCardImage = new Image();

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

        transparentOverlay = new Image(skin.getTiledDrawable("transparent"));
        transparentOverlay.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

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
    public void resume() { }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        textureAtlas.dispose();
        if(VisUI.isLoaded()){
            VisUI.dispose();
        }
        stage.dispose();
        platformFactory.stopListeningForActions();
    }

    protected void generateUIComponents(){
        generatePlayerListView();
        generateActivePlayerLabel();
        generateCardStackActor();
    }

    protected void generatePlayerListView(){
        playerListAdapter = new PlayerVisAdapter(fontManager, players);
        playerListView = new ListView<>(playerListAdapter);
        playerListView.getScrollPane().setFlickScroll(true);
        playerListView.getScrollPane().setScrollbarsOnTop(true);
        playerListView.getScrollPane().setVariableSizeKnobs(false);
        playerListView.getScrollPane().setScrollingDisabled(true, false);
    }

    protected void generateActivePlayerLabel(){
        activePlayerLabel = new VisLabel("");
        activePlayerLabel.setAlignment(Align.center);
        loadMessageFontStyle();
    }

    /**
     * Modifies the default label style
     */
    protected void loadMessageFontStyle(){
        messageLabelStyle = new Label.LabelStyle(activePlayerLabel.getStyle());
        messageLabelStyle.font = fontManager.getBlackCastleFont(FontManager.MESSAGE_LABEL_FONT_SIZE, true);
        messageLabelStyle.background = skin.getTiledDrawable("transparent");
    }

    protected abstract void loadTextButtonStyle();

    protected void generateCardStackActor(){
        currentCardStackActor = new CurrentCardStackActor(CARD_STACK_HEIGHT);
    }

    protected void setCardStackPosition(){
        currentCardStackActor.setAbsolutePosition(
                currentCardStackActor.localToStageCoordinates(
                        new Vector2(currentCardStackActor.getWidth() / 2, currentCardStackActor.getHeight() / 2)));
    }

    /**
     * Sets the ActivePlayerLabel font style
     * @param fontSize Size of the font to use
     */
    protected void setActivePlayerLabelFont(float fontSize) {
        //Sets the style of the activePlayerLabel
        Label.LabelStyle labelStyle = activePlayerLabel.getStyle();
        labelStyle.font = fontManager.getBlackCastleFont(fontSize, false);
        activePlayerLabel.setStyle(labelStyle);
    }

    protected void setBackground(){
        background = new TextureRegionDrawable(assetManager.get(Assets.wood, Texture.class));
        mainTable.setBackground(background);
    }

    protected void loadAssets(){
        assetManager.load(Assets.wood, Texture.class);
        assetManager.update();
        assetManager.finishLoading();
    }

    /**
     * Shows an excuse card with an animation and hides it to the
     * current card stack
     * @param card Card to show
     */
    public void showPlayExcuseCardAnimation(Card card){
        if(currentCardImage != null){
            currentCardImage.remove();
        }

        currentCardImage = new Image(textureAtlas.findRegion(card.getFullName()));
        currentCardImage.setHeight(currentCardStackActor.getImageHeight());
        currentCardImage.setScaling(Scaling.fit);
        currentCardImage.setOrigin(Align.center);
        currentCardImage.setPosition(SCREEN_WIDTH/2 - currentCardImage.getWidth()/2, SCREEN_HEIGHT/2 - currentCardImage.getHeight()/2);

        currentCardImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getTarget().addAction(ActionGenerator.getMoveCardToStackAction(currentCardStackActor)); //Hides image and sends to CardStack
            }
        });

        currentCardStackActor.setTouchable(Touchable.disabled);
        currentCardStackActor.addListener(new CardStackImageListener(currentCardImage));


        currentCardImage.addAction(ActionGenerator.getShowCardAction());
        stage.addActor(currentCardImage);
    }

    protected void showMessage(String message){
        final VisLabel label = new VisLabel(message, messageLabelStyle);
        label.setAlignment(Align.center);
        label.setWrap(true);
        label.setOrigin(Align.center);

        final Container<VisLabel> labelContainer = new Container<>(label);
        labelContainer.setTransform(true);
        labelContainer.setWidth(SCREEN_WIDTH - 100);
        labelContainer.prefWidth(SCREEN_WIDTH - 100);
        labelContainer.setPosition((SCREEN_WIDTH / 2) - (labelContainer.getWidth() / 2), SCREEN_HEIGHT);
        labelContainer.setOrigin(Align.center);
        labelContainer.setScale(0.5f);

        stage.addActor(labelContainer);
        labelContainer.addAction(Actions.sequence(
                ActionGenerator.getPanDownAndScaleUpAction(-MESSAGE_PAN_HEIGHT, 1),
                Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        labelContainer.remove();
                        transparentOverlay.remove();
                    }
                })
        ));
    }

    public void showPlayAndHideCardAnimation(String name){
        cardImage = new Image(textureAtlas.findRegion(name));
        cardImage.setHeight(currentCardStackActor.getImageHeight());
        cardImage.setScaling(Scaling.fit);
        cardImage.setOrigin(Align.center);
        cardImage.setPosition(SCREEN_WIDTH/2 - cardImage.getWidth()/2, SCREEN_HEIGHT/2 - cardImage.getHeight()/2);
        cardImage.addAction(Actions.sequence(
                ActionGenerator.getShowCardAction(),
                Actions.delay(2),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        cardImage.remove();
                    }
                })));
        stage.addActor(cardImage);
    }

    protected void showGameOverMessage(String reason){
        stage.addActor(transparentOverlay);

        final VisLabel reasonLabel = new VisLabel(reason, messageLabelStyle);
        reasonLabel.setAlignment(Align.center);
        Container<VisLabel> reasonLabelContainer = new Container<>(reasonLabel);
        reasonLabelContainer.setTransform(true);
        reasonLabelContainer.setOrigin(Align.center);
        reasonLabelContainer.setPosition((SCREEN_WIDTH / 2) - (reasonLabelContainer.getWidth() / 2), SCREEN_HEIGHT);

        final VisLabel gameOverLabel = new VisLabel("GAME OVER", messageLabelStyle);
        gameOverLabel.setAlignment(Align.center);
        final Container<VisLabel> gameOverLabelContainer = new Container<>(gameOverLabel);
        gameOverLabelContainer.setTransform(true);
        gameOverLabelContainer.setOrigin(Align.center);
        gameOverLabelContainer.setPosition(SCREEN_WIDTH / 2  - (gameOverLabelContainer.getWidth() / 2), SCREEN_HEIGHT / 2);

        stage.addActor(reasonLabelContainer);

        reasonLabelContainer.addAction(
                Actions.sequence(
                        ActionGenerator.getPanDownAction(-(SCREEN_HEIGHT / 5), 3),
                        Actions.delay(1),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                stage.addActor(gameOverLabelContainer);
                                gameOverLabelContainer.addAction(
                                        Actions.sequence(
                                                Actions.scaleBy(1.5f, 1.5f, 5),
                                                Actions.run(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        platformFactory.goBackToMainMenu();
                                                    }
                                                }))
                                );
                            }
                        })
                )
        );

    }
}
