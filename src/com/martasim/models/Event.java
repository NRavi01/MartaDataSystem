package com.martasim.models;

import java.util.Objects;

public class Event {
    int id;
    int time;
    EventType type;

    public Event(int id, int time, EventType type) {
        this.id = id;
        this.time = time;
        this.type = type;
    }

    @Override
    public String toString() {
        return "(" +
                id +
                ", " + time +
                ", '" + type.name() + '\'' +
                ')';
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) { this.time = time; }

    public EventType getType() {
        return type;
    }

    public int getId() {
        return id;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return id == event.id &&
                time == event.time &&
                Objects.equals(type, event.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, type);
    }
}
