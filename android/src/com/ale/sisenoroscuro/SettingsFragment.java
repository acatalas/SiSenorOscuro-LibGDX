package com.ale.sisenoroscuro;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private int numMaxPlayers;
    private int numMinPlayers;
    private int numMinLengthPlayerName;
    private int numMaxLengthPlayerName;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundResource(R.drawable.wood);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        numMinPlayers = getResources().getInteger(R.integer.min_players);
        numMaxPlayers = getResources().getInteger(R.integer.max_players);
        findPreference(PreferencesManager.getMaxPlayersPrefKey())
                .setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                    int value = Integer.parseInt((String)newValue);
                    if(value >= numMinPlayers && value <= numMaxPlayers){
                        return true;
                    }
                    Toast.makeText(getContext(), getString(R.string.incorrect_num_players_error, numMinPlayers, numMaxPlayers), Toast.LENGTH_SHORT).show();
                    return false;
        });
        numMinLengthPlayerName = getResources().getInteger(R.integer.min_player_name_chars);
        numMaxLengthPlayerName = getResources().getInteger(R.integer.max_player_name_chars);
        findPreference(PreferencesManager.getPlayerNameKey())
                .setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                    String playerName = (String)newValue;
                    if(playerName.length() >= numMinLengthPlayerName && playerName.length() <= numMaxLengthPlayerName){
                        return true;
                    }
                    Toast.makeText(getContext(), getString(R.string.incorrect_length_error, "Player name", numMinLengthPlayerName, numMaxLengthPlayerName), Toast.LENGTH_SHORT).show();
                    return false;
                });
    }

}
