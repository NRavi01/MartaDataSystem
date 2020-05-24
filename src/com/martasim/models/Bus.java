package com.martasim.models;

import java.util.Objects;

public class Bus {
    final int id;
    Route route;
    int location;
    int passengers;
    int passengerCapacity;
    double fuel;
    double fuelCapacity;
    double speed;

    public Bus(int id, Route route, int location, int passengers, int passengerCapacity, double fuel, double fuelCapacity, double speed) {
        this.id = id;
        this.route = route;
        this.location = location;
        this.passengers = passengers;
        this.passengerCapacity = passengerCapacity;
        this.fuel = fuel;
        this.fuelCapacity = fuelCapacity;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "(" +
                id + ", " +
                route.id + ", " +
                location + ", " +
                passengers + ", " +
                passengerCapacity + ", " +
                fuel + ", " +
                fuelCapacity + ", " +
                speed +
                ")";
    }

    public int getId() {
        return id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bus)) return false;
        Bus bus = (Bus) o;
        return id == bus.id &&
                location == bus.location &&
                passengers == bus.passengers &&
                passengerCapacity == bus.passengerCapacity &&
                Double.compare(bus.fuel, fuel) == 0 &&
                Double.compare(bus.fuelCapacity, fuelCapacity) == 0 &&
                Double.compare(bus.speed, speed) == 0 &&
                Objects.equals(route, bus.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, route, location, passengers, passengerCapacity, fuel, fuelCapacity, speed);
    }
}
