package com.martasim.datamgmt;

import com.martasim.models.Bus;
import com.martasim.models.Event;
import com.martasim.models.Route;
import com.martasim.models.Stop;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase implements Database {

    final static int DEFAULT_TIMEOUT = 30;
    Connection connection;
    Statement statement;
    int timeout;

    public SQLiteDatabase() throws SQLException {
        this("MartaSimulation.db");
    }

    public SQLiteDatabase(File file) throws SQLException {
        this(file.getAbsolutePath());
    }

    private SQLiteDatabase(String databasePath) throws SQLException {
        connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", databasePath));
        statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
    }

    public void clear() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS bus");
        statement.executeUpdate("CREATE TABLE bus (id integer, route integer, location integer, passengers integer, passengerCapacity integer, fuel real, fuelCapacity real, speed real)");
        statement.executeUpdate("DROP TABLE IF EXISTS route");
        statement.executeUpdate("CREATE TABLE route (id INTEGER, number INTEGER, name STRING)");
        statement.executeUpdate("DROP TABLE IF EXISTS routeToStop");
        statement.executeUpdate("CREATE TABLE routeToStop (routeId INTEGER, stopId INTEGER, routeIndex INTEGER)");
        statement.executeUpdate("DROP TABLE IF EXISTS stop");
        statement.executeUpdate("CREATE TABLE stop (id INTEGER, name STRING, riders INTEGER, latitude REAL, longitude REAL)");
        statement.executeUpdate("DROP TABLE IF EXISTS event");
        statement.executeUpdate("CREATE TABLE event (time INTEGER, type STRING, id INTEGER)");
    }

    @Override
    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void addBus(Bus bus) throws SQLException {
        statement.executeUpdate("INSERT INTO bus values" + bus);
    }

    @Override
    public void addEvent(Event event) throws SQLException {
        statement.executeUpdate("INSERT INTO event values" + event);
    }

    @Override
    public void addRoute(Route route) throws SQLException {
        statement.executeUpdate("INSERT INTO route values" + route);
    }

    @Override
    public void addStop(Stop stop) throws SQLException {
        statement.executeUpdate("INSERT INTO stop values" + stop);
    }

    @Override
    public void updateBus(Bus bus) throws SQLException {
        statement.executeUpdate(String.format("UPDATE bus SET route=%d, location=%d, passengers=%d, passengerCapacity=%d, fuel=%f, fuelCapacity=%f, speed=%f WHERE id=%d",
                 bus.getRoute().getId(), bus.getLocation(), bus.getPassengers(), bus.getPassengerCapacity(), bus.getFuel(), bus.getFuelCapacity(), bus.getSpeed(), bus.getId()
        ));
    }

    @Override
    public void extendRoute(Route route, Stop stop) throws SQLException {
        statement.executeUpdate(String.format("INSERT INTO routeToStop values (%d, %d, %d)", route.getId(), stop.getId(), route.getStops().size()));
        route.extend(stop);
    }

    @Override
    public void updateStop(Stop stop) throws SQLException {
        statement.executeUpdate((String.format("UPDATE stop SET name='%s', riders=%d, latitude=%f, longitude=%f WHERE id=%d",
                stop.getName(), stop.getRiders(), stop.getLatitude(), stop.getLongitude(), stop.getId())));
    }

    @Override
    public Bus getBus(int id) throws SQLException {
        Bus bus = null;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM bus WHERE id=" + id);
        if (resultSet.next()) {
            bus = getBus(resultSet);
        }
        return bus;
    }

    private Bus getBus(ResultSet resultSet) throws SQLException {
        return new Bus(
                resultSet.getInt("id"),
                getRoute(resultSet.getInt("route")),
                resultSet.getInt("location"),
                resultSet.getInt("passengers"),
                resultSet.getInt("passengerCapacity"),
                resultSet.getDouble("fuel"),
                resultSet.getDouble("fuelCapacity"),
                resultSet.getDouble("speed")
        );
    }

    private Event getEvent(ResultSet resultSet) throws SQLException {
        return new Event(
                resultSet.getInt("id"),
                resultSet.getString("location"),
                resultSet.getInt("passengers")
        );
    }

    @Override
    public Route getRoute(int id) throws SQLException {
        Route route = null;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM route WHERE id=" + id);
        if (resultSet.next()) {
            route = getRoute(resultSet);
        }
        return route;
    }

    private Route getRoute(ResultSet resultSet) throws SQLException {
        return new Route(
                resultSet.getInt("id"),
                resultSet.getInt("number"),
                resultSet.getString("name"),
                getAllStops(resultSet.getInt("id"))
        );
    }

    @Override
    public Stop getStop(int id) throws SQLException {
        Stop stop = null;
        ResultSet resultSet = statement.executeQuery("SELECT * FROM stop WHERE id=" + id);
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
    public List<Bus> getAllBusses() throws SQLException {
        List<Bus> busses = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM bus");
        while (rs.next()) {
            busses.add(new Bus(
                    rs.getInt("id"),
                    getRoute(rs.getInt("route")),
                    rs.getInt("location"),
                    rs.getInt("passengers"),
                    rs.getInt("passengerCapacity"),
                    rs.getDouble("fuel"),
                    rs.getDouble("fuelCapacity"),
                    rs.getDouble("speed")
            ));
        }
        return busses;
    }

    @Override
    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM event");
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public List<Route> getAllRoutes() throws SQLException {
        List<Route> routes = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM route");
        while (resultSet.next()) {
            routes.add(getRoute(resultSet));
        }
        return routes;
    }

    @Override
    public List<Stop> getAllStops() throws SQLException {
        List<Stop> stops = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM stop");
        while (resultSet.next()) {
            stops.add(getStop(resultSet));
        }
        return stops;
    }

    @Override
    public List<Stop> getAllStops(int routeId) throws SQLException {
        List<Stop> stops = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM routeToStop WHERE routeId=" + routeId);
        while (resultSet.next()) {
            stops.add(getStop(resultSet.getInt("stopId")));
        }
        return stops;
    }

    @Override
    public void removeBus(Bus bus) throws SQLException {
        statement.executeUpdate("DELETE FROM bus WHERE id=" + bus.getId());
    }

    @Override
    public void removeRoute(Route route) throws SQLException {
        statement.executeUpdate("DELETE FROM route WHERE id=" + route.getId());
        statement.executeUpdate("DELETE FROM routeToStop WHERE routeId=" + route.getId());
    }

    @Override
    public void removeStop(Stop stop) throws SQLException {
        statement.executeUpdate("DELETE FROM stop WHERE id=" + stop.getId());
        statement.executeUpdate("DELETE FROM routeToStop WHERE stopId=" + stop.getId());
    }

    @Override
    public void removeEvent(Event event) throws SQLException {
        statement.executeUpdate("DELETE FROM event WHERE time=" + event.getTime() + ", type=" + event.getType() + ", id=" + event.getId());
    }
}
