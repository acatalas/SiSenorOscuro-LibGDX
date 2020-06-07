package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionGenerator;
import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.ImageListener;
import com.ale.sisenoroscuro.PlayerVisAdapter;
import com.ale.sisenoroscuro.actors.CardActor;
import com.ale.sisenoroscuro.actors.CardBoardActor;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.actors.CurrentCardStackActor;
import com.ale.sisenoroscuro.actors.MiradaCardActor;
import com.ale.sisenoroscuro.actors.ModalDialog;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;
import java.util.List;

public class SlaveGameScreen extends GameScreen implements Screen, ActionListener {
    private static float ACTIVE_PLAYER_LABEL_FONT_SIZE = 28;
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128
    private static float HEIGHT_UNIT = SCREEN_HEIGHT / 5;
    private static float DIALOG_LIST_HEIGHT = HEIGHT_UNIT * 3;
    private static float DIALOG_BUTTON_HEIGHT = HEIGHT_UNIT * 1;
    private static float LIST_WIDTH = SCREEN_WIDTH / 2;
    private static float MIRADA_CARD_TOTAL_WIDTH = (WIDTH_UNIT * 2) / 4; //85
    private static float MIRADA_CARD_PADDING = MIRADA_CARD_TOTAL_WIDTH / 8; //28
    private static float MIRADA_CARD_WIDTH = MIRADA_CARD_TOTAL_WIDTH - MIRADA_CARD_PADDING; //85
    private static float STACK_CARD_WIDTH = SCREEN_WIDTH / 2 / 3;
    private float STACK_CARD_GROUP_WIDTH;

    private VerticalGroup cardStackGroup;
    private CardBoardActor actionCardsBoard, excuseCardsBoard;
    private Table cardTable;
    private ArrayList<Player> playersMinusMe;
    private ArrayListAdapter<Player, VisTable> playerListDetailAdapter;
    private ListView<Player> playerListDetailView;
    private final ModalDialog modalDialog;
    private TextButton playButton;

    private CardActor standbyCardActor;
    private MiradaCardActor[] miradaImages = new MiradaCardActor[3];

    private DragAndDrop dnd;

    private Image miradaCardImage;
    private TiledDrawable transparentOverlay;

