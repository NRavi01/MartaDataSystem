package com.martasim.models;

import java.util.Objects;

public class Event implements Cloneable {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
