package com.ale.sisenoroscuro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.classes.GroupState;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.groups.GroupCreationActivity;
import com.ale.sisenoroscuro.groups.GroupSlaveSelectionActivity;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

public class MainActivity extends AppCompatActivity {
    private ListenerRegistration groupRegistration;
    private GroupDTO groupDTO;
    private int playerCounter = 2;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        findViewById(R.id.btn_create_group).setOnClickListener((view -> startGame()));
        findViewById(R.id.btn_join_group).setOnClickListener((view -> joinGroup()));
        findViewById(R.id.btn_settings).setOnClickListener((view -> showPreferences()));
        findViewById(R.id.btnShop).setOnClickListener(view -> showShop());
    }

    public void startGame(){
        Intent intent = new Intent(MainActivity.this, GroupCreationActivity.class);
        startActivity(intent);
    }

    public void joinGroup(){
        Intent intent = new Intent(MainActivity.this, GroupSlaveSelectionActivity.class);
        startActivity(intent);
    }

    public void showPreferences(){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showShop(){
        progressBar.setVisibility(View.VISIBLE);
        createTestGroup();
    }

    public void showTestGroup(){
        Intent intent = new Intent(MainActivity.this, AndroidLauncher.class);
        intent.putExtra("playerId", "l0GbIYh6w6TmvrNd5hIM");
        intent.putExtra("playerName", "Player1");
        intent.putExtra("groupId", "Ui5aUf1eA3GH4jRBIkNH");
        intent.putExtra("numPlayers", 4);
        intent.putExtra("groupName", "Test3");
        intent.putExtra("masterId", "FeML2r0tJMACuBdXrYag");
        startActivity(intent);
    }

    //create test group in database and play
    private void createTestGroup(){
        //prepare group and players
        Group group = new Group("Test", //Group name
                1, //Number of players will be 1
                4); //number of maxPlayers
        Player creator = new Player("Player1");
        group.addPlayer(creator);

        //create group with Google Function and get player id
        Group finalGroup = new GroupRepository(FirebaseHelper.getInstance(this)).createGroup(group);

        //create group state change listener
        EventListener<DocumentSnapshot> groupStateListener = (snapshot, e) -> {
            if (e != null) {
                Log.e("APP_LISTENER", "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                groupDTO = snapshot.toObject(GroupDTO.class);

                //add users succesively
                if(groupDTO.getState() == GroupState.WAITING && playerCounter <= groupDTO.getMaxPlayers()) {
                    GroupRepository.joinGroup(finalGroup.getId(), "Player" + playerCounter);
                    playerCounter++;
                }
                if(groupDTO.getState() == GroupState.COMPLETE && groupDTO.getMasterId() != null){
                    Log.d("APP_MASTER?", groupDTO.getMasterId() + " = " + creator.getId());

                    groupRegistration.remove();

                    //start game intent
                    Intent intent = new Intent(MainActivity.this, AndroidLauncher.class);
                    intent.putExtra("playerId", finalGroup.getPlayers().get(0).getId());
                    intent.putExtra("playerName", creator.getName());
                    intent.putExtra("groupId", finalGroup.getId());
                    intent.putExtra("numPlayers", finalGroup.getMaxPlayers());
                    intent.putExtra("groupName", finalGroup.getName());
                    intent.putExtra("masterId", groupDTO.getMasterId());

                    startActivity(intent);
                }
            }
        };

        //listen to group
        groupRegistration = new GroupRepository(FirebaseHelper.getInstance(this))
                .listenGroup(finalGroup.getId(), groupStateListener);
    }
}
