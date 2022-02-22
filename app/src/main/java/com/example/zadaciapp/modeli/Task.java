package com.example.zadaciapp.modeli;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Task implements Serializable{

    private int id;
    private String name;
    private String description;
    private String date;
    private int done;
    private SimpleDateFormat dateTimeFromat = new SimpleDateFormat(
            "dd. MM. yyyy. HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd. MM. yyyy.");
    private SimpleDateFormat timeFromat = new SimpleDateFormat(
            "HH:mm");



    public Task(){

    }

    public Task(int id, String name, String description, String date, int done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDone() { return this.done; }

    public void setDone(int done){
        this.done = done;
    }

    public boolean isDone(){
        return done != 0;
    }

    public void setDone(boolean done){ this.done = (done ? 1 : 0); }

    public void toggleDone(){
        done = done==0 ? 1:0;
    }

    public void setDate (Calendar calendar){

        date =  dateTimeFromat.format(calendar.getTime());
    }

    public Calendar getCalendar (){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy. HH:mm");
        try{
            cal.setTime(sdf.parse(date));
        } catch (Exception e){
            Log.e("Parsiranje", "Greška pri parsiranju kalendara");
        }

        return cal;
    }

    public String getDateString(){
        return dateFormat.format(getCalendar().getTime());
    }

    public String getTimeString(){
        return timeFromat.format(getCalendar().getTime());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
