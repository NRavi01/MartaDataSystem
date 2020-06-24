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

    void addBus(Bus bus) throws SQLException;

    void addEvent(Event event) throws SQLException;

    void addRoute(Route route) throws SQLException;

    void addStop(Stop stop) throws SQLException;

    void updateBus(Bus bus) throws SQLException;

    void updateRoute(Route route) throws SQLException;

    void extendRoute(Route route, Stop stop) throws SQLException;

    void updateStop(Stop stop) throws SQLException;

    Bus getBus(int id) throws SQLException;

    Route getRoute(int id) throws SQLException;

    Stop getStop(int id) throws SQLException;

    List<Bus> getAllBusses() throws SQLException;

    List<Event> getAllEvents() throws SQLException;

    List<Route> getAllRoutes() throws SQLException;

    List<Stop> getAllStops() throws SQLException;

    List<Stop> getAllStops(int routeId) throws SQLException;

    void removeBus(Bus bus) throws SQLException;

    void removeRoute(Route route) throws SQLException;

    void removeStop(Stop stop) throws SQLException;

    void removeEvent(Event event) throws SQLException;
}
