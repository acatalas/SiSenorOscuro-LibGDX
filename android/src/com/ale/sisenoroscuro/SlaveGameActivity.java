package com.ale.sisenoroscuro;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.classes.ActionDTO;
import com.ale.sisenoroscuro.classes.ActionCard;
import com.ale.sisenoroscuro.classes.ActionType;
import com.ale.sisenoroscuro.classes.Card;
import com.ale.sisenoroscuro.classes.CardSubType;
import com.ale.sisenoroscuro.classes.CardType;
import com.ale.sisenoroscuro.classes.ExcuseCard;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.groups.GroupPlayersAdapter;
import com.ale.sisenoroscuro.network.ActionsRepository;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SlaveGameActivity extends AppCompatActivity {
    private GroupRepository groupRepository;
    private ActionsRepository actionsRepository;

    private final int IV_ACTION_CARD_1 = R.id.iv_a1_card;
    private final int IV_ACTION_CARD_2 = R.id.iv_a2_card;
    private final int IV_ACTION_CARD_3 = R.id.iv_a3_card;

    private final int IV_EXCUSE_CARD_1 = R.id.iv_e1_card;
    private final int IV_EXCUSE_CARD_2 = R.id.iv_e2_card;
    private final int IV_EXCUSE_CARD_3 = R.id.iv_e3_card;

    private final int IV_CURRENT_CARD_1 = R.id.iv_current_card_1;
    private final int IV_CURRENT_CARD_2 = R.id.iv_current_card_2;

    private List<ActionDTO> actions;

    private Button btnPlay;
    private Button btnPass;

    private ImageView[] ivActionCards;
    private ImageView[] ivExcuseCards;
    private ImageView[] ivCurrentCards;

    private RecyclerView rvPlayerList;
    private GroupPlayersAdapter playersAdapter;

    private Card[] actionCards = new Card[3];
    private Card[] excuseCards = new Card[3];

    private List<Card> selectedCards;

    private GroupDTO group;
    private List<Player> players;
    private Player me;
    private PlayerManager playerManager;
    private byte numTurno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_table_slave);

        rvPlayerList = findViewById(R.id.rv_players);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("groupId") != null){
            group = new GroupDTO(bundle.getString("groupName"),
                    bundle.getInt("numPlayers"));
            group.setId(bundle.getString("groupId"));
            group.setMasterId(bundle.getString("masterId"));

            me = new Player(bundle.getString("playerId"), bundle.getString("playerName"));
        }

        ivActionCards = new ImageView[]{
                findViewById(IV_ACTION_CARD_1),
                findViewById(IV_ACTION_CARD_2),
                findViewById(IV_ACTION_CARD_3)};

        ivExcuseCards = new ImageView[]{
                findViewById(IV_EXCUSE_CARD_1),
                findViewById(IV_EXCUSE_CARD_2),
                findViewById(IV_EXCUSE_CARD_3)};

        ivCurrentCards = new ImageView[]{
                findViewById(IV_CURRENT_CARD_1),
                findViewById(IV_CURRENT_CARD_2)};

        getPlayersAndCards();

        selectedCards = new ArrayList<>();

    }

    public void setPlayerListView(){
        rvPlayerList.setLayoutManager(new LinearLayoutManager(this));
        playersAdapter = new GroupPlayersAdapter(players, (playerId) -> {
            Log.d("APP_LIST", playerId);
        });
        rvPlayerList.setAdapter(playersAdapter);
    }

    private Drawable getDrawableFromAssets(String name) throws IOException{
        InputStream ims = getAssets().open(name + ".png");
        Drawable d = Drawable.createFromStream(ims, null);
        ims.close();
        return d;
    }

    private void getPlayersAndCards(){
        groupRepository = new GroupRepository(FirebaseHelper.getInstance(this));
        actionsRepository = new ActionsRepository(FirebaseHelper.getInstance(this), group.getId());
        getPlayers();
        listenForAction();
    }

    private void listenForAction(){
        actions = new ArrayList<>();
        EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w("APP_ACTIONS", "Listen failed.", e);
                return;
            }

            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                if(dc.getType() == DocumentChange.Type.ADDED){
                    ActionDTO action = dc.getDocument().toObject(ActionDTO.class);
                    actions.add(action);
                    doAction(action);
                }
            }
        };
        actionsRepository.listenActions(eventListener);
    }

    private void doAction(ActionDTO action){
        if(action.getAction() == ActionType.GET_CARD){
            doGetCardAction(action);
        } else if(action.getAction() == ActionType.START) {
            doStartAction(action);
        }
    }

    private void doGetCardAction(ActionDTO action){
        playerManager.giveCardToPlayer(action.getCardDTO().getCard(), action.getPlayer());
        if(action.getPlayer().equals(me.getId())){
            setCard(action);
        }
        playersAdapter.notifyDataSetChanged();
    }

    private void doStartAction(ActionDTO action){
        playerManager.setActivePlayer(action.getPlayer());
        playersAdapter.notifyDataSetChanged();
        numTurno = 0;
    }

    private void getPlayers(){
        players = new ArrayList<>();
        groupRepository.getPlayersInGroupTask(group.getId())
                .addOnSuccessListener((queryDocumentSnapshots) -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    for(int i = 0; i < docs.size(); i++){
                        PlayerDTO playerDTO = docs.get(i).toObject(PlayerDTO.class);
                        Player player = new Player(playerDTO.getId(), playerDTO.getName());
                        players.add(player);
                    }
                    Log.d("APP_PLAYERS", players.toString());
                    playerManager = new PlayerManager(players, me.getId(), group.getMasterId());
                    setPlayerListView();
                });
    }

    private void setCard(ActionDTO action){
        if(action.getCardDTO().getCardType() == CardType.ACTION){
            setActionCard((ActionCard) action.getCardDTO().getCard());
        } else {
            setExcuseCard((ExcuseCard) action.getCardDTO().getCard());
        }
    }

    private void setActionCard(ActionCard actionCard){
        boolean placed = false;
        for(int i = 0; i < actionCards.length && !placed; i++){
            if(actionCards[i] == null){
                actionCards[i] = actionCard;
                setCardImage(ivActionCards[i], actionCard);
                setCardListener(ivActionCards[i], actionCard);
                placed = true;
            }
        }
    }

    private void setExcuseCard(ExcuseCard excuseCard){
        boolean placed = false;
        for(int i = 0; i < excuseCards.length && !placed; i++){
            if(excuseCards[i] == null){
                excuseCards[i] = excuseCard;
                setCardImage(ivExcuseCards[i], excuseCard);
                setCardListener(ivExcuseCards[i], excuseCard);
                placed = true;
            }
        }
    }

    private void setCardImage(ImageView image, Card card){
        try {
            image.setImageDrawable(getDrawableFromAssets(card.getFullName()));
        } catch (IOException e){
            Log.e("APP_CARDS", e.getMessage(), e);
        }
    }

    private void setCardListener(ImageView imageView, Card card) {
        imageView.setOnClickListener(new CardClickListener(card.getFullName()));
        imageView.setOnLongClickListener(new CardLongClickListener(card));
    }

    private boolean isCardSelected(Card card){
        for(Card selectedCard : selectedCards){
            if(selectedCard.equals(card)){
                return true;
            }
        }
        return false;
    }

    private class PlayClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(playerManager.amIPlaying()){
                Card card = selectedCards.get(0);
                if(card.getCardType() == CardType.EXCUSE){
                    //Excuse on myself
                    actionsRepository.sendPlayExcuseCard(me.getId(), (ExcuseCard)card);
                } else {
                    //Pasar el marron
                }
            } else {

            }
        }
    }

    private class PassClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }

    private class CardClickListener implements View.OnClickListener{

        final String imageName;

        public CardClickListener(String imageName){
            this.imageName = imageName;
        }

        @Override
        public void onClick(View v) {
            CardDialog cardDialog = new CardDialog(SlaveGameActivity.this, imageName);
            cardDialog.show();
        }
    }

    private class CardLongClickListener implements View.OnLongClickListener {

        private final Card card;

        public CardLongClickListener(Card card){
            this.card = card;
        }

        private void selectCard(View v, Card card){
            selectedCards.add(card);
            v.setBackgroundResource(R.drawable.card_border);
        }

        private boolean canPlayerPlayFirstCard(Card card){
            if(playerManager.amIPlaying()){
                return canActivePlayerPlayFirstCard(card);
            } else {
                return true;
            }
        }

        private boolean canActivePlayerPlayFirstCard(Card card){
            if(numTurno == 0 && card.getCardType() == CardType.EXCUSE){

                return true;
            //If turn is last must choose Action card
            } else if(numTurno > 2 && (card.getCardType() == CardType.ACTION) &&
                    ((ActionCard)card).getCardSubType() != CardSubType.MARRON){
                return true;
            } else if(numTurno > 0 && numTurno < 3){
                return true;
            }
            return false;
        }

        private boolean canPlayerPlaySecondCard(Card card1, Card card2){
            return (card1.getCardType() == CardType.EXCUSE && card2.getCardType() == CardType.ACTION &&
                    ((ActionCard)card2).getCardSubType() != CardSubType.MARRON) ||
                    (card1.getCardType() == CardType.ACTION && card2.getCardType() == CardType.EXCUSE &&
                            ((ActionCard)card1).getCardSubType() != CardSubType.MARRON);
        }

        @Override
        public boolean onLongClick(View v) {
            //If no cards are currently selected
            if(selectedCards.size() == 0){
                if(canPlayerPlayFirstCard(card)){
                    selectCard(v, card);
                    return true;
                } else {
                    return false;
                }

            //If 1 card is selected
            } else if(selectedCards.size() == 1){
                //If the selected card is already selected, remove it
                if(isCardSelected(card)){
                    selectedCards.remove(card);
                    v.setBackgroundResource(0);
                    return true;

                //If one card is already selected and is not repeated, and the player is not active, check
                //if they can play the card
                } else if(!playerManager.amIPlaying() && canPlayerPlaySecondCard(this.card, selectedCards.get(0))) {
                    selectCard(v, card);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
