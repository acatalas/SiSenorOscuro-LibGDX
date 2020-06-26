package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.actors.CardActor;
import com.ale.sisenoroscuro.actors.CardBoardActor;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
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
import com.ale.sisenoroscuro.recyclerView.ArrayListAdapter;
import com.ale.sisenoroscuro.recyclerView.ListAdapter;
import com.ale.sisenoroscuro.recyclerView.PlayerViewHolder;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import java.util.List;

public class SlaveGameScreen extends GameScreen implements Screen, ActionListener {
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128
    private static float HEIGHT_UNIT = SCREEN_HEIGHT / 5;
    private static float DIALOG_LIST_HEIGHT = HEIGHT_UNIT * 3;
    private static float DIALOG_BUTTON_HEIGHT = HEIGHT_UNIT * 1;
    /*private static float MIRADA_CARD_TOTAL_WIDTH = (WIDTH_UNIT * 2) / 4; //85
    private static float MIRADA_CARD_PADDING = MIRADA_CARD_TOTAL_WIDTH / 8; //28
    private static float MIRADA_CARD_WIDTH = MIRADA_CARD_TOTAL_WIDTH - MIRADA_CARD_PADDING; //85
    private static float STACK_CARD_WIDTH = SCREEN_WIDTH / 2 / 3;
    private static float STACK_CARD_GROUP_HEIGHT = SCREEN_HEIGHT / 2 / 4 * 3;*/

    private static float STACK_CARD_GROUP_HEIGHT = SCREEN_HEIGHT / 2 / 4 * 3;

    private float MIRADA_CARD_HEIGHT;
    private float STACK_CARD_GROUP_WIDTH;

    private float MIRADA_CARD_TOTAL_WIDTH, MIRADA_CARD_PADDING, MIRADA_CARD_WIDTH;

    private VerticalGroup cardStackGroup;
    private CardBoardActor actionCardsBoard, excuseCardsBoard;
    private Table cardTable;
    private ArrayListAdapter<Player> playerListDetailAdapter;
    private ModalDialog modalDialog;
    private TextButton playButton;

    private CardActor standbyCardActor;
    private MiradaCardActor[] miradaImages = new MiradaCardActor[3];

    private DragAndDrop dnd;

    public SlaveGameScreen(final PlatformFactory platformFactory, final Group group, Player me){
        super(platformFactory, group, me);
        this.currentCardImage = new Image();
    }

