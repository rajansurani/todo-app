package com.example.todoapp.database.model;

import java.sql.Timestamp;

public class Todo {
    String taskName;
    String taskDetails;
    String taskId;
    String userID;
    String taskStatus;
    Timestamp taskCreatedDate;
    boolean taskSynced;

    public Todo() {
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isTaskSynced() {
        return taskSynced;
    }

    public void setTaskSynced(boolean taskSynced) {
        this.taskSynced = taskSynced;
    }
}
