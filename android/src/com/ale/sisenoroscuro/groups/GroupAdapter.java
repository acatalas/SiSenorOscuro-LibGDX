package com.ale.sisenoroscuro.groups;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class GroupAdapter extends FirestoreRecyclerAdapter<GroupDTO, GroupAdapter.ViewHolder> {
    private View.OnClickListener onClickListener;
    private final Resources resources;
    private int selectedPos = RecyclerView.NO_POSITION;

    public GroupAdapter(FirestoreRecyclerOptions<GroupDTO> options, Context context) {
        super(options);
        resources = context.getResources();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvGroupName;
        public final TextView tvPlayers;
        public final ImageView isSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvGroupName = itemView.findViewById(R.id.group_name);
            tvPlayers = itemView.findViewById(R.id.group_players);
            isSelected = itemView.findViewById(R.id.iv_selected);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view);
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_element, parent, false);
        return new ViewHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int i, @NonNull GroupDTO group) {
        holder.tvPlayers.setText(resources.getString(R.string.num_players_max_players, group.getNumPlayers(), group.getMaxPlayers()));
        holder.tvGroupName.setText(group.getName());
        if(selectedPos == i){
            holder.isSelected.setImageResource(R.drawable.selected_arrow);
        }
    }
}
