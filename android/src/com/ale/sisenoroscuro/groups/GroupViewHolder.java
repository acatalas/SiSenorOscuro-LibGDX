package com.ale.sisenoroscuro.groups;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.R;


public class GroupViewHolder extends RecyclerView.ViewHolder{
    public final TextView tvPlayerName;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPlayerName = itemView.findViewById(R.id.tv_player_name);
    }
}