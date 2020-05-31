package com.ale.sisenoroscuro.groups;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.ale.sisenoroscuro.MainActivity;
import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.GroupRepository;

public class GroupMasterSelectionActivity extends GroupSelectionActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_master_layout);

        FragmentManager fm = getSupportFragmentManager();

        GroupDetailFragment groupDetailFragment = (GroupDetailFragment) fm.findFragmentById(R.id.group_list_detail);
        groupDetailFragment.changeTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("groupId") != null){
            group = new GroupDTO(bundle.getString("groupName"),
                    bundle.getInt("numPlayers"));

            group.setId(bundle.getString("groupId"));

            me = new PlayerDTO(bundle.getString("playerName"));
            me.setId(bundle.getString("playerId"));
        }

        Log.d("APP_MASTER", group.toString() + " " + me.toString());

        groupDetailFragment.setSelectedGroup(group.getId(), group.getName());
        listenCurrentGroup();

        Button bAbandonGroup = findViewById(R.id.btn_leave_group);
        bAbandonGroup.setOnClickListener((View view) -> leaveGroup());
    }

    private void leaveGroup(){
        Log.d("APP_GROUP_MASTER", group.getId() + " : " + me.getId());
        GroupRepository.leaveGroup(group.getId(), me.getId());
        Intent intent = new Intent(GroupMasterSelectionActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {}
}
