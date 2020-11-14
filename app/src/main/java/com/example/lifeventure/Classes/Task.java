package com.example.lifeventure.Classes;

import java.util.Date;

public class Task {
    String taskClassTitle;
    String taskClassDesc;
    double taskClassExp;
    Boolean taskClassComplete;

    Date taskClassDate;

    public Task(String taskClassTitle, String taskClassDesc, int taskClassDiff, Boolean taskClassComplete) {
        this.taskClassTitle = taskClassTitle;
        this.taskClassDesc = taskClassDesc;
        this.taskClassComplete = taskClassComplete;

        taskClassExp = (taskClassDiff+1)*0.25*100;
    }

    public Task(String taskClassTitle, String taskClassDesc, int taskClassDiff, Boolean taskClassComplete, Date taskClassDate) {
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

    public Date getTaskClassDate() {
        return taskClassDate;
    }

    public void setTaskClassDate(Date taskClassDate) {
        this.taskClassDate = taskClassDate;
    }
}
