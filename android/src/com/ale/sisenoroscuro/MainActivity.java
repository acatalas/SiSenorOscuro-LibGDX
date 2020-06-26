package com.ale.sisenoroscuro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.ale.sisenoroscuro.groups.GroupCreationActivity;
import com.ale.sisenoroscuro.groups.GroupSlaveSelectionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_create_group).setOnClickListener((view -> startGame()));
        findViewById(R.id.btn_join_group).setOnClickListener((view -> joinGroup()));
        findViewById(R.id.btn_settings).setOnClickListener((view -> showPreferences()));
        findViewById(R.id.btnShop).setOnClickListener(view -> showShop());
    }

    public void startGame(){
        Intent intent = new Intent(MainActivity.this, GroupCreationActivity.class);
        startActivity(intent);
    }

    public void joinGroup(){
        Intent intent = new Intent(MainActivity.this, GroupSlaveSelectionActivity.class);
        startActivity(intent);
    }

    public void showPreferences(){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void showShop(){
        Intent intent = new Intent(MainActivity.this, AndroidLauncher.class);
        intent.putExtra("playerId", "l0GbIYh6w6TmvrNd5hIM");
        intent.putExtra("playerName", "ale");
        intent.putExtra("groupId", "Ui5aUf1eA3GH4jRBIkNH");
        intent.putExtra("numPlayers", 4);
        intent.putExtra("groupName", "Ale");
        intent.putExtra("masterId", "FeML2r0tJMACuBdXrYag");
        startActivity(intent);
    }

}
