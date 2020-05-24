package com.martasim.models;

public class Stop {
    final int id;
    String name;
    int riders;
    double latitude;
    double longitude;

    public Stop(int id, String name, int riders, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.riders = riders;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "(" +
                id + ", " +
                '\'' + name + "', " +
                riders + ", " +
                latitude + ", " +
                longitude +
                ")";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRiders() {
        return riders;
    }

    public void setRiders(int riders) {
        this.riders = riders;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
