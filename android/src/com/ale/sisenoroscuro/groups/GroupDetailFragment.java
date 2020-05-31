package com.ale.sisenoroscuro.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.PlayerDTO;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.List;



public class GroupDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private GroupPlayersFirestoreAdapter adapter;
    private int textAlignment;

    public GroupDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.rv_group_players);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void changeTextAlignment(int textAlignment){
        getView().findViewById(R.id.tv_players_title).setTextAlignment(textAlignment);
        this.textAlignment = textAlignment;
    }

    public void setSelectedGroup(String id){
        Query query = new GroupRepository(FirebaseHelper.getInstance(getContext()))
                .getPlayersInGroupQuery(id);

        FirestoreRecyclerOptions<PlayerDTO> options = new FirestoreRecyclerOptions.Builder<PlayerDTO>()
                .setQuery(query, PlayerDTO.class)
                .build();

        if(adapter == null){
            adapter = new GroupPlayersFirestoreAdapter(options, textAlignment);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } else {
            adapter.updateOptions(options);
        }
    }


    public void setSelectedGroup(String id, String groupName){
        ((TextView)getView().findViewById(R.id.tv_players_title)).setText(groupName);
        setSelectedGroup(id);
    }

    public List<String> getPlayerNames(){
        return adapter.getPlayerNames();
    }
}
