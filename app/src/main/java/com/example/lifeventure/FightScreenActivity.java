package com.example.lifeventure;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class FightScreenActivity extends AppCompatActivity {

    int monsterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fight_screen);

        monsterId=getIntent().getExtras().getInt("Value");

        TextView title = findViewById(R.id.monsterTitle);
        title.setText(String.valueOf(monsterId));

    }

}
