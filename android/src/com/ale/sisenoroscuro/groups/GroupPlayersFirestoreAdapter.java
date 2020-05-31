package com.ale.sisenoroscuro.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupPlayersFirestoreAdapter extends FirestoreRecyclerAdapter<PlayerDTO, GroupViewHolder> {
    private int textAligment = View.TEXT_ALIGNMENT_TEXT_START;

    public GroupPlayersFirestoreAdapter(FirestoreRecyclerOptions<PlayerDTO> options) {
        super(options);
    }

    public GroupPlayersFirestoreAdapter(FirestoreRecyclerOptions<PlayerDTO> options, int textAlignment){
        super(options);
        this.textAligment = textAlignment;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_element, parent, false);
        v.findViewById(R.id.tv_player_name).setTextAlignment(textAligment);
        return new GroupViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int i, @NonNull PlayerDTO player) {
        holder.tvPlayerName.setText(player.getName());
    }

    public List<String> getPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        Iterator<PlayerDTO> iterator = getSnapshots().iterator();
        while (iterator.hasNext()){
            playerNames.add(iterator.next().getName());
        }
        return playerNames;
    }
}