package com.martasim.datamgmt;

import com.martasim.models.Bus;
import com.martasim.models.Event;
import com.martasim.models.Route;
import com.martasim.models.Stop;

import java.sql.SQLException;
import java.util.List;

public interface Database {

    void clear() throws SQLException;

    void close() throws SQLException;

    /**
     * Adds a bus to the database.
     *
     * @param bus that is to be added to the database
     * @throws SQLException
     */
    void addBus(Bus bus) throws SQLException;

    /**
     * Adds an event to the database.
     *
     * @param event that is to be added to the database
     * @throws SQLException
     */
    void addEvent(Event event) throws SQLException;

    /**
     * Adds a route to the database.
     *
     * @param route that is to be added to the database
     * @throws SQLException
     */
    void addRoute(Route route) throws SQLException;

    /**
     * Adds a stop to the database.
     *
     * @param stop that is to be added to the database
     * @throws SQLException
     */
    void addStop(Stop stop) throws SQLException;

    /** Updates the route, location, passengers, passenger capacity, fuel, fuel capacity, and/or speed of a bus.
     *
     * @param bus Bus object that is updated to database
     * @throws SQLException
     */
    void updateBus(Bus bus) throws SQLException;

    /** Updates the name or number of a route. To add to a route's list of stops, use the extendRoute function instead.
     *
     * @param route Route object that is updated to database
     * @throws SQLException
     */
    void updateRoute(Route route) throws SQLException;

    /** Adds a stop to only the end of a route's list of stops
     *
     * @param route Route object that is getting a stop added to the end of its list of stops
     * @param stop Stop object that is getting added to the end of the route input
     * @throws SQLException
     */
    void extendRoute(Route route, Stop stop) throws SQLException;

    /** Updates name, # of riders, latitude, and/or longitude of a stop
     *
     *
     * @param stop Stop object that is updated to database
     * @throws SQLException
     */
    void updateStop(Stop stop) throws SQLException;

    /** Updates only the time of event
     *
     * @param event Event object that is updated to database
     * @throws SQLException
     */
    void updateEvent(Event event) throws SQLException;

    Bus getBus(int id) throws SQLException;

    Route getRoute(int id) throws SQLException;

    Stop getStop(int id) throws SQLException;

    List<Bus> getAllBusses() throws SQLException;

    List<Event> getAllEvents() throws SQLException;

    List<Route> getAllRoutes() throws SQLException;

    List<Stop> getAllStops() throws SQLException;

    List<Stop> getAllStops(int routeId) throws SQLException;

    /**
     * Removes a bus from the database.
     *
     * @param bus that is removed from the database
     * @throws SQLException
     */
    void removeBus(Bus bus) throws SQLException;

    /**
     * Removes a route from the database.
     *
     * @param route that is removed from the database
     * @throws SQLException
     */
    void removeRoute(Route route) throws SQLException;

    /**
     * Removes a stop from the database.
     *
     * @param stop that is removed from the database
     * @throws SQLException
     */
    void removeStop(Stop stop) throws SQLException;

    /**
     * Removes an event from the database.
     *
     * @param event that is removed from the database
     * @throws SQLException
     */
    void removeEvent(Event event) throws SQLException;
}
