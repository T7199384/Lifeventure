package com.example.lifeventure.Classes;

public class Task {
    String taskClassTitle;
    String taskClassDesc;
    double taskClassExp;
    Boolean taskClassComplete;

    public Task(String taskClassTitle, String taskClassDesc, int taskClassDiff, Boolean taskClassComplete) {
        this.taskClassTitle = taskClassTitle;
        this.taskClassDesc = taskClassDesc;
        this.taskClassComplete = taskClassComplete;

        taskClassExp = (taskClassDiff+1)*0.25*100;
    }



}
