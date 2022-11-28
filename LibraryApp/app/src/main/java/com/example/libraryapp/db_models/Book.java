package com.example.libraryapp.db_models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="book")
public class Book {

    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }
    public Book(){

    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;

    public int getId() {
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


}
