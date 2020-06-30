package com.martasim.datamgmt;

import com.martasim.models.Bus;
import com.martasim.models.Event;
import com.martasim.models.Route;
import com.martasim.models.Stop;

import java.sql.SQLException;
import java.util.Collection;
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

    void updateBus(Bus bus) throws SQLException;

    void extendRoute(Route route, Stop stop) throws SQLException;

    void updateStop(Stop stop) throws SQLException;

    Event getEvent(int id) throws SQLException;

    Bus getBus(int id) throws SQLException;

    Route getRoute(int id) throws SQLException;

    Stop getStop(int id) throws SQLException;

    Collection<Bus> getAllBuses() throws SQLException;

    Collection<Bus> getAllBuses(int routeId) throws SQLException;

    Collection<Event> getAllEvents(int time) throws SQLException;

    Collection<Event> getAllEvents() throws SQLException;

    Collection<Route> getAllRoutes() throws SQLException;

    Collection<Stop> getAllStops() throws SQLException;

    Collection<Stop> getAllStops(int routeId) throws SQLException;

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
