package com.ale.sisenoroscuro;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class PreferencesManager {
    private static final String PLAYER_NAME_KEY = "NAME";
    private static final String MAX_PLAYERS_PREF_KEY = "MAX_PLAYERS";

    public static String getPlayerName(Context context){
        return getString(context, PLAYER_NAME_KEY, null);
    }

    public static int getMaxPlayers(Context context){
        return Integer.parseInt(getString(context, MAX_PLAYERS_PREF_KEY, "0"));
    }

    public static String getPlayerNameKey(){
        return PLAYER_NAME_KEY;
    }

    public static String getMaxPlayersPrefKey(){
        return MAX_PLAYERS_PREF_KEY;
    }

    private static String getString(Context context, String key, String defaultValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }
}
