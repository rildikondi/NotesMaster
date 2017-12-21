package com.example.amarildo.notesmaster;


import com.orm.SugarRecord;

public class Note extends SugarRecord<Note> {



    private String message;


    private String currentDate;


    private String clickedDate;

    public Note(){

    }

    public Note(String message, String currentDate, String clickedDate) {

        //super();
        this.message = message;
        this.currentDate = currentDate;
        this.clickedDate = clickedDate;
    }



    public String getMessage() {
        return message;
    }

    public String getCurrentDate() {
        return currentDate;
    }


    public String getClickedDate() {
        return clickedDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setClickedDate(String clickedDate) {
        this.clickedDate = clickedDate;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.currentDate = date;
    }


}
