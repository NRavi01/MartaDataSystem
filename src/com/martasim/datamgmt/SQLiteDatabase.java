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

    public void executeUpdate(String sql) throws SQLException {
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
        executeUpdate("CREATE TABLE bus (id INTEGER PRIMARY KEY, route STRING, outbound INTEGER, currentStop INTEGER, latitude REAL, longitude REAL, passengers INTEGER, passengerCapacity INTEGER, fuel real, fuelCapacity REAL, speed REAL)");
        executeUpdate("DROP TABLE IF EXISTS route");
        executeUpdate("CREATE TABLE route (id STRING PRIMARY KEY, shortName STRING, name STRING)");
        executeUpdate("DROP TABLE IF EXISTS routeToStop");
        executeUpdate("CREATE TABLE routeToStop (routeId STRING, stopId STRING, stopIndex INTEGER)");
        executeUpdate("DROP TABLE IF EXISTS stop");
        executeUpdate("CREATE TABLE stop (id STRING PRIMARY KEY, name STRING, riders INTEGER, latitude REAL, longitude REAL)");
        executeUpdate("DROP TABLE IF EXISTS event");
        executeUpdate("CREATE TABLE event (busId STRING, stopId STRING, arrivalTime INTEGER, departureTime INTEGER)");
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
        executeUpdate(String.format("UPDATE bus SET route='%s', outbound=%d, currentStop=%d, latitude=%f, longitude=%f, passengers=%d, passengerCapacity=%d, fuel=%f, fuelCapacity=%f, speed=%f WHERE id='%s'",
                 bus.getRoute().getId(), bus.getOutboundAsInt(), bus.getCurrentStopIndex(), bus.getLatitude(), bus.getLongitude(), bus.getPassengers(), bus.getPassengerCapacity(), bus.getFuel(), bus.getFuelCapacity(), bus.getSpeed(), bus.getId()
        ));
    }

    @Override
    public void updateEvent(Event oldEvent, Event newEvent) throws SQLException {
        executeUpdate(String.format(
                "UPDATE event SET busId='%s', stopId='%s', arrivalTime=%d, departureTime=%d WHERE busId='%s' AND stopId='%s' AND arrivalTime=%d AND departureTime=%d",
                newEvent.getBusId(),
                newEvent.getStopId(),
                newEvent.getArrivalTime(),
                newEvent.getDepartureTime(),
                oldEvent.getBusId(),
                oldEvent.getStopId(),
                oldEvent.getArrivalTime(),
                oldEvent.getDepartureTime()
        ));
    }

    @Override
    public void updateRoute(Route route) throws SQLException {
        executeUpdate((String.format("UPDATE route SET shortName='%s', name='%s' WHERE id='%s'",
                route.getShortName(), route.getName(), route.getId())));
    }

    @Override
    public void extendRoute(Route route, Stop stop) throws SQLException {
        executeUpdate(String.format("INSERT INTO routeToStop values ('%s', '%s', %d)", route.getId(), stop.getId(), route.getStops().size()));
        route.extend(stop);
    }

    @Override
    public void updateStop(Stop stop) throws SQLException {
        executeUpdate((String.format("UPDATE stop SET name='%s', riders=%d, latitude=%f, longitude=%f WHERE id='%s'",
                stop.getName(), stop.getRiders(), stop.getLatitude(), stop.getLongitude(), stop.getId())));
    }

    @Override
    public Bus getBus(String id) throws SQLException {
        Bus bus = null;
        ResultSet resultSet = executeQuery("SELECT * FROM bus WHERE id='" + id + '\'');
        if (resultSet.next()) {
            bus = getBus(resultSet);
        }
        return bus;
    }

    private Bus getBus(ResultSet resultSet) throws SQLException {
        return new Bus(
                resultSet.getString("id"),
                getRoute(resultSet.getString("route")),
                resultSet.getInt("outbound") != 0,
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
                resultSet.getString("busId"),
                resultSet.getString("stopId"),
                resultSet.getInt("arrivalTime"),
                resultSet.getInt("departureTime")
        );
    }

    @Override
    public Route getRoute(String id) throws SQLException {
        Route route = null;
        ResultSet resultSet = executeQuery("SELECT * FROM route WHERE id='" + id + '\'');
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
    public Stop getStop(String id) throws SQLException {
        Stop stop = null;
        ResultSet resultSet = executeQuery("SELECT * FROM stop WHERE id='" + id + '\'');
        if (resultSet.next()) {
            stop = getStop(resultSet);
        }
        return stop;
    }

    private Stop getStop(ResultSet resultSet) throws SQLException {
        return new Stop (
                resultSet.getString("id"),
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
    public Collection<Event> getAllEventsWithBusId(int busId) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE busId'=" + busId + '\'');
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithStopId(int stopId) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE stopId'=" + stopId + '\'');
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithArrivalTime(int arrivalTime) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE arrivalTime=" + arrivalTime);
        while (resultSet.next()) {
            events.add(getEvent(resultSet));
        }
        return events;
    }

    @Override
    public Collection<Event> getAllEventsWithDepartureTime(int departureTime) throws SQLException {
        List<Event> events = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT * FROM event WHERE departureTime=" + departureTime);
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
        ResultSet resultSet = executeQuery("SELECT * FROM routeToStop WHERE routeId='" + routeId + "' ORDER BY stopIndex");
        while (resultSet.next()) {
            stops.add(getStop(resultSet.getString("stopId")));
        }
        return stops;
    }

    @Override
    public void removeBus(Bus bus) throws SQLException {
        executeUpdate("DELETE FROM bus WHERE id='" + bus.getId() + '\'');
    }

    @Override
    public void removeEvent(Event event) throws SQLException {
        executeUpdate(String.format(
                "DELETE FROM event WHERE busId='%s' AND stopId='%s' AND arrivalTime=%d AND departureTime=%d",
                event.getBusId(),
                event.getStopId(),
                event.getArrivalTime(),
                event.getDepartureTime()
        ));
    }

    @Override
    public void removeRoute(Route route) throws SQLException {
        executeUpdate("DELETE FROM route WHERE id='" + route.getId() + '\'');
        executeUpdate("DELETE FROM routeToStop WHERE routeId='" + route.getId() + '\'');
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
        executeUpdate("DELETE FROM stop WHERE id='" + stop.getId() + '\'');
        executeUpdate("DELETE FROM routeToStop WHERE stopId='" + stop.getId() + '\'');
    }
}
