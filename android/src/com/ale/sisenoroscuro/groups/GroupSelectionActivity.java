package com.ale.sisenoroscuro.groups;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ale.sisenoroscuro.AndroidLauncher;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.classes.GroupState;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

public class GroupSelectionActivity extends AppCompatActivity {
    protected ListenerRegistration groupRegistration;
    protected GroupDTO group;
    protected PlayerDTO me;
    /**
     * Starts listening changes in the joined group status
     */
    protected void listenCurrentGroup(){
        EventListener<DocumentSnapshot> groupStateListener = (snapshot, e) -> {

            if (e != null) {
                Log.e("APP_LISTENER", "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                group = snapshot.toObject(GroupDTO.class);
                if(group.getState() == GroupState.COMPLETE && group.getMasterId() != null){
                    Log.d("APP_MASTER?", group.getMasterId() + " = " + me.getId());
                    startBoardActivity();
                }
            }
        };

        groupRegistration = new GroupRepository(FirebaseHelper.getInstance(this))
                .listenGroup(group.getId(), groupStateListener);
    }

    private void startBoardActivity(){
        groupRegistration.remove();

        Intent intent = new Intent(GroupSelectionActivity.this, AndroidLauncher.class);
        intent = addExtrasToIntent(intent);
        startActivity(intent);
    }

    private Intent addExtrasToIntent(Intent intent){
        intent.putExtra("playerId", me.getId());
        intent.putExtra("playerName", me.getName());
        intent.putExtra("groupId", group.getId());
        intent.putExtra("numPlayers", group.getNumPlayers());
        intent.putExtra("groupName", group.getName());
        intent.putExtra("masterId", group.getMasterId());
        return intent;
    }
}
