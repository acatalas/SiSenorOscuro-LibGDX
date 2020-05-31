package com.ale.sisenoroscuro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PlayerNameDialogFragment extends DialogFragment {
    public interface PlayerNameDialogListener{
        void onPlayerNameSelected(PlayerNameDialogFragment dialogFragment);
    }

    public enum DialogTitle{
        NO_NAME(R.string.no_name_dialog),
        DUPLICATE_NAME(R.string.duplicate_name_dialog);

        private final int dialogTitle;

        DialogTitle(int dialogTitle) {
            this.dialogTitle = dialogTitle;
        }

        private int getTitle(){
            return this.dialogTitle;
        }
    }

    private Button btnPlayerName;
    private EditText etPlayerName;
    private int title;

    PlayerNameDialogListener dialogListener;

    public PlayerNameDialogFragment(DialogTitle dialogTitle){
        this.title = dialogTitle.getTitle();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_player_name, null, false);
        builder.setView(v);
        ((TextView)v.findViewById(R.id.tv_name_question)).setText(title);
        btnPlayerName = v.findViewById(R.id.btn_player_name);
        etPlayerName = v.findViewById(R.id.et_player_name);
        btnPlayerName.setOnClickListener((view) -> {
            dialogListener.onPlayerNameSelected(PlayerNameDialogFragment.this);
            dismiss();
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (PlayerNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement NoticeDialogListener");
        }
    }

    public String getPlayerName(){
        return etPlayerName.getText().toString();
    }
}
