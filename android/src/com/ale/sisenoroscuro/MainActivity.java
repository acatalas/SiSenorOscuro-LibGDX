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
        intent.putExtra("playerId", "9HvZNqOCjhnH86oG7dcv");
        intent.putExtra("playerName", "Acknowledgement");
        intent.putExtra("groupId", "XbhBIbkFsPmB0qp4zjZQ");
        intent.putExtra("numPlayers", 4);
        intent.putExtra("groupName", "Aaaa");
        intent.putExtra("masterId", "i5ogRmhGHU0W4RVqfFQf");
        startActivity(intent);
    }

}
