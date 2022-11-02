package com.example.todoapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks;

    public static TaskStorage getInstance()  {
        return taskStorage;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i = 1; i<=150; i++){
            Task task = new Task();
            task.setName("Pilne zadanie nr "+ i);
            task.setDone(i%3==0);
            if (i%3==0){
                task.setCategory(Category.COLLEGE);
            }
            else{
                task.setCategory(Category.HOME);
            }
            tasks.add(task);
        }
    }
    public Task getTask(UUID id){
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
    }
    public List<Task> getTasks(){
        return tasks;
    }
}
