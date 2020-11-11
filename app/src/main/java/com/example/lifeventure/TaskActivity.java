package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final ImageButton profile= findViewById(R.id.profileButton);
        final ImageButton fight = findViewById(R.id.fightButton);
        final ImageButton settings = findViewById(R.id.settingsButton);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this, ProfileActivity.class));
            }
        });
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this, FightActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskActivity.this, SettingsActivity.class));
            }
        });


        Button createTask = findViewById(R.id.taskCreate);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTaskDialog();
            }
        });
    }

    public void CreateTaskDialog() {
        CreateTaskDialog createTaskDialog = new CreateTaskDialog();
        createTaskDialog.show(getSupportFragmentManager(),"Create Task");
    }
}
