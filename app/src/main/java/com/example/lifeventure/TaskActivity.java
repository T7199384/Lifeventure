package com.example.lifeventure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
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

    public static final String TASKS = "Tasks";
    public static final String GEOCACHE = "Geocache";

    private int lvl;
    private int exp;
    int currentLvl;

    public static final String PROFILE = "Profile";
    public static final int PROFILE_LEVEL = 1;
    public static final int PROFILE_EXP = 0;

    public static final String TOKEN = "Token";

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

        /*TODO
        Here is createList()
         */
        createList();
        buildRecycler();

        Intent intent = getIntent();
        if(intent.hasExtra("index")){
            int index = intent.getIntExtra("index",-1);
            if(index!=-1){taskComplete(index);}
        }

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
                String cardDesc = checkList.get(position).getTaskClassDesc();
                AlertDialog.Builder descDialog = new AlertDialog.Builder(TaskActivity.this);
                descDialog
                        .setTitle("Description")
                        .setMessage(cardDesc)
                        .setNegativeButton("Close",null)
                        .show();
            }

            @Override
            public void onCheck(int position) {
                taskComplete(position);

            }
        });

    }

    public void taskComplete(int position){
        int addExp = (int) checkList.get(position).getTaskClassExp();
        checkList.remove(position);

        mAdapter.notifyItemRemoved(position);

        SharedPreferences taskRefresh = getSharedPreferences(TASKS, MODE_PRIVATE);
        SharedPreferences.Editor taskRefreshEditor = taskRefresh.edit();
        for(int i = 0; i<taskRefresh.getInt("taskNum",0);i++){
            taskRefreshEditor.remove(String.valueOf(i));
        }

        int taskNum = 0;
        String taskStr="";
        for(int i = 0; i<checkList.size();i++){
            int diff= (int) (checkList.get(i).getTaskClassExp()/50);
            if(checkList.get(i).getTaskClassDate()==null) {
                taskStr = checkList.get(i).getTaskClassTitle() + "¬" + checkList.get(i).getTaskClassDesc() + "¬" + diff + "¬" + "NA";
                taskRefreshEditor.putString(String.valueOf(taskNum),taskStr);
            }
            else{
                taskStr = checkList.get(i).getTaskClassTitle() + "¬" + checkList.get(i).getTaskClassDesc() + "¬" + diff + "¬" + checkList.get(i).getTaskClassDate();
                taskRefreshEditor.putString(String.valueOf(taskNum),taskStr);
            }
            taskRefreshEditor.putString(String.valueOf(taskNum),taskStr);
            taskNum = taskNum+1;
        }
        taskRefreshEditor.putInt("taskNum", taskNum);
        taskRefreshEditor.commit();



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

        SharedPreferences tokens = getSharedPreferences(TOKEN,MODE_PRIVATE);
        int tokenCount = tokens.getInt("tokenCount",0);
        int fightTokens = tokens.getInt("fightTokens",0);
        tokenCount=tokenCount+1;
        if(tokenCount>=3){
            fightTokens = fightTokens+1;
            tokenCount=0;
            Toast.makeText(TaskActivity.this,"You obtain a fight token", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences.Editor tokenEditor = tokens.edit();
        tokenEditor.putInt("tokenCount", tokenCount);
        tokenEditor.putInt("fightTokens", fightTokens);
        tokenEditor.apply();
    }

    public void createList(){
        checkList=new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(TASKS, MODE_PRIVATE);
        int total = sharedPreferences.getInt("taskNum",0);
        for(int i = 0; i<total;i++){
            String taskStr = sharedPreferences.getString(String.valueOf(i),"");
            if(taskStr!="") {
                String[] split = taskStr.split("¬");
                try {
                    if (split[3].equals("NA")) {
                        checkList.add(new Task(split[0], split[1], Integer.parseInt(split[2]), false));
                    } else {
                        checkList.add(new Task(split[0], split[1], Integer.parseInt(split[2]), false, split[3]));
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                }
            }
            }
        }

    public void createTestList() {
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

        /* TODO
        *   Copy the MapApply SharedPrefs for tasks, use the same number as a primary key so that tasks are numbered and easier
        *   to find during deletion . . .
        *   Test task will mess with finding id as it won't be first, so either remove them or temporarily work around them*/

        SharedPreferences taskPref = getSharedPreferences(TASKS, MODE_PRIVATE);
        SharedPreferences.Editor taskEditor = taskPref.edit();

        int taskNum = taskPref.getInt("taskNum",0);
        String taskStr = uName+"¬"+uDesc+"¬"+tDiffInt+"¬"+"NA";
        taskEditor.putString(String.valueOf(taskNum),taskStr);
        taskNum = taskNum+1;
        taskEditor.putInt("taskNum", taskNum);
        taskEditor.apply();
    }

    @Override
    public void scheApply(String uName, String uDesc, int tDiffInt, String uStrDate, Calendar calendar) {
        Task task = new Task(uName, uDesc, tDiffInt, false, uStrDate);
        insert(task);
        startAlarm(calendar, uName, uStrDate);

        SharedPreferences taskPref = getSharedPreferences(TASKS, MODE_PRIVATE);
        SharedPreferences.Editor taskEditor = taskPref.edit();

        int taskNum = taskPref.getInt("taskNum",0);
        String taskStr = uName+"¬"+uDesc+"¬"+tDiffInt+"¬"+uStrDate;
        taskEditor.putString(String.valueOf(taskNum),taskStr);
        taskNum = taskNum+1;
        taskEditor.putInt("taskNum", taskNum);
        taskEditor.apply();
    }

    @Override
    public void mapApply (String uName, String uAddress, int tDiffInt){
        Task task = new Task(uName, uAddress, tDiffInt, false);
        insert(task);

        SharedPreferences sharedPreferences = getSharedPreferences(TASKS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int amountOfAddresses = sharedPreferences.getInt("amountOfAddresses",0);
        editor.putString(String.valueOf(amountOfAddresses), uAddress);
        amountOfAddresses=amountOfAddresses+1;
        editor.putInt("amountOfAddresses", amountOfAddresses);

        editor.apply();


        SharedPreferences taskPref = getSharedPreferences(TASKS, MODE_PRIVATE);
        SharedPreferences.Editor taskEditor = taskPref.edit();

        int taskNum = taskPref.getInt("taskNum",0);
        String taskStr = uName+"¬"+uAddress+"¬"+tDiffInt+"¬"+"NA";
        taskEditor.putString(String.valueOf(taskNum),taskStr);
        taskNum = taskNum+1;
        taskEditor.putInt("taskNum", taskNum);
        taskEditor.apply();

        String alertMessage="";
        for(int i = 0; i<sharedPreferences.getInt("amountOfAddresses",0);i++){
            alertMessage=alertMessage+"Location "+i+": "+sharedPreferences.getString(String.valueOf(i),"Not Found")+"\n";
        }

        new AlertDialog.Builder(this)
                .setTitle("Location Prefs")
                .setMessage(alertMessage)
                .setNegativeButton("Exit",null)
                .show();
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
