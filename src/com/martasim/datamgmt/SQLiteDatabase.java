package com.martasim.datamgmt;

import com.martasim.models.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class SQLiteDatabase implements Database {

    Connection connection;

    public SQLiteDatabase() throws SQLException {
        this("MartaSimulation.db");
    }

    public SQLiteDatabase(File file) throws SQLException {
        this(file.getAbsolutePath());
    }

    private SQLiteDatabase(String databasePath) throws SQLException {
        connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", databasePath));
    }

    private void executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.closeOnCompletion();
    }

    private ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        statement.closeOnCompletion();
        return resultSet;
    }

    public void clear() throws SQLException {
        executeUpdate("DROP TABLE IF EXISTS bus");
        executeUpdate("CREATE TABLE bus (id INTEGER PRIMARY KEY, route INTEGER, currentStop INTEGER, latitude REAL, longitude REAL, passengers INTEGER, passengerCapacity INTEGER, fuel real, fuelCapacity REAL, speed REAL)");
        executeUpdate("DROP TABLE IF EXISTS route");
        executeUpdate("CREATE TABLE route (id STRING PRIMARY KEY, shortName STRING, name STRING)");
        executeUpdate("DROP TABLE IF EXISTS routeToStop");
        executeUpdate("CREATE TABLE routeToStop (routeId INTEGER, stopId INTEGER, stopIndex INTEGER)");
        executeUpdate("DROP TABLE IF EXISTS stop");
        executeUpdate("CREATE TABLE stop (id INTEGER PRIMARY KEY, name STRING, riders INTEGER, latitude REAL, longitude REAL)");
        executeUpdate("DROP TABLE IF EXISTS event");
        executeUpdate("CREATE TABLE event (id INTEGER, time INTEGER, type STRING NOT NULL)");
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void addBus(Bus bus) throws SQLException {
        executeUpdate("INSERT INTO bus values" + bus);
    }

    @Override
    public void addEvent(Event event) throws SQLException {
        executeUpdate("INSERT INTO event values" + event);
    }

    @Override
    public void addRoute(Route route) throws SQLException {
        executeUpdate("INSERT INTO route values" + route);
    }

    @Override
    public void addStop(Stop stop) throws SQLException {
        executeUpdate("INSERT INTO stop values" + stop);
    }

    @Override
    public void updateBus(Bus bus) throws SQLException {
        executeUpdate(String.format("UPDATE bus SET route=%d, currentStop=%d, latitude=%f, longitude=%f, passengers=%d, passengerCapacity=%d, fuel=%f, fuelCapacity=%f, speed=%f WHERE id=%d",
                 bus.getRoute().getId(), bus.getCurrentStopIndex(), bus.getLatitude(), bus.getLongitude(), bus.getPassengers(), bus.getPassengerCapacity(), bus.getFuel(), bus.getFuelCapacity(), bus.getSpeed(), bus.getId()
        ));
    }

    @Override
    public void updateEvent(Event oldEvent, Event newEvent) throws SQLException {
        executeUpdate(String.format(
                "UPDATE event SET id=%d, time=%d, type='%s' WHERE id=%d AND time=%d AND type='%s'",
                newEvent.getId(),
                newEvent.getTime(),
                newEvent.getType().name(),
                oldEvent.getId(),
                oldEvent.getTime(),
                oldEvent.getType().name()
        ));
    }

    @Override
    public void updateRoute(Route route) throws SQLException {
        executeUpdate((String.format("UPDATE route SET shortName='%s', name='%s' WHERE id=%d",
                route.getShortName(), route.getName(), route.getId())));
    }

    @Override
    public void extendRoute(Route route, Stop stop) throws SQLException {
        executeUpdate(String.format("INSERT INTO routeToStop values (%d, %d, %d)", route.getId(), stop.getId(), route.getStops().size()));
        route.extend(stop);
    }

    @Override
    public void updateStop(Stop stop) throws SQLException {
        executeUpdate((String.format("UPDATE stop SET name='%s', riders=%d, latitude=%f, longitude=%f WHERE id=%d",
                stop.getName(), stop.getRiders(), stop.getLatitude(), stop.getLongitude(), stop.getId())));
    }

    @Override
    public Bus getBus(int id) throws SQLException {
        Bus bus = null;
        ResultSet resultSet = executeQuery("SELECT * FROM bus WHERE id=" + id);
        if (resultSet.next()) {
            bus = getBus(resultSet);
        }
        return bus;
    }

    private Bus getBus(ResultSet resultSet) throws SQLException {
        return new Bus(
                resultSet.getInt("id"),
                getRoute(resultSet.getString("route")),
                resultSet.getInt("currentStop"),
                resultSet.getDouble("latitude"),
                resultSet.getDouble("longitude"),
                resultSet.getInt("passengers"),
                resultSet.getInt("passengerCapacity"),
                resultSet.getDouble("fuel"),
                resultSet.getDouble("fuelCapacity"),
                resultSet.getDouble("speed")
        );
    }

    private Event getEvent(ResultSet resultSet) throws SQLException {
        return new Event(
                resultSet.getInt("id"), resultSet.getInt("time"),
                EventType.valueOf(resultSet.getString("type"))
        );
    }

    @Override
    public Route getRoute(String id) throws SQLException {
        Route route = null;
        ResultSet resultSet = executeQuery("SELECT * FROM route WHERE id=" + id);
        if (resultSet.next()) {
            route = getRoute(resultSet);
        }
        return route;
    }

    private Route getRoute(ResultSet resultSet) throws SQLException {
        return new Route(
                resultSet.getString("id"),
                resultSet.getString("shortName"),
                resultSet.getString("name"),
                getAllStops(resultSet.getInt("id"))
        );
    }

    @Override
    public Stop getStop(int id) throws SQLException {
        Stop stop = null;
        ResultSet resultSet = executeQuery("SELECT * FROM stop WHERE id=" + id);
        if (resultSet.next()) {
            stop = getStop(resultSet);
        }
        return stop;
    }

    private Stop getStop(ResultSet resultSet) throws SQLException {
        return new Stop (
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("riders"),
                resultSet.getDouble("latitude"),
                resultSet.getDouble("longitude")
        );
    }

    @Override
    public Collection<Bus> getAllBuses() throws SQLException {
        List<Bus> buses = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM bus");
        while (rs.next()) {
            buses.add(getBus(rs));
        }
        return buses;
    }

    @Override
    public Collection<Bus> getAllBuses(String routeId) throws SQLException {
        List<Bus> buses = new ArrayList<>();
        ResultSet rs = executeQuery("SELECT * FROM bus WHERE route=" + routeId);
        while (rs.next()) {
            buses.add(getBus(rs));
        }
        return buses;
    }

    @Override
    public Collection<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event");
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithId(int id) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE id=" + id);
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithTime(int time) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE time=" + time);
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithType(EventType type) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE type='" + type.name() + '\'');
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Route> getAllRoutes() throws SQLException {
        List<Route> routes = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM route");
        while (resultSet.next()) {
            routes.add(getRoute(resultSet));
        }
        return routes;
    }

    @Override
    public Collection<Stop> getAllStops() throws SQLException {
        List<Stop> stops = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM stop");
        while (resultSet.next()) {
            stops.add(getStop(resultSet));
        }
        return stops;
    }

    @Override
    public List<Stop> getAllStops(int routeId) throws SQLException {
        List<Stop> stops = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM routeToStop WHERE routeId=" + routeId + " ORDER BY stopIndex");
        while (resultSet.next()) {
            stops.add(getStop(resultSet.getInt("stopId")));
        }
        return stops;
    }

    @Override
    public void removeBus(Bus bus) throws SQLException {
        executeUpdate("DELETE FROM bus WHERE id=" + bus.getId());
    }

    @Override
    public void removeEvent(Event event) throws SQLException {
        executeUpdate("DELETE FROM event WHERE id=" + event.getId());
    }

    @Override
    public void removeRoute(Route route) throws SQLException {
        executeUpdate("DELETE FROM route WHERE id=" + route.getId());
        executeUpdate("DELETE FROM routeToStop WHERE routeId=" + route.getId());
    }

    @Override
    public void removeFromRoute(Route route, Stop stop) throws SQLException {
        // TODO
    }

    @Override
    public void removeFromRoute(int routeId, int stopId) throws SQLException {
        // TODO
    }

    @Override
    public void removeStop(Stop stop) throws SQLException {
        executeUpdate("DELETE FROM stop WHERE id=" + stop.getId());
        executeUpdate("DELETE FROM routeToStop WHERE stopId=" + stop.getId());
    }
}
