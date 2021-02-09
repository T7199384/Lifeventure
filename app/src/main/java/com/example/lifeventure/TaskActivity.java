package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifeventure.Classes.AlertReceiver;
import com.example.lifeventure.Classes.Task;
import com.example.lifeventure.Classes.TaskAdapter;
import com.example.lifeventure.Dialogs.CreateTaskDialog;
import com.example.lifeventure.Dialogs.MapAddressDialog;
import com.example.lifeventure.Dialogs.ScheduleTaskDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity implements CreateTaskDialog.TaskCreateListener, ScheduleTaskDialog.TaskScheduleListener, MapAddressDialog.TaskMapListener {

    private ArrayList<Task> checkList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static final String GEOCACHE = null;

    private int lvl;
    private int exp;
    int currentLvl;

    public static final String PROFILE = null;
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final ImageButton profile= findViewById(R.id.profileButton);
        final ImageButton fight = findViewById(R.id.fightButton);
        final ImageButton settings = findViewById(R.id.settingsButton);

        loadProfile();

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

        Button mapTask = findViewById(R.id.addDestination);
        mapTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapTaskDialog();
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
                int addExp = (int) checkList.get(position).getTaskClassExp();
                checkList.remove(position);
                mAdapter.notifyItemRemoved(position);
                cancelAlarm();
                SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                exp=exp+addExp;
                editor.putInt(String.valueOf(PROFILE_EXP), (exp));
                Toast.makeText(TaskActivity.this, String.valueOf(exp), Toast.LENGTH_SHORT).show();
                lvl =(((exp)/1000)+1);
                editor.putInt(String.valueOf(PROFILE_LEVEL),lvl);
                if(lvl>currentLvl){
                    Toast.makeText(TaskActivity.this,"Level Up", Toast.LENGTH_SHORT).show();
                }
                currentLvl=lvl;
                editor.apply();
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

    public void MapTaskDialog() {
        MapAddressDialog mapTaskDialog = new MapAddressDialog();
        mapTaskDialog.show(getSupportFragmentManager(),"Map your journey");
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
    public void scheApply(String uName, String uDesc, int tDiffInt, String uStrDate, Calendar calendar) {
        Task task = new Task(uName, uDesc, tDiffInt, false, uStrDate);
        insert(task);
        startAlarm(calendar, uName, uStrDate);

    }

    @Override
    public void mapApply (String uName, String uAddress, int tDiffInt){
        Task task = new Task(uName, uAddress, tDiffInt, false);
        insert(task);

        SharedPreferences sharedPreferences = getSharedPreferences(GEOCACHE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(uName, uAddress);

        editor.apply();
    }

    private void startAlarm(Calendar calendar, String taskName, String taskDate) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlertReceiver alertReceiver = new AlertReceiver();
        alertReceiver.setTaskDate(taskDate);
        alertReceiver.setTaskName(taskName);
        Intent intent = new Intent(this, alertReceiver.getClass());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        am.cancel(pendingIntent);
    }

    public void loadProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(PROFILE, MODE_PRIVATE);
        lvl = sharedPreferences.getInt(String.valueOf(PROFILE_LEVEL),1);
        exp = sharedPreferences.getInt(String.valueOf(PROFILE_EXP), 0);
        currentLvl=lvl;
    }
}
