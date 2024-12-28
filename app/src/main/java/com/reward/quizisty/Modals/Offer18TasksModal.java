package com.reward.quizisty.Modals;

public class Offer18TasksModal {

    String taskCount, taskName;
    boolean isTaskCompleted;

    public Offer18TasksModal(String taskCount, String taskName, boolean isTaskCompleted) {
        this.taskCount = taskCount;
        this.taskName = taskName;
        this.isTaskCompleted = isTaskCompleted;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }
}
