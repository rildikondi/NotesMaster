package com.example.amarildo.notesmaster.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;


@Table(name = "event_table")
public class EventObjects extends Model {

    @Column(name = "id")
    private int id;

    @Column(name = "message")
    private String message;

    @Column(name = "date")
    private Date date;

    public EventObjects(String message, Date date) {

        this.message = message;
        this.date = date;
    }

    public EventObjects(int id, String message, Date date) {

        this.date = date;
        this.message = message;
        this.id = id;
    }

    public int getid() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}