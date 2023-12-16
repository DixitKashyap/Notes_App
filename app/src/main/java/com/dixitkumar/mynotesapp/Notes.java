package com.dixitkumar.mynotesapp;

public class Notes {

    private int id;


    private String notes_title;

    private String notes_content;

    private String notes_time;

    public Notes(int id, String notes_title, String notes_content, String notes_time) {
        this.id = id;
        this.notes_title = notes_title;
        this.notes_content = notes_content;
        this.notes_time = notes_time;
    }


    public Notes(String notes_title,String notes_content,String notes_time){
        this.notes_title = notes_title;
        this.notes_content = notes_content;
        this.notes_time = notes_time;
    }
    public Notes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes_title() {
        return notes_title;
    }

    public void setNotes_title(String notes_title) {
        this.notes_title = notes_title;
    }

    public String getNotes_content() {
        return notes_content;
    }

    public void setNotes_content(String notes_content) {
        this.notes_content = notes_content;
    }

    public String getNotes_time() {
        return notes_time;
    }

    public void setNotes_time(String notes_time) {
        this.notes_time = notes_time;
    }
}