    public SlaveGameScreen(final PlatformFactory platformFactory, final Group group, Player me){
        super(platformFactory, group, me);
        this.textureAtlas = new TextureAtlas("cards.atlas");
        this.playersMinusMe = new ArrayList<>();

        playButton = new TextButton("Play", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player selectedPlayer = playerManager.getSelectedPlayer();
                if(selectedPlayer != null){
                    playerManager.deselectPlayer(selectedPlayer.getId());

                    //Send PLAY_PASAR action
                    platformFactory.sendPasarElMarron(group.getId(), selectedPlayer.getId(), (ActionCard)standbyCardActor.getCard());

                    //Send petition to get cards
                    int numExcuseCards = playerManager.getNumberOfExcuseCards(player.getId());
                    platformFactory.getCards(group.getId(), player.getId(), 0, 3 - numExcuseCards);

                    modalDialog.hide();
                    standbyCardActor = null;
                }
            }
        });
        modalDialog = new ModalDialog(skin, playButton);
    }

    @Override
    public void show() {
        super.show();

        loadAssets();

        generateUIComponents();

        messageLabelStyle = new Label.LabelStyle(activePlayerLabel.getStyle());
        messageLabelStyle.font = fontManager.getBlackCastleFont(FontManager.MESSAGE_LABEL_FONT_SIZE, true);

        TextureAtlas.AtlasRegion miradaCardRegion = textureAtlas.findRegion(Assets.mirada1);
        float mHeight = MIRADA_CARD_WIDTH * miradaCardRegion.getRegionHeight() / miradaCardRegion.getRegionWidth();

        miradaImages[0] = new MiradaCardActor(new TextureRegionDrawable(miradaCardRegion), MIRADA_CARD_WIDTH, mHeight);
        miradaImages[1] = new MiradaCardActor(new TextureRegionDrawable(textureAtlas.findRegion(Assets.mirada2)), MIRADA_CARD_WIDTH, mHeight);
        miradaImages[2] = new MiradaCardActor(new TextureRegionDrawable(textureAtlas.findRegion(Assets.mirada3)), MIRADA_CARD_WIDTH, mHeight);

        mainTable.add(miradaImages[0]).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).expandY();//78
        mainTable.add(getCurrentCardStackGroup()).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).growY();//
        mainTable.add(miradaImages[2]).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0).expandY();//

        mainTable.add(cardStackGroup).width(STACK_CARD_GROUP_WIDTH).padLeft(10).padRight(10).padTop(MIRADA_CARD_PADDING / 2).growY().center();

        mainTable.add(playerListView.getMainTable()).height(SCREEN_HEIGHT / 2).top().right().padTop(MIRADA_CARD_PADDING / 2).growX();
        mainTable.row();

        mainTable.add(cardTable).colspan(5);

        background = new TextureRegionDrawable(assetManager.get(Assets.wood, Texture.class));
        mainTable.setBackground(background);
        //mainTable.debug();

        stage.addActor(mainTable);
        mainTable.layout();

        currentCardStackActor.setAbsolutePosition(
                currentCardStackActor.localToStageCoordinates(
                        new Vector2(currentCardStackActor.getWidth() / 2, currentCardStackActor.getHeight() / 2)));
        //activeCardImagePosition = activeCardImage.localToStageCoordinates(new Vector2(activeCardImage.getWidth() / 2, activeCardImage.getHeight() / 2));

        currentCardImage = new Image();

        platformFactory.getPlayers(group.getId(), this);

        configureDragAndDrop();

        /*showContextualButton(new TextButton("HELLO", skin), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.getTarget().remove();
            }
        });*/
    }

    protected void generateUIComponents(){
        super.generateUIComponents();
        setActivePlayerLabelStyle(ACTIVE_PLAYER_LABEL_FONT_SIZE);
        generatePlayerDetailListView();
        generateCardBoardTable();
        STACK_CARD_GROUP_WIDTH = generateCardStackGroup();
        generatePlayerModal();
    }

    private void generatePlayerModal() {
        modalDialog.add(playerListDetailView.getMainTable()).height(DIALOG_LIST_HEIGHT);
        modalDialog.row();
        modalDialog.add(playButton).size(WIDTH_UNIT, DIALOG_BUTTON_HEIGHT);
    }

    @Override
    protected void generatePlayerListView(){
        playerListAdapter = new PlayerVisAdapter(fontManager, players, Align.right);
        playerListView = new ListView<>(playerListAdapter);
        playerListView.getScrollPane().setFlickScroll(true);
        playerListView.getScrollPane().setScrollbarsOnTop(true);
        playerListView.getScrollPane().setVariableSizeKnobs(false);
        playerListView.getScrollPane().setScrollingDisabled(true, false);
    }

    protected void generatePlayerDetailListView(){
        playerListDetailAdapter = new PlayerVisAdapter(fontManager, playersMinusMe);
        playerListDetailView = new ListView<>(playerListDetailAdapter);
        playerListDetailView.setItemClickListener(new ListView.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                playerManager.selectPlayer(player.getId());
                playerListDetailAdapter.itemsDataChanged();
            }
        });
        playerListDetailView.getScrollPane().setFlickScroll(true);
        playerListDetailView.getScrollPane().setScrollbarsOnTop(true);
        playerListDetailView.getScrollPane().setVariableSizeKnobs(false);
        playerListDetailView.getScrollPane().setScrollingDisabled(true, false);
    }

    private VerticalGroup getCurrentCardStackGroup(){
        VerticalGroup vGroup = new VerticalGroup();
        vGroup.addActor(miradaImages[1]);
        vGroup.addActor(new Container<>(currentCardStackActor).padTop(HEIGHT_UNIT / 2));
        return vGroup;
    }

    @Override
    public void render(float delta) {
       super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
    }

    private void loadAssets(){
        assetManager.load(Assets.wood, Texture.class);
        assetManager.update();
        assetManager.finishLoading();
    }

    private void generateCardBoardTable(){
        cardTable = new Table();

        actionCardsBoard = new CardBoardActor(textureAtlas);
        excuseCardsBoard = new CardBoardActor(textureAtlas);

        cardTable.add(actionCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        cardTable.add(excuseCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        Drawable drawable = skin.getTiledDrawable("felt_tile");
        cardTable.setBackground(drawable);
    }

    /**
     * Generates the vertical group that contains the active player label and the
     * cardStack.
     * @return Max width of the label, used to set the width
     */
    private float generateCardStackGroup(){

        //Get width of label
        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(activePlayerLabel.getStyle().font, "Acknowledgement");

        //Loads the reverse card
        Drawable visReverseDrawable = changeDrawableSize(new TextureRegionDrawable(textureAtlas.findRegion(Assets.reverso)), STACK_CARD_WIDTH);
        VisImage visReverse = new VisImage(visReverseDrawable);
        visReverse.setScaling(Scaling.fit);

        cardStackGroup = new VerticalGroup();
        cardStackGroup.space(MIRADA_CARD_PADDING / 2);
        cardStackGroup.align(Align.top);
        cardStackGroup.addActor(activePlayerLabel);
        cardStackGroup.addActor(new Image(visReverseDrawable));
        return layout.width;
    }

    private void configureDragAndDrop(){
        dnd = new DragAndDrop();
        dnd.setDragActorPosition(actionCardsBoard.getCardWidth() / 2, -actionCardsBoard.getCardHeight() / 2);
        dnd.addSource(new DragAndDrop.Source(actionCardsBoard) {
            private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                CardActor selectedActor = (CardActor)event.getTarget();
                payload.setObject(selectedActor);
                payload.setDragActor(selectedActor);
                actionCardsBoard.removeCardActor(selectedActor);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if(target == null){
                    actionCardsBoard.addCardActor((CardActor) payload.getObject());
                }
            }
        });

        dnd.addSource(new DragAndDrop.Source(excuseCardsBoard) {
            private final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                CardActor selectedActor = (CardActor)event.getTarget();
                payload.setObject(selectedActor);
                payload.setDragActor(selectedActor);
                excuseCardsBoard.removeCardActor(selectedActor);
                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if(target == null){
                    excuseCardsBoard.addCardActor((CardActor) payload.getObject());
                }

            }
        });

        dnd.addTarget(new DragAndDrop.Target(cardStackGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Card secondCard = standbyCardActor == null ? null : standbyCardActor.getCard();
                return playerManager.canPlayerPlayCard(player.getId(), ((CardActor)(payload.getObject())).getCard(), secondCard);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor selectedCardActor = (CardActor) payload.getObject();

                //If the player is active
                if(playerManager.isPlayerActive(player.getId())){
                    if(selectedCardActor.getCard().getCardType() == CardType.ACTION) {
                        standbyCardActor = selectedCardActor;
                        actionCardsBoard.removeCardActor(selectedCardActor);
                        playerListDetailAdapter.itemsChanged();
                        modalDialog.show(stage);
                    } else {
                        excuseCardsBoard.removeCardActor(selectedCardActor);
                        playExcuseCard((ExcuseCard)selectedCardActor.getCard());
                    }
                } else {
                    if(standbyCardActor == null){
                        standbyCardActor = selectedCardActor;
                        //TODO: Show only cards that can be used; change border
                    } else {
                        if(standbyCardActor.getCard().getCardType() == CardType.EXCUSE){
                            platformFactory.sendInterrumpir(group.getId(),
                                    player.getId(),
                                    (ExcuseCard)standbyCardActor.getCard(),
                                    (ActionCard)selectedCardActor.getCard());
                        } else {
                            platformFactory.sendInterrumpir(group.getId(),
                                    player.getId(),
                                    (ExcuseCard)selectedCardActor.getCard(),
                                    (ActionCard)standbyCardActor.getCard());
                        }
                    }
                }
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

    private void playExcuseCard(ExcuseCard excuseCard){
        platformFactory.sendPlayExcuseCard(group.getId(), player.getId(), excuseCard);
    }

    @Override
    public void onActionReceived(Action action) {
        System.out.println(action);
        if (action.getAction() == ActionType.GET_CARD){
            doGetCardAction(action);
        } else if(action.getAction() == ActionType.PLAY_EXCUSE){
            doPlayExcuseAction(action);
        } else if(action.getAction() == ActionType.START){
            doStartAction(action);
        } else if(action.getAction() == ActionType.MIRADA){
            doMiradaAction(action);
        } else if(action.getAction() == ActionType.PLEAD){
            doPleadAction(action);
        } else if(action.getAction() == ActionType.PLEAD_ACCEPTED){
            doPleadAcceptedAction(action);
        } else if(action.getAction() == ActionType.PLEAD_NOT_ACCEPTED){
            doPleadNotAcceptedAction(action);
        } else if(action.getAction() == ActionType.PLAY_INTERRUMPIR){
            doPlayInterrumpirAction(action);
        } else if(action.getAction() == ActionType.PLAY_PASAR){
            doPlayPasarAction(action);
        } else if(action.getAction() == ActionType.GET_PLEAD_CARD){
            doGetPleadCardAction(action);
        } else if(action.getAction() == ActionType.GAME_OVER){
            doGameOverAction(action);
        } else if(action.getAction() == ActionType.GAME_NOT_OVER){
            doGameNotOverAction(action);
        }
    }

    private void doGameNotOverAction(Action action) {
        playerManager.resetPlayerTurn(player.getId());
        playerManager.setPleadingPlayer(action.getPlayer(), false);
    }

    private void doGameOverAction(Action action) {
        gameOver(playerManager.getPlayer(action.getPlayer()));
    }

    private void doGetPleadCardAction(Action action) {
        //TODO: SHOW CARD WITH ANIMATION

        if(action.getPlayer().equals(player.getId())){
            ActionCard actionCard = (ActionCard)action.getCard();
            if(actionCard.getSiSenorOscuro()){
                platformFactory.sendGameNotOverAction(group.getId(), player.getId());
            } else {
                platformFactory.sendGameOverAction(group.getId(), player.getId());
            }
        }
    }

    //passes turn to another player
    private void doPlayPasarAction(Action action) {
        //If I just played my turn, get excuse cards
        //THIS IS DONE ABOVE IN THE MODAL PLAY BUTTTON'S ONCLICK!!!!!!!
        /*if(player.getId().equals(playerManager.getActivePlayerId())){
            int numExcuseCards = playerManager.getNumberOfExcuseCards(player.getId());
            platformFactory.getCard(group.getId(), player.getId(), CardType.EXCUSE, 3 - numExcuseCards);
        }*/

        playerManager.playPasarElMarronCard(playerManager.getActivePlayerId(), (ActionCard) action.getCard());
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemsDataChanged();
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        showMessage("It's " + playerManager.getPlayerName(action.getPlayer()) + "'s turn");

        //If I'm receiving my turn
        if(player.getId().equals(action.getPlayer()) && playerManager.getNumberOfActionCards(player.getId()) < 3){
            platformFactory.getCard(group.getId(), player.getId(), CardType.ACTION);
        }
    }

    private void doPlayInterrumpirAction(Action action) {
        playerManager.playInterrumpirCard(action.getPlayer(),
                action.getCard().getCardType() == CardType.ACTION ?
                        (ActionCard)action.getCard() :
                        (ActionCard)action.getSecondCard());

        playerListAdapter.itemsDataChanged();

        showReceiveExcuseCardAnimation(action.getCard().getCardType() == CardType.EXCUSE ? action.getCard() : action.getSecondCard());
        showMessage(playerManager.getPlayerName(action.getPlayer()) + " has interrupted " + playerManager.getActivePlayerName());
    }

    private void doPleadNotAcceptedAction(Action action) {
        showMessage("PLEAD DENIED!");
        gameOver(playerManager.getPlayer(action.getPlayer()));
    }

    private void doPleadAcceptedAction(Action action) {
        showMessage("PLEAD ACCEPTED!");

        //TODO: Sow button to get the plead card
    }

    private void doPleadAction(Action action) {
        //DO SOMETHING???
    }

    private void doMiradaAction(Action action) {
        //Adds out card and sets cards to 0
        playerManager.giveOutCardToPlayer(action.getPlayer());

        int numOutCards = playerManager.getNumOutCards(action.getPlayer());

        if(numOutCards < 3){
            //Shows that the user is getting rid of all their out cards
            minNumPlays = 6;
            numPlays = 0;
            playerManager.getRidOfAllCards(action.getPlayer());
        } else {
            playerManager.setPleadingPlayer(action.getPlayer(), true);
        }

        playerListAdapter.itemsDataChanged();

        showReceiveMiradaCardAnimation(playerManager.getNumOutCards(action.getPlayer()));

        //If I'm the player receiving the out card
        if(action.getPlayer().equals(player.getId())){

            //Show the icon
            miradaImages[playerManager.getNumOutCards(player.getId()) - 1].showCard();

            if(numOutCards < 3){
                //Ask for all new cards
                actionCardsBoard.removeAllCards();
                excuseCardsBoard.removeAllCards();

                platformFactory.getAllNewCards(group.getId(), player.getId());
            } else {
                showContextualButton(new TextButton("Plead", skin), new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        platformFactory.sendPlead(group.getId(), player.getId());
                        event.getTarget().remove();
                    }
                });
            }
        }
    }

    private void doGetCardAction(Action action){
        playerManager.giveCardToPlayer(action.getCard(), action.getPlayer());
        if(action.getPlayer().equals(player.getId())){
            if(action.getCard().getCardType() == CardType.ACTION){
                actionCardsBoard.addCard(action.getCard());
            } else {
                excuseCardsBoard.addCard(action.getCard());
            }
        }
        numPlays++;
        if(numPlays >= minNumPlays){
            playerListAdapter.itemsChanged();
        }
    }

    private void doStartAction(Action action){
        showMessage(playerManager.getPlayerName(action.getPlayer()) + " starts!");
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemsDataChanged();
    }

    private void doPlayExcuseAction(Action action){
        playerManager.playExcuseCard(action.getPlayer());
        playerListAdapter.itemsDataChanged();
        showReceiveExcuseCardAnimation(action.getCard());
    }

    @Override
    public void onPlayersRetrieved(List<Player> players) {
        for(Player p : players){
            if(!p.getId().equals(group.getMasterId())){
                this.players.add(p);
                if(!p.getId().equals(player.getId())){
                    this.playersMinusMe.add(p);
                }
            }
        }

        playerListAdapter.itemsChanged();
        playerListDetailAdapter.itemsChanged();

        playerManager = new PlayerManager(players, group.getId(), group.getMasterId());
        platformFactory.listenForAction(group.getId(), this);
    }

    public void gameOver(Player loser){
        System.out.println(loser.getName() + " LOST!!");
        //TODO: DO GAME OVER ANIMATION and switch screen
    }

    private void showMessage(String message){
        final VisLabel label = new VisLabel(message, messageLabelStyle);
        label.setAlignment(Align.center);
        Container<VisLabel> labelContainer = new Container<>(label);
        labelContainer.setTransform(true);
        labelContainer.setOrigin(Align.center);
        labelContainer.debug();
        stage.addActor(labelContainer);
        labelContainer.addAction(Actions.sequence(
                Actions.moveTo((SCREEN_WIDTH / 2) - (labelContainer.getWidth() / 2), SCREEN_HEIGHT),
                Actions.scaleTo(0.5f, 0.5f),
                Actions.parallel(
                        Actions.moveBy(0, -HEIGHT_UNIT, 1),
                        Actions.scaleTo(1, 1, 1)),
                Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        label.remove();
                    }
                })
        ));
    }

    private void showContextualButton(final TextButton button, ChangeListener listener){
        transparentOverlay = skin.getTiledDrawable("transparent");
        button.setSize(WIDTH_UNIT * 1.5f, HEIGHT_UNIT);
        button.setOrigin(Align.center);
        button.addListener(listener);
        stage.addActor(button);
        button.addAction(Actions.moveTo(SCREEN_WIDTH / 2 - button.getWidth() / 2, SCREEN_HEIGHT / 2 - button.getHeight()));
    }

    public void showReceiveMiradaCardAnimation(int number){
        miradaCardImage = new Image(textureAtlas.findRegion("m" + number));
        miradaCardImage.setHeight(currentCardStackActor.getImageHeight());
        miradaCardImage.setScaling(Scaling.fit);
        miradaCardImage.setOrigin(Align.center);
        miradaCardImage.setPosition(SCREEN_WIDTH/2 - miradaCardImage.getWidth()/2, SCREEN_HEIGHT/2 - miradaCardImage.getHeight()/2);
        miradaCardImage.addAction(Actions.sequence(
                ActionGenerator.getShowCardAction(),
                Actions.delay(2),
                Actions.run(new Runnable() {
            @Override
            public void run() {
                miradaCardImage.remove();
            }
        })));
        stage.addActor(miradaCardImage);
    }
}
