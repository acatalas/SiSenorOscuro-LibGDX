package com.ale.sisenoroscuro.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.sisenoroscuro.R;
import com.ale.sisenoroscuro.classes.GroupDTO;
import com.ale.sisenoroscuro.network.FirebaseHelper;
import com.ale.sisenoroscuro.network.GroupRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class GroupListFragment extends Fragment  {
    private OnGroupSelectedListener onGroupSelectedListener;
    private RecyclerView recyclerView;
    private GroupAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.rv_group_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = new GroupRepository(FirebaseHelper.getInstance(getContext())).getGroupsQuery();

        FirestoreRecyclerOptions<GroupDTO> options = new FirestoreRecyclerOptions.Builder<GroupDTO>()
                .setQuery(query, GroupDTO.class)
                .build();

        adapter = new GroupAdapter(options, getContext());
        adapter.setOnItemClickListener((v) -> {
            String id = adapter.getSnapshots().getSnapshot(recyclerView.getChildAdapterPosition(v)).getId();
            onGroupSelectedListener.onGroupSelected(id);
            Log.d("APP", id);
        });

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setOnGroupSelectedListener(OnGroupSelectedListener onGroupSelectedListener){
        this.onGroupSelectedListener = onGroupSelectedListener;
    }

    public interface OnGroupSelectedListener {
        void onGroupSelected(String id);
    }
}
