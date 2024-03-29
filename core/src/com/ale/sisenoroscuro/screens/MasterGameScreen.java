package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.FontManager;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.actors.ModalDialog;
import com.ale.sisenoroscuro.classes.Action;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.recyclerView.ListAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

public class MasterGameScreen extends GameScreen implements Screen, ActionListener {
    private static final float ACTIVE_PLAYER_LABEL_FONT_SIZE = 50;
    private static final float HEIGHT_UNIT = SCREEN_HEIGHT / 7;
    private static final float TABLE_HEIGHT = HEIGHT_UNIT * 4;
    private static final float HEADER_HEIGHT = HEIGHT_UNIT * 1;
    private static final float FOOTER_HEIGHT = HEIGHT_UNIT * 2;
    private static final float WIDTH_UNIT = SCREEN_WIDTH / 5; //128

    private final ModalDialog modalDialog;
    private TextButton btnMiradaAsesina;
    private boolean firstTurn = true;

    public MasterGameScreen(PlatformFactory platformFactory, Group group, Player me){
        super(platformFactory, group, me);
        modalDialog = new ModalDialog(skin);
    }

    @Override
    public void show() {
        super.show();

        generateUIComponents();

        //selects player to start the game
        playerListAdapter.setItemClickListener(new ListAdapter.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                if(firstTurn){
                    activePlayerLabel.setText(playerManager.getPlayerName(player.getId()));
                    playerManager.setActivePlayer(player.getId());
                    playerListAdapter.itemChanged(player);
                    sendStartAction(player.getId());
                    firstTurn = false;
                }
            }
        });

        //HEADER
        mainTable.add();
        mainTable.add(activePlayerLabel).growX().height(HEADER_HEIGHT - 20).pad(10);
        mainTable.add()
                .row();

        //CARD STACK
        mainTable.add(new Container<>(currentCardStackActor)).width(WIDTH_UNIT);
        mainTable.add(playerListAdapter.getView()).size(WIDTH_UNIT * 3, TABLE_HEIGHT).left();
        mainTable.add().width(WIDTH_UNIT)
                .row();

        //FOOTER
        mainTable.add(btnMiradaAsesina).colspan(3).size(WIDTH_UNIT * 2, FOOTER_HEIGHT - 20).pad(10);

        setBackground();

        stage.addActor(mainTable);
        mainTable.layout();

        setCardStackPosition();

        platformFactory.getPlayers(group.getId(), this);
    }

    protected void generateUIComponents(){
        super.generateUIComponents();
        setActivePlayerLabelFont(ACTIVE_PLAYER_LABEL_FONT_SIZE);
        generateMiradaButton();
        generateAcceptOrDenyModal();
    }

    private void generateMiradaButton() {
        btnMiradaAsesina = new TextButton("Mirada asesina", skin);
        loadTextButtonStyle();
        btnMiradaAsesina.setStyle(textButtonStyle);

        btnMiradaAsesina.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(playerManager.getActivePlayer() != null){
                    sendMiradaAction();
                }
            }
        });
    }

    //Modifies the default button style
    @Override
    protected void loadTextButtonStyle() {
        textButtonStyle = btnMiradaAsesina.getStyle();
        textButtonStyle.font = fontManager.getBlackCastleFont(FontManager.BUTTON_FONT_SIZE, false);
    }

    private void generateAcceptOrDenyModal() {
        TextButton acceptButton = new TextButton("Accept", textButtonStyle);
        TextButton denyButton = new TextButton("Deny", textButtonStyle);

        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                sendPleadAcceptAction();
                modalDialog.hide();
            }
        });

        denyButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                sendPleadDenyAction();
                modalDialog.hide();
            }
        });

        modalDialog.add(denyButton).size(WIDTH_UNIT * 1.5f, BUTTON_HEIGHT).padRight(WIDTH_UNIT * 0.1f);
        modalDialog.add(acceptButton).size(WIDTH_UNIT * 1.5f, BUTTON_HEIGHT).padLeft(WIDTH_UNIT * 0.1f);
        modalDialog.padLeft(BUTTON_PADDING_LEFT).padRight(BUTTON_PADDING_RIGHT)
                .padTop(BUTTON_PADDING_TOP).padBottom(BUTTON_PADDING_BOTTOM);
    }

    //METHODS TO SEND ACTIONS

    private void sendStartAction(String selectedPlayerId){
        platformFactory.sendStartAction(group.getId(), selectedPlayerId);
    }

    private void sendMiradaAction() {
        platformFactory.sendMiradaAction(group.getId(), playerManager.getActivePlayerId());
    }

    private void sendPleadAcceptAction(){
        platformFactory.sendPleadAcceptedAction(group.getId(), playerManager.getActivePlayerId());
    }

    private void sendPleadDenyAction(){
        platformFactory.sendPleadDeniedAction(group.getId(), playerManager.getActivePlayerId());
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
        } else if(action.getAction() == ActionType.GET_PLEAD_CARD){
            doGetPleadCardAction(action);
        } else if(action.getAction() == ActionType.GAME_NOT_OVER){
            doGameNotOverAction(action);
        } else if(action.getAction() == ActionType.GAME_OVER){
            doGameOverAction(action);
        }
    }

    private void doStartAction(Action action) {
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getActivePlayer());
    }

    private void doGetCardAction(Action action){
        playerManager.giveCardToPlayer(action.getCard(), action.getPlayer());

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

    private void doPlayExcuseAction(Action action) {
        playerManager.playExcuseCard(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getPlayer(action.getPlayer()));
        showPlayExcuseCardAnimation(action.getCard());
    }

    private void doPlayPasarAction(Action action) {
        minNumPlays = (byte)(3 - playerManager.getActivePlayer().getNumExcuseCards());
        numPlays = 0;

        playerManager.playPasarElMarronCard(playerManager.getActivePlayerId(), (ActionCard) action.getCard());
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemChanged(playerManager.getActivePlayer());
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        showMessage("It's " + playerManager.getPlayerName(action.getPlayer()) + "'s turn");
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
    }

    private void doPleadAction(Action action) {
        showMessage("is pleading your forgiveness", new Runnable() {
            @Override
            public void run() {
                showAcceptAndDenyModal();
            }
        });
    }

    private void doPleadNotAcceptedAction(Action action) {
        showGameOverMessage(playerManager.getPlayerName(action.getPlayer()) + "'s plead was denied!");
    }

    private void doGetPleadCardAction(Action action) {
        showPlayAndHideCardAnimation(action.getCard().getFullName());
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
    }

    private void doGameOverAction(Action action) {
        showGameOverMessage(playerManager.getPlayerName(action.getPlayer()) + " had no luck");
    }

    @Override
    public void onPlayersRetrieved(List<Player> players) {

        //Add all players to the list except me
        for(Player p : players){
            if(!p.getId().equals(player.getId())){
                this.players.add(p);
                playerListAdapter.add(p);
            }
        }

        playerManager = new PlayerManager(this.players, new Player(player.getId(), player.getName()), player.getId());
        platformFactory.listenForAction(group.getId(), this);
    }

    //SHOW SPECIFIC UI COMPONENTS
    private void showAcceptAndDenyModal(){
        modalDialog.show(stage);
    }
}

