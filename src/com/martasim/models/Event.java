package com.martasim.models;

public class Event {
    int time;
    String type;
    int id;

    public Event(int time, String type, int id) {
        this.time = time;
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return '(' +
                time +
                ", '" + type + '\'' +
                ", " + id +
                ')';
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
