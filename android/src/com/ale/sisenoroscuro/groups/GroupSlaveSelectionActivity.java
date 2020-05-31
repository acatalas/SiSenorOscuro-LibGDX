package com.ale.sisenoroscuro.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ale.sisenoroscuro.PlayerNameDialogFragment;
import com.ale.sisenoroscuro.PreferencesManager;
import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class GroupSlaveSelectionActivity extends GroupSelectionActivity
        implements GroupListFragment.OnGroupSelectedListener,
                    View.OnClickListener, PlayerNameDialogFragment.PlayerNameDialogListener {

    private static final String JOIN_TAG = "JOIN";
    private static final String LEAVE_TAG = "LEAVE";
    private GroupDetailFragment groupDetailFragment;
    private Button btnJoinGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_layout);

        btnJoinGroup = findViewById(R.id.btn_group_join);
        btnJoinGroup.setTag(JOIN_TAG);
        btnJoinGroup.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();

        groupDetailFragment = (GroupDetailFragment) fm.findFragmentById(R.id.group_list_detail);

        GroupListFragment groupListFragment = (GroupListFragment) fm.findFragmentById(R.id.group_list_fragment);

        groupListFragment.setOnGroupSelectedListener(this);

        me = new PlayerDTO(PreferencesManager.getPlayerName(this));
        group = new GroupDTO();

    }

    @Override
    public void onGroupSelected(String id) {
        if(btnJoinGroup.getTag().equals(JOIN_TAG)){
            group.setId(id);
            btnJoinGroup.setVisibility(View.VISIBLE);
            groupDetailFragment.setSelectedGroup(group.getId());
        }
    }

    /**
     * When "Join" button is clicked, send request to join group
     * Defines the callback
     * @param v View that executed the call
     */
    @Override
    public void onClick(View v) {
        if(btnJoinGroup.getTag().equals(JOIN_TAG)){
            Log.d("APP_JOIN_GROUP", group.getId() + " " + me.getName());
            if(me.getName() == null){
                promptUserName();
            } else if(isNameUsed(me.getName())) {
                promptChangeName();
            } else {
                joinGroup();
            }
        } else {
            leaveGroup();
        }
    }

    private void joinGroup(){
        GroupRepository.joinGroup(group.getId(), me.getName(), getJoinGroupCallback());
        listenCurrentGroup();
        btnJoinGroup.setTag(LEAVE_TAG);
        btnJoinGroup.setText(R.string.abandon_group);
    }

    private void leaveGroup(){
        GroupRepository.leaveGroup(group.getId(), me.getId());
        groupRegistration.remove();
        btnJoinGroup.setTag(JOIN_TAG);
        btnJoinGroup.setText(R.string.join_group);
    }

    private void promptUserName(){
        DialogFragment dialog = new PlayerNameDialogFragment(PlayerNameDialogFragment.DialogTitle.NO_NAME);
        dialog.show(getSupportFragmentManager(), "PlayerNameDialogFragment");
    }

    private void promptChangeName(){
        DialogFragment dialog = new PlayerNameDialogFragment(PlayerNameDialogFragment.DialogTitle.DUPLICATE_NAME);
        dialog.show(getSupportFragmentManager(), "PlayerNameDialogFragment");
    }

    public boolean isNameUsed(String name){
        return groupDetailFragment.getPlayerNames().contains(name);
    }

    /**
     * Gets callback object for the http join group request
     * @return callback for httpRequest
     */
    private Callback getJoinGroupCallback(){
        return new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                Log.e("APP_RESPONSE", e.toString(), e);
            }

            @Override
            public void onResponse(final Response response) throws IOException{
                me.setId(response.body().string());
            }
        };
    }

    @Override
    public void onPlayerNameSelected(PlayerNameDialogFragment dialogFragment) {
        Log.d("APP_PLAYER_NAME", dialogFragment.getPlayerName() + " isrepeat: " + isNameUsed(dialogFragment.getPlayerName()) );
        if(isNameUsed(dialogFragment.getPlayerName())){
            promptChangeName();
        } else {
            me.setName(dialogFragment.getPlayerName());
            joinGroup();
        }

    }
}

