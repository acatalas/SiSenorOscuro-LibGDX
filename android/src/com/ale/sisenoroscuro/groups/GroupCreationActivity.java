package com.ale.sisenoroscuro.groups;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ale.sisenoroscuro.PreferencesManager;
import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.Group;
import com.ale.sisenoroscuro.classes.Player;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;


public class GroupCreationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etGroupName;
    private EditText etMaxPlayers;
    private EditText etPlayerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_creation);
        etGroupName = findViewById(R.id.et_group_name);
        etMaxPlayers = findViewById(R.id.et_num_players);
        etPlayerName = findViewById(R.id.et_player_name);
        Button btnCreateGroup = findViewById(R.id.btn_create_group);

        btnCreateGroup.setOnClickListener(this);

        String playerName = PreferencesManager.getPlayerName(this);
        int maxPlayers = PreferencesManager.getMaxPlayers(this);

        if(playerName != null){
            etPlayerName.setText(playerName);
        }
        if(maxPlayers > 0){
            etMaxPlayers.setText(String.valueOf(maxPlayers));
        }
    }

    @Override
    public void onClick(View v) {
        if(formIsValid()){
            Player creator = new Player(etPlayerName.getText().toString());

            Group group = new Group(etGroupName.getText().toString(), //Group name
                    1, //Number of players will be 1
                    Integer.parseInt(etMaxPlayers.getText().toString())); //number of maxPlayers

            group.addPlayer(creator);

            group = new GroupRepository(FirebaseHelper.getInstance(this)).createGroup(group);

            Intent intent = new Intent(GroupCreationActivity.this, GroupMasterSelectionActivity.class);
            intent.putExtra("groupId", group.getId());
            intent.putExtra("groupName", group.getName());
            intent.putExtra("playerName", creator.getName());
            intent.putExtra("numPlayers", group.getMaxPlayers());
            intent.putExtra("playerId", group.getPlayers().get(0).getId());

            startActivity(intent);
        }
    }

    private boolean formIsValid(){
        byte numErrors = 0;
        String numPlayers = etMaxPlayers.getText().toString();
        if(!numPlayersIsValid(numPlayers)){
            numErrors++;
        }

        String groupName = etGroupName.getText().toString();
        if(!groupNameLengthIsValid(groupName)){
            numErrors++;
        }

        String playerName = etPlayerName.getText().toString();
        if(!playerNameLengthIsValid(playerName)){
            numErrors++;
        }

        Log.d("NUM_ERRORS", numErrors + " ERRORS");

        return numErrors == 0;
    }

    private boolean numPlayersIsValid(String maxPlayers){
        int maxAllowedPlayers = getResources().getInteger(R.integer.max_players);
        int minAllowedPlayers = getResources().getInteger(R.integer.min_players);
        try {
            int numPlayers = Integer.parseInt(maxPlayers);
            if(numPlayers < minAllowedPlayers || numPlayers > maxAllowedPlayers){
                Toast.makeText(this, getString(R.string.incorrect_num_players_error, minAllowedPlayers, maxAllowedPlayers), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e){
            Toast.makeText(this, getString(R.string.incorrect_num_players_error, minAllowedPlayers, maxAllowedPlayers), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean groupNameLengthIsValid(String groupName){
        int minAllowedGroupNameChars = getResources().getInteger(R.integer.min_group_name_chars);
        int maxAllowedGroupNameChars = getResources().getInteger(R.integer.max_group_name_chars);
        if(groupName.length() < minAllowedGroupNameChars || groupName.length() > maxAllowedGroupNameChars){
            Toast.makeText(this, getString(R.string.incorrect_length_error, "Group name", minAllowedGroupNameChars, maxAllowedGroupNameChars), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean playerNameLengthIsValid(String playerName){
        int minAllowedPlayerNameChars = getResources().getInteger(R.integer.min_player_name_chars);
        int maxAllowedPlayerNameChars = getResources().getInteger(R.integer.max_player_name_chars);

        if(playerName.length() < minAllowedPlayerNameChars || playerName.length() > maxAllowedPlayerNameChars){
            Toast.makeText(this, getString(R.string.incorrect_length_error, "Player name", minAllowedPlayerNameChars, maxAllowedPlayerNameChars), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
