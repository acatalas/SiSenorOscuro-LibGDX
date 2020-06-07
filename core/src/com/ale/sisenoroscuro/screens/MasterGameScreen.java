package com.ale.sisenoroscuro.screens;

import com.ale.sisenoroscuro.ActionGenerator;
import com.ale.sisenoroscuro.ActionListener;
import com.ale.sisenoroscuro.Assets;
import com.ale.sisenoroscuro.ImageListener;
import com.ale.sisenoroscuro.PlatformFactory;
import com.ale.sisenoroscuro.PlayerManager;
import com.ale.sisenoroscuro.actors.CurrentCardStackActor;
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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.List;

public class MasterGameScreen extends GameScreen implements Screen, ActionListener {
    private static float ACTIVE_PLAYER_LABEL_FONT_SIZE = 50;
    private static float HEIGHT_UNIT = SCREEN_HEIGHT / 7;
    private static float TABLE_HEIGHT = HEIGHT_UNIT * 4;
    private static float HEADER_HEIGHT = HEIGHT_UNIT * 1;
    private static float FOOTER_HEIGHT = HEIGHT_UNIT * 2;
    private static float WIDTH_UNIT = SCREEN_WIDTH / 5; //128


    private TextButton btnMiradaAsesina;
    private boolean firstTurn = true;

    public MasterGameScreen(PlatformFactory platformFactory, Group group, Player me){
        super(platformFactory, group, me);

    }

    @Override
    public void show() {
        super.show();

        loadAssets();

        generateUIComponents();

        //Current card stack
        mainTable.add();
        mainTable.add(activePlayerLabel).growX().height(HEADER_HEIGHT - 20).pad(10);
        mainTable.add();
        mainTable.row();

        Container<CurrentCardStackActor> container = new Container<>(currentCardStackActor);

        playerListView.setItemClickListener(new ListView.ItemClickListener<Player>() {
            @Override
            public void clicked(Player player) {
                if(firstTurn){
                    activePlayerLabel.setText(playerManager.getPlayerName(player.getId()));
                    playerManager.setActivePlayer(player.getId());
                    playerListAdapter.itemsDataChanged();
                    sendStartAction(player.getId());
                    firstTurn = false;
                }
            }
        });

        mainTable.add(container).width(WIDTH_UNIT);
        mainTable.add(playerListView.getMainTable()).size(WIDTH_UNIT * 3, TABLE_HEIGHT).left();
        mainTable.add().width(WIDTH_UNIT);
        mainTable.row();

        btnMiradaAsesina = new TextButton("Mirada asesina", skin);
        TextButton.TextButtonStyle btnStyle = btnMiradaAsesina.getStyle();
        btnStyle.font = fontManager.getBlackCastleFont(25, false);
        btnMiradaAsesina.setStyle(btnStyle);

        btnMiradaAsesina.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendMiradaAction();
            }
        });

        mainTable.add(btnMiradaAsesina).colspan(3).size(WIDTH_UNIT * 2, FOOTER_HEIGHT - 20).pad(10);

        background = new TextureRegionDrawable(assetManager.get(Assets.wood, Texture.class));
        mainTable.setBackground(background);
        stage.addActor(mainTable);
        mainTable.debug();
        mainTable.layout();

        currentCardStackActor.setAbsolutePosition(
                currentCardStackActor.localToStageCoordinates(
                        new Vector2(currentCardStackActor.getWidth() / 2, currentCardStackActor.getHeight() / 2)));

        platformFactory.getPlayers(group.getId(), this);
    }

    private void sendMiradaAction() {
        platformFactory.sendMiradaAction(group.getId(), playerManager.getActivePlayerId());
    }

    protected void generateUIComponents(){
        super.generateUIComponents();
        setActivePlayerLabelStyle(ACTIVE_PLAYER_LABEL_FONT_SIZE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
            doGetCardAction(action);
        }  else if(action.getAction() == ActionType.PLAY_EXCUSE){
            doPlayExcuseAction(action);
        } else if(action.getAction() == ActionType.START){
            doStartAction(action);
        } else if(action.getAction() == ActionType.MIRADA){
            doMiradaAction(action);
        } else if(action.getAction() == ActionType.PLEAD){
            doPleadAction(action);
        } else if(action.getAction() == ActionType.PLEAD_NOT_ACCEPTED){
            doPleadNotAcceptedAction(action);
        }else if(action.getAction() == ActionType.PLAY_INTERRUMPIR){
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

    }

    private void doGameOverAction(Action action) {
        gameOver(playerManager.getPlayer(action.getPlayer()));
    }

    private void doGetPleadCardAction(Action action) {
        //TODO: SHOW CARD WITH ANIMATION
    }

    private void doPlayPasarAction(Action action) {
        playerManager.playPasarElMarronCard(playerManager.getActivePlayerId(), (ActionCard) action.getCard());
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemsDataChanged();
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        showMessage("It's " + playerManager.getPlayerName(action.getPlayer()) + "'s turn");
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
        gameOver(playerManager.getPlayer(action.getPlayer()));
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

        playerListAdapter.itemsDataChanged();
    }

    private void doPleadAction(Action action) {
        showMessage(playerManager.getPlayerName(action.getPlayer()) + " is pleading your forgiveness");
        //TODO: Show accept and deny buttons
    }

    private void doStartAction(Action action) {
        activePlayerLabel.setText(playerManager.getPlayerName(action.getPlayer()));
        playerManager.setActivePlayer(action.getPlayer());
        playerListAdapter.itemsDataChanged();
    }

    private void doPlayExcuseAction(Action action) {
        playerManager.playExcuseCard(action.getPlayer());
        playerListAdapter.itemsDataChanged();
        showReceiveExcuseCardAnimation(action.getCard());
    }

    private void doGetCardAction(Action action){
        playerManager.giveCardToPlayer(action.getCard(), action.getPlayer());
        numPlays++;
        if(numPlays >= minNumPlays){
            playerListAdapter.itemsChanged();
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
}

