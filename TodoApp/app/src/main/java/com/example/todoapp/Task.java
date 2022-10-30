package com.example.todoapp;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task(){
        id=UUID.randomUUID();
        date = new Date();
    }
    public void setName(String s){
        name = s;
    }
    public String getName(){ return  name ;}
    public void setDone(boolean value){done= value;}
    public boolean isDone(){
        return done;
    }
    public Date getDate(){
        return date;
    }
    public UUID getId(){ return id;}
}
