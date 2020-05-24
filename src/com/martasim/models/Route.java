package com.martasim.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Route {
    final int id;
    int number;
    String name;
    List<Stop> stops;

    public Route(int id, int number, String name) {
        this(id, number, name, new ArrayList<>());
    }

    public Route(int id, int number, String name, List<Stop> stops) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "(" +
                id + ", " +
                number + ", " +
                '\'' + name + '\'' +
                ")";
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void extend(Stop stop) {
        this.stops.add(stop);
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return id == route.id &&
                number == route.number &&
                Objects.equals(name, route.name) &&
                Objects.equals(stops, route.stops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, name, stops);
    }
}
