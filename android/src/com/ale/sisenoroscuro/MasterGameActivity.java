package com.ale.sisenoroscuro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.classes.ActionDTO;
import com.ale.sisenoroscuro.classes.ActionType;
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

import java.util.ArrayList;
import java.util.List;

public class MasterGameActivity extends AppCompatActivity implements GroupPlayersAdapter.OnPlayerSelectedListener {
    private RecyclerView rvPlayers;
    private GroupPlayersAdapter playersAdapter;

    private ActionsRepository actionsRepository;

    private List<Player> players;
    private List<ActionDTO> actions;
    private PlayerManager playerManager;

    private GroupDTO group;
    private PlayerDTO me;
    private String selectedPlayer;
    private boolean isFirstMove = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_table_master);

        rvPlayers = findViewById(R.id.rv_players);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("groupId") != null) {
            group = new GroupDTO(bundle.getString("groupName"),
                    bundle.getInt("numPlayers"));

            group.setId(bundle.getString("groupId"));

            me = new PlayerDTO(bundle.getString("playerName"));
            me.setId(bundle.getString("playerId"));
        }

        getPlayers();
        listenForAction();

        findViewById(R.id.btn_mirada_asesina).setOnClickListener(v -> sendMiradaAsesina());
    }

    private void listenForAction(){
        actions = new ArrayList<>();
        actionsRepository = new ActionsRepository(FirebaseHelper.getInstance(this), group.getId());

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
        playersAdapter.notifyDataSetChanged();
    }

    private void doStartAction(ActionDTO action){
        playerManager.setActivePlayer(action.getPlayer());
        playersAdapter.notifyDataSetChanged();
    }

    private void sendMiradaAsesina() {
        if(selectedPlayer == null){
            Toast.makeText(this, "NO PLAYER SELECTED", Toast.LENGTH_SHORT).show();
        } else if(!playerManager.isPlayerActive(selectedPlayer)){
            Toast.makeText(this, "PLAYER IS NOT ACTIVE", Toast.LENGTH_SHORT).show();
        } else {
            actionsRepository.sendMiradaAsesina(selectedPlayer);
        }
    }

    private void getPlayers() {
        players = new ArrayList<>();
        GroupRepository groupRepository = new GroupRepository(FirebaseHelper.getInstance(this));
        groupRepository.getPlayersInGroupTask(group.getId())
                .addOnSuccessListener((queryDocumentSnapshots) -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    for (int i = 0; i < docs.size(); i++) {
                        PlayerDTO playerDTO = docs.get(i).toObject(PlayerDTO.class);
                        Player player = new Player(playerDTO.getId(), playerDTO.getName());
                        if(!player.getId().equals(me.getId())){
                            player.setIsAvailable(true);
                        }
                        players.add(player);
                    }
                    Log.d("APP_PLAYERS", players.toString());
                    playerManager = new PlayerManager(players, me.getId(), group.getMasterId());
                    setPlayerListView();
                });
    }

    private void setPlayerListView() {
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        playersAdapter = new GroupPlayersAdapter(players, (playerId) -> {
            if (isFirstMove) {
                actionsRepository.sendStartAction(playerId);
                isFirstMove = false;
            }
        });
        rvPlayers.setAdapter(playersAdapter);
    }

    @Override
    public void onPlayerSelected(String playerId) {
        this.selectedPlayer = playerId;
    }
}