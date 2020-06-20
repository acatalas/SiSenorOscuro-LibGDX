package com.ale.sisenoroscuro.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.Player;

import java.util.List;

public class GroupPlayersAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    private final List<Player> players;
    private OnPlayerSelectedListener onPlayerSelectedListener;
    private String selectedPlayer;

    public GroupPlayersAdapter(List<Player> players){
        this.players = players;
        this.onPlayerSelectedListener = (playerId) -> {}; //do nothing when clicked
    }

    public GroupPlayersAdapter(List<Player> players, OnPlayerSelectedListener onPlayerSelectedListener){
        this(players);
        this.onPlayerSelectedListener = onPlayerSelectedListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_element, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Player currentPlayer = players.get(holder.getAdapterPosition());
        holder.tvPlayerName.setText(players.get(position).getName());

        //If the player is selected, change background
        if(selectedPlayer != null &&
                selectedPlayer.equals(currentPlayer.getId()) &&
                currentPlayer.isAvailable()){
            holder.itemView.setBackgroundColor(holder.tvPlayerName.getContext().getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.itemView.setBackgroundColor(holder.tvPlayerName.getContext().getResources().getColor(android.R.color.transparent));
        }

        if(players.get(position).isPlaying()){
            holder.tvPlayerName.setTextColor(holder.tvPlayerName.getContext().getResources().getColor(R.color.red));
        } else if(players.get(position).isAvailable()){
            holder.tvPlayerName.setTextColor(holder.tvPlayerName.getContext().getResources().getColor(android.R.color.white));
        } else {
            holder.tvPlayerName.setTextColor(holder.tvPlayerName.getContext().getResources().getColor(android.R.color.darker_gray));
        }

        //holder.tvNumActionCards.setText("A: " + currentPlayer.getNumActionCards());
        //holder.tvNumExcuseCards.setText("E: " + currentPlayer.getNumExcuseCards());

        holder.itemView.setOnClickListener((v) -> {
            selectedPlayer = players.get(holder.getAdapterPosition()).getId();
            onPlayerSelectedListener.onPlayerSelected(players.get(holder.getAdapterPosition()).getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public interface OnPlayerSelectedListener{
        void onPlayerSelected(String playerId);
    }


}