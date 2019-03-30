package com.hutechlab.model;

public class Task {
    private String taskName;
    private String taskInfo;
    private String taskEmp;
    private String taskDate;
    private String taskTime;
    private boolean isCheckedDelete;
    private boolean isCheckedComplete;
    private String taskID;
    private Boolean stamp = false;

    public Task(String taskName, String taskInfo, String taskEmp, String taskDate, String taskTime, boolean isCheckedDelete, boolean isCheckedComplete) {
        this.taskName = taskName;
        this.taskInfo = taskInfo;
        this.taskEmp = taskEmp;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.isCheckedDelete = isCheckedDelete;
        this.isCheckedComplete = isCheckedComplete;
    }

    public Task() {
    }

    public Boolean getStamp() {
        return stamp;
    }

    public void setStamp(Boolean stamp) {
        this.stamp = stamp;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getTaskEmp() {
        return taskEmp;
    }

    public void setTaskEmp(String taskEmp) {
        this.taskEmp = taskEmp;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public boolean isCheckedDelete() {
        return isCheckedDelete;
    }

    public void setCheckedDelete(boolean checkedDelete) {
        isCheckedDelete = checkedDelete;
    }

    public boolean isCheckedComplete() {
        return isCheckedComplete;
    }

    public void setCheckedComplete(boolean checkedComplete) {
        isCheckedComplete = checkedComplete;
    }
}
