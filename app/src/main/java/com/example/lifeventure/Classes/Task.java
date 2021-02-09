package com.example.lifeventure.Classes;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lifeventure.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Task {
    String taskClassTitle;
    String taskClassDesc;
    double taskClassExp;
    Boolean taskClassComplete;

    String taskClassDate;

    public Task(String taskClassTitle, String taskClassDesc, int taskClassDiff, Boolean taskClassComplete) {
        this.taskClassTitle = taskClassTitle;
        this.taskClassDesc = taskClassDesc;
        this.taskClassComplete = taskClassComplete;

        taskClassExp = (taskClassDiff+1)*0.25*100;
    }

    public Task(String taskClassTitle, String taskClassDesc, int taskClassDiff, Boolean taskClassComplete, String taskClassDate) {
        this.taskClassTitle = taskClassTitle;
        this.taskClassDesc = taskClassDesc;
        this.taskClassComplete = taskClassComplete;
        this.taskClassDate = taskClassDate;

        taskClassExp = (taskClassDiff+1)*0.25*100;
    }

    public String getTaskClassTitle() {
        return taskClassTitle;
    }

    public void setTaskClassTitle(String taskClassTitle) {
        this.taskClassTitle = taskClassTitle;
    }

    public String getTaskClassDesc() {
        return taskClassDesc;
    }

    public void setTaskClassDesc(String taskClassDesc) {
        this.taskClassDesc = taskClassDesc;
    }

    public double getTaskClassExp() {
        return taskClassExp;
    }

    public void setTaskClassExp(double taskClassExp) {
        this.taskClassExp = taskClassExp;
    }

    public Boolean getTaskClassComplete() {
        return taskClassComplete;
    }

    public void setTaskClassComplete(Boolean taskClassComplete) {
        this.taskClassComplete = taskClassComplete;
    }

    public String getTaskClassDate() {
        return taskClassDate;
    }

    public void setTaskClassDate(String taskClassDate) {
        this.taskClassDate = taskClassDate;
    }

    public void showDesc(TextView hideTitle, TextView hideDesc, TextView hideExp,TextView hideDate,TextView hideCheck){
        if(hideTitle.getVisibility()== View.GONE){
            hideTitle.setVisibility(View.VISIBLE);
            hideExp.setVisibility(View.VISIBLE);
            hideDate.setVisibility(View.VISIBLE);
            hideCheck.setVisibility(View.VISIBLE);
        }
        else{
            hideTitle.setVisibility(View.GONE);
            hideExp.setVisibility(View.GONE);
            hideDate.setVisibility(View.GONE);
            hideCheck.setVisibility(View.GONE);
        }
    }
}