    @Override
    public void show() {
        super.show();

        generateUIComponents();

        mainTable.defaults().maxHeight(SCREEN_HEIGHT / 2);

        //Adds the MIRADA cards to the table
        mainTable.add(miradaImages[0]).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0);//78
        mainTable.add(getCurrentCardStackGroup()).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0);//
        mainTable.add(miradaImages[2]).top().width(MIRADA_CARD_WIDTH).pad(MIRADA_CARD_PADDING / 2).padBottom(0);//

        //Adds the center card stack
        //mainTable.add(cardStackGroup).center().width(STACK_CARD_GROUP_WIDTH).height(SCREEN_HEIGHT / 2).padLeft(10).padRight(10).padTop(MIRADA_CARD_PADDING / 2).growY();
        mainTable.add(cardStackGroup).center().width(STACK_CARD_GROUP_WIDTH).height(SCREEN_HEIGHT / 2).padTop(MIRADA_CARD_PADDING / 2).growY();

        //Adds the player list
        mainTable.add(playerListAdapter.getView()).top().right().padTop(MIRADA_CARD_PADDING / 2).growX();
        mainTable.row();

        //Adds the card board
        mainTable.add(cardTable).colspan(5);

        setBackground();

        stage.addActor(mainTable);
        mainTable.layout();

        setCardStackPosition();

        platformFactory.getPlayers(group.getId(), this);

        configureDragAndDrop();
    }

    protected void generateUIComponents(){
        super.generateUIComponents();
        setActivePlayerLabelFont(FontManager.BUTTON_FONT_SIZE);

        loadTextButtonStyle();
        generatePlayerDetailListView();
        generateCardBoardTable();
        generateCardStackGroup();
        generatePlayerModal();
        generateMiradaCardImages();
    }

    protected void loadTextButtonStyle() {
        //Modifies the default TextButton style's font
        textButtonStyle = new TextButton("", skin).getStyle();
        textButtonStyle.font = fontManager.getBlackCastleFont(FontManager.BUTTON_FONT_SIZE, false);
    }

    private void generatePlayerModal() {
        modalDialog = new ModalDialog(skin);

        playButton = new TextButton("Play", textButtonStyle);
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

        modalDialog.add(playerListDetailAdapter.getView()).height(DIALOG_LIST_HEIGHT);
        modalDialog.row();
        modalDialog.add(playButton).size(WIDTH_UNIT, DIALOG_BUTTON_HEIGHT);
        modalDialog.padTop(10).padBottom(10).padLeft(20).padRight(20);
    }

    //adds the player list. Override aligns the list to the right.
    @Override
    protected void generatePlayerListView(){
        playerListAdapter = new ArrayListAdapter<>(skin, players, new PlayerViewHolder(fontManager, skin), Align.right);
        playerListAdapter.setScrollPaneConfig(true, true, false, false, true);
    }

    protected void generatePlayerDetailListView(){
        playerListDetailAdapter = new ArrayListAdapter<>(skin, new PlayerViewHolder(fontManager, skin));
        playerListDetailAdapter.setScrollPaneConfig(true, true, false, false, true);
        playerListDetailAdapter.setItemClickListener(new ListAdapter.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                playerManager.selectPlayer(player.getId());
                playerListDetailAdapter.itemChanged(player);
            }
        });
    }

    private VerticalGroup getCurrentCardStackGroup(){
        VerticalGroup vGroup = new VerticalGroup();
        vGroup.addActor(miradaImages[1]);
        vGroup.addActor(new Container<>(currentCardStackActor).padTop(HEIGHT_UNIT * 0.35f));
        return vGroup;
    }

    private void generateMiradaCardImages(){
        Texture mirada1 = assetManager.get(Assets.Image.mirada1, Texture.class);
        MIRADA_CARD_HEIGHT = MIRADA_CARD_WIDTH * mirada1.getHeight() / mirada1.getWidth();

        miradaImages[0] = new MiradaCardActor(new TextureRegionDrawable(mirada1), MIRADA_CARD_WIDTH, MIRADA_CARD_HEIGHT);
        miradaImages[1] = new MiradaCardActor(new TextureRegionDrawable(assetManager.get(Assets.Image.mirada2, Texture.class)), MIRADA_CARD_WIDTH, MIRADA_CARD_HEIGHT);
        miradaImages[2] = new MiradaCardActor(new TextureRegionDrawable(assetManager.get(Assets.Image.mirada3, Texture.class)), MIRADA_CARD_WIDTH, MIRADA_CARD_HEIGHT);
    }

    private void generateCardBoardTable(){
        cardTable = new Table();

        actionCardsBoard = new CardBoardActor(assetManager);
        excuseCardsBoard = new CardBoardActor(assetManager);

        cardTable.add(actionCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        cardTable.add(excuseCardsBoard).size(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        Drawable drawable = skin.getTiledDrawable(Assets.Skin.Image.felt);
        cardTable.setBackground(drawable);
    }

    /**
     * Generates the vertical group that contains the active player label and the
     * cardStack.
     */
    private void generateCardStackGroup(){

        //Get width of label
        GlyphLayout layout = new GlyphLayout(); //don't do this every frame! Store it as member
        layout.setText(activePlayerLabel.getStyle().font, "Acknowledgement");
        STACK_CARD_GROUP_WIDTH = layout.width;
        MIRADA_CARD_TOTAL_WIDTH = (SCREEN_WIDTH - STACK_CARD_GROUP_WIDTH) / 2 / 3;
        MIRADA_CARD_PADDING = MIRADA_CARD_TOTAL_WIDTH / 8;
        MIRADA_CARD_WIDTH = MIRADA_CARD_TOTAL_WIDTH - MIRADA_CARD_PADDING;

        //Loads the reverse card
        Drawable reverseDrawable = changeDrawableSizeWithHeight(new TextureRegionDrawable(assetManager.get(Assets.Image.reverso, Texture.class)), STACK_CARD_GROUP_HEIGHT );
        Image reverse = new Image(reverseDrawable);
        reverse.setScaling(Scaling.fit);

        cardStackGroup = new VerticalGroup();
        cardStackGroup.space(MIRADA_CARD_PADDING / 2);
        cardStackGroup.align(Align.top);
        cardStackGroup.addActor(activePlayerLabel);
        cardStackGroup.addActor(new Image(reverseDrawable));
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
                        playerListDetailAdapter.itemChanged(player);
                        modalDialog.show(stage);
                    } else {
                        excuseCardsBoard.removeCardActor(selectedCardActor);
                        sendExcuseCard((ExcuseCard)selectedCardActor.getCard());
                    }
                } else {
                    if(standbyCardActor == null){
                        standbyCardActor = selectedCardActor;
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
                        standbyCardActor = null;
                    }
                }
            }
        });
    }

    private Drawable changeDrawableSizeWithWidth(Drawable drawable, float width){
        float minWidth = drawable.getMinWidth();
        drawable.setMinWidth(width);
        drawable.setMinHeight(drawable.getMinHeight() * width / minWidth);
        return drawable;
    }

    private Drawable changeDrawableSizeWithHeight(Drawable drawable, float height){
        float minHeight = drawable.getMinHeight();
        drawable.setMinHeight(height);
        drawable.setMinWidth(drawable.getMinWidth() * height / minHeight);
        return drawable;
    }

    //METHODS TO SEND ACTIONS

    private void sendExcuseCard(ExcuseCard excuseCard){
        platformFactory.sendPlayExcuseCard(group.getId(), player.getId(), excuseCard);
    }

    //METHODS TO RECEIVE ACTIONS

    @Override
    public void onActionReceived(Action action) {
        if (action.getAction() == ActionType.START){
            doStartAction(action);
        } else if(action.getAction() == ActionType.GET_CARD){
            doGetCardAction(action);
        } else if(action.getAction() == ActionType.PLAY_EXCUSE){
            doPlayExcuseAction(action);
        } else if(action.getAction() == ActionType.PLAY_PASAR){
            doPlayPasarAction(action);
        } else if(action.getAction() == ActionType.PLAY_INTERRUMPIR){
            doPlayInterrumpirAction(action);
        } else if(action.getAction() == ActionType.MIRADA){
            doMiradaAction(action);
        } else if(action.getAction() == ActionType.PLEAD){
            doPleadAction(action);
        } else if(action.getAction() == ActionType.PLEAD_NOT_ACCEPTED){
            doPleadNotAcceptedAction(action);
        } else if(action.getAction() == ActionType.PLEAD_ACCEPTED){
            doPleadAcceptedAction(action);
        } else if(action.getAction() == ActionType.GET_PLEAD_CARD){
            doGetPleadCardAction(action);
        } else if(action.getAction() == ActionType.GAME_NOT_OVER){
            doGameNotOverAction(action);
        } else if(action.getAction() == ActionType.GAME_OVER){
            doGameOverAction(action);
        }
    }

    private void doStartAction(Action action){
        showMessage("The master, " + playerManager.getMasterName() + ", wants explanations from " + playerManager.getPlayerName(action.getPlayer()) + "!");
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getActivePlayer());
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
        //when all cards are given out at the start of the round, update whole player list
        if(numPlays == (minNumPlays - 1)){
            for(Player p : playerManager.getPlayers()){
                playerListAdapter.itemChanged(p);
            }

        } else if(numPlays >= minNumPlays){ //all subsequent cards given out require individual updates
            playerListAdapter.itemChanged(playerManager.getPlayer(action.getPlayer()));
        }
        numPlays++;
    }

    private void doPlayExcuseAction(Action action){
        playerManager.playExcuseCard(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getPlayer(action.getPlayer()));
        showPlayExcuseCardAnimation(action.getCard());
    }

    //passes turn to another player
    private void doPlayPasarAction(Action action) {

        //
        minNumPlays = (byte)(3 - playerManager.getActivePlayer().getNumExcuseCards());
        numPlays = 0;

        playerManager.playPasarElMarronCard(playerManager.getActivePlayerId(), (ActionCard) action.getCard());
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getActivePlayer());
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        showMessage("It's " + playerManager.getPlayerName(action.getPlayer()) + "'s turn");

        //If I'm receiving my turn
        if(player.getId().equals(action.getPlayer()) &&
                playerManager.getNumberOfActionCards(player.getId()) < 3){
            platformFactory.getCard(group.getId(), player.getId(), CardType.ACTION);
        }

        //if was playing interrumpir card, bring back to hand
        /*if(standbyCardActor != null){
            if(standbyCardActor.getCard().getCardType() == CardType.EXCUSE){
                excuseCardsBoard.addCardActor(standbyCardActor);
            } else {
                actionCardsBoard.addCardActor(standbyCardActor);
            }
            standbyCardActor = null;
        }*/
    }

    private void doPlayInterrumpirAction(Action action) {
        playerManager.playInterrumpirCard(action.getPlayer(),
                action.getCard().getCardType() == CardType.ACTION ?
                        (ActionCard)action.getCard() :
                        (ActionCard)action.getSecondCard());

        playerListAdapter.itemChanged(playerManager.getPlayer(action.getPlayer()));
        showPlayExcuseCardAnimation(action.getCard().getCardType() == CardType.EXCUSE ? action.getCard() : action.getSecondCard());
        showMessage(playerManager.getPlayerName(action.getPlayer()) + " has interrupted " + playerManager.getActivePlayerName());
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

        playerListAdapter.itemChanged(playerManager.getActivePlayer());

        //If I'm the player receiving the out card
        if(action.getPlayer().equals(player.getId())){

            //Show the icon
            miradaImages[playerManager.getNumOutCards(player.getId()) - 1].showCard();

            if(numOutCards < 3){
                showPlayAndHideCardAnimation("m" + playerManager.getNumOutCards(action.getPlayer()));

                //Ask for all new cards
                actionCardsBoard.removeAllCards();
                excuseCardsBoard.removeAllCards();

                platformFactory.getAllNewCards(group.getId(), player.getId());

            } else {
                showPlayAndHideCardAnimation("m" + playerManager.getNumOutCards(action.getPlayer()),
                        new Runnable() {
                            @Override
                            public void run() {
                                showContextualButton(new TextButton("Plead", textButtonStyle), new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        platformFactory.sendPlead(group.getId(), player.getId());
                                        ((ModalDialog)event.getTarget().getParent()).hide();
                                    }}, 1.5f);
                            }
                        });
            }
        } else {
            showPlayAndHideCardAnimation("m" + playerManager.getNumOutCards(action.getPlayer()));
        }
    }

    private void doPleadAction(Action action) {
        showMessage(playerManager.getPlayerName(action.getPlayer()) + " is pleading");
    }

    private void doPleadNotAcceptedAction(Action action) {
        showGameOverMessage(playerManager.getPlayerName(action.getPlayer()) + "'s plead was denied!");
    }

    private void doPleadAcceptedAction(Action action) {

        if(player.getId().equals(action.getPlayer())){

            showMessage("PLEAD ACCEPTED!", new Runnable() {
                @Override
                public void run() {
                    showContextualButton(new TextButton("Get action card", textButtonStyle), new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            platformFactory.getPleadCard(group.getId(), player.getId());
                            ((ModalDialog)event.getTarget().getParent()).hide();
                        }
                    }, 2f);
                }
            });
        } else {
            showMessage("PLEAD ACCEPTED!");
        }
    }

    private void doGetPleadCardAction(Action action) {
        showPlayAndHideCardAnimation(action.getCard().getFullName());

        //If I'm receiving
        if(action.getPlayer().equals(player.getId())){
            ActionCard actionCard = (ActionCard)action.getCard();
            if(actionCard.getSiSenorOscuro()){
                platformFactory.sendGameNotOverAction(group.getId(), player.getId());
            } else {
                platformFactory.sendGameOverAction(group.getId(), player.getId());
            }
        }
    }

    private void doGameNotOverAction(Action action) {
        showMessage("Safe... for now");
        playerManager.resetPlayerTurn(player.getId());
        playerManager.removeOutCard(action.getPlayer());
        playerManager.setPleadingPlayer(action.getPlayer(), false);

        minNumPlays = 6;
        numPlays = 0;
        playerManager.getRidOfAllCards(action.getPlayer());

        playerListAdapter.itemChanged(playerManager.getActivePlayer());

        if(player.getId().equals(action.getPlayer())){
            miradaImages[2].hideCard();
            //Ask for all new cards
            actionCardsBoard.removeAllCards();
            excuseCardsBoard.removeAllCards();

            platformFactory.getAllNewCards(group.getId(), player.getId());
        }
    }

    private void doGameOverAction(Action action) {
        showGameOverMessage(playerManager.getPlayerName(action.getPlayer()) + " had no luck");
    }


    @Override
    public void onPlayersRetrieved(List<Player> players) {
        Player master = new Player();

        for(Player p : players){
            //CHECK THAT PLAYER IS NOT MASTER
            if(!p.getId().equals(group.getMasterId())){
                this.players.add(p);
                playerListAdapter.add(p);
                if(!p.getId().equals(player.getId())){
                    playerListDetailAdapter.add(p);
                }
            } else {
                master = new Player(p.getId(), p.getName());
            }
        }

        playerManager = new PlayerManager(this.players, master, player.getId());
        platformFactory.listenForAction(group.getId(), this);
    }

    //SHOW SPECIFIC UI COMPONENTS
    private void showContextualButton(final TextButton button, ChangeListener listener, float width){
        ModalDialog contextualModal = new ModalDialog(skin);
        contextualModal.add(button)
                .size(WIDTH_UNIT * width, BUTTON_HEIGHT)
                .padLeft(BUTTON_PADDING_LEFT).padRight(BUTTON_PADDING_RIGHT)
                .padTop(BUTTON_PADDING_TOP).padBottom(BUTTON_PADDING_BOTTOM);
        button.addListener(listener);
        contextualModal.show(stage);
    }
}
