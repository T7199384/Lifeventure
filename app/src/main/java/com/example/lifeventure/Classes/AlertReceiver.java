package com.example.lifeventure.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    private String taskName;
    private String taskDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.setTaskDate(taskDate);
        notificationHelper.setTaskName(taskName);
        NotificationCompat.Builder nb = notificationHelper.getChannelOneNotification(taskName,taskDate);
        notificationHelper.getManager().notify(1, nb.build());

    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
}
