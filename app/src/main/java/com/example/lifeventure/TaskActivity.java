package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lifeventure.Classes.Task;
import com.example.lifeventure.Classes.TaskAdapter;
import com.example.lifeventure.Dialogs.CreateTaskDialog;
import com.example.lifeventure.Dialogs.ScheduleTaskDialog;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class TaskActivity extends AppCompatActivity implements CreateTaskDialog.TaskCreateListener, ScheduleTaskDialog.TaskScheduleListener {

    private ArrayList<Task> checkList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        Button scheTask = findViewById(R.id.scheduleTask);
        scheTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheTaskDialog();
            }
        });

        createList();
        buildRecycler();

    }

    public void buildRecycler() {
        mRecyclerView=findViewById(R.id.checkList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mAdapter = new TaskAdapter(checkList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TextView hideTitle = findViewById(R.id.taskCardTitle);
                TextView hideDesc = findViewById(R.id.taskCardDesc);
                TextView hideExp = findViewById(R.id.taskCardExp);
                TextView hideDate = findViewById(R.id.taskCardSche);
                CheckBox hideCheck = findViewById(R.id.taskCardComplete);
                checkList.get(position).showDesc(hideTitle,hideDesc,hideExp,hideDate,hideCheck);
            }

            @Override
            public void onCheck(int position) {
                checkList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

    }

    public void createList() {
        checkList = new ArrayList<>();
        checkList.add(new Task("test title","test desc",0,false));
        checkList.add(new Task("test title","test desc",1,false));
        checkList.add(new Task("test title","test desc",2,false));
        checkList.add(new Task("test title","test desc",3,false));
        checkList.add(new Task("test title","test desc",4,false));
    }

    public void insert(Task taskIn){
        checkList.add(taskIn);
        mAdapter.notifyItemInserted(checkList.size());
    }

    public void CreateTaskDialog() {
        CreateTaskDialog createTaskDialog = new CreateTaskDialog();
        createTaskDialog.show(getSupportFragmentManager(),"Create Task");
    }

    public void ScheTaskDialog() {
        ScheduleTaskDialog scheTaskDialog = new ScheduleTaskDialog();
        scheTaskDialog.show(getSupportFragmentManager(),"Schedule Task");
    }

    @Override
    public void apply(String uName, String uDesc, int tDiffInt) {
        Task task = new Task(uName, uDesc, tDiffInt, false);
        insert(task);
        //Button createTask = findViewById(R.id.taskCreate);
        //createTask.setText(task.getTaskClassTitle());
;
    }

    @Override
    public void scheApply(String uName, String uDesc, int tDiffInt, String uStrDate) {
        Task task = new Task(uName, uDesc, tDiffInt, false, uStrDate);
        insert(task);
    }
}
