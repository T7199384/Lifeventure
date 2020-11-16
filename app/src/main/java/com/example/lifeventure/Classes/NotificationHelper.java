package com.example.lifeventure.Classes;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lifeventure.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelOneID = "channel1ID";
    public static final String channelOneName = "Channel 1";
    private Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private AudioAttributes audioAttributes;

    private String taskName;
    private String taskDate;

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {createChannels();}
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channelOne = new NotificationChannel(channelOneID,channelOneName, NotificationManager.IMPORTANCE_HIGH);
        channelOne.enableLights(true);
        channelOne.enableLights(true);
        channelOne.setLightColor(R.color.colorPrimary);
        channelOne.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channelOne.setSound(alarmSound,audioAttributes);

        getManager().createNotificationChannel(channelOne);
    }

    public NotificationManager getManager() {
        if (mManager==null){
            mManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelOneNotification(String taskName, String taskDate){
        return new NotificationCompat.Builder(getApplicationContext(),channelOneID)
                .setContentTitle("Your time is up!")
                .setContentText("One of your tasks should be completed by now, remember to check the task for completion!")
                .setSmallIcon(R.drawable.ic_crossed_swords);
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }
}
