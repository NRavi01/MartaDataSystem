package com.martasim.datamgmt;

import com.martasim.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLiteDatabaseTest {

    SQLiteDatabase db;

    @BeforeEach
    void setup() throws SQLException {
        db = new SQLiteDatabase();
        db.clear();
    }

    @AfterEach
    void tearDown() throws SQLException {
        db.close();
    }

    @Test
    void add_route() throws SQLException {
        List<Route> routes = db.getAllRoutes();
        assertEquals(0, routes.size());

        Route route = new Route(0, 0, "route 0");
        db.addRoute(route);

        routes = db.getAllRoutes();

        assertEquals(1, routes.size());
        assertEquals(route, routes.get(0));

        Stop stop = new Stop(0, "stop 0", 0, 0, 0);
        List <Stop> stops = new ArrayList<>();
        stops.add(stop);
        Route route2 = new Route(1, 1, "route 1", stops);
        db.addRoute(route2);

        routes = db.getAllRoutes();

        assertEquals(2, routes.size());
        assertEquals(route2, routes.get(1));
    }

    @Test
    void add_bus() throws SQLException {
        List<Bus> busses = db.getAllBusses();
        assertEquals(0, busses.size());

        Route route = new Route(0, 0, "route 0");
        Bus bus1 = new Bus(0, null, 0, 0 , 10 , 0, 10, 0);
        Bus bus2 = new Bus(1, route, 1, 1 , 11 , 1, 11, 1);
        db.addBus(bus1);

        busses = db.getAllBusses();

        assertEquals(1, busses.size());
        assertEquals(bus1, busses.get(0));

        db.addBus(bus2);
        busses = db.getAllBusses();

        assertEquals(2, busses.size());
        assertEquals(bus2, busses.get(1));
    }

    @Test
    void add_stop() throws SQLException {
        List<Stop> stops = db.getAllStops();
        assertEquals(0, stops.size());

        Stop stop = new Stop (0, "stop 0", 0, 0, 0);

        db.addStop(stop);

        stops = db.getAllStops();

        assertEquals(1, stops.size());
        assertEquals(stop, stops.get(0));
    }

    @Test
    void add_event() throws SQLException {
        List<Event> events = db.getAllEvents();
        assertEquals(0, events.size());

        Event event = new Event (0, 0 , EventType.move_bus);

        db.addEvent(event);

        events = db.getAllEvents();

        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }

    @Test
    void get_route() throws SQLException {
        Route route = new Route(1, 0, "route 0");
        db.addRoute(route);

        assertEquals(route, db.getRoute(1));
    }

    @Test
    void remove_bus() throws SQLException {
        Route R = new Route(0, 0, "0");
        Bus A = new Bus(0, R, 0, 0, 0, 0, 0, 0);
        Bus B = new Bus(1, R, 1, 1, 1, 1, 1, 1);

        db.addRoute(R);
        db.addBus(A);
        db.addBus(B);
        assertEquals(2, db.getAllBusses().size());

        db.removeBus(A);
        assertEquals(1, db.getAllBusses().size());
        assertEquals(B, db.getAllBusses().get(0));
    }

    @Test
    void remove_route() throws SQLException {
        Route A = new Route(0, 0, "0");
        Route B = new Route(1, 1, "1");

        db.addRoute(A);
        db.addRoute(B);
        assertEquals(2, db.getAllRoutes().size());

        db.removeRoute(A);
        assertEquals(1, db.getAllRoutes().size());
        assertEquals(B, db.getAllRoutes().get(0));
    }

    @Test
    void remove_stop() throws SQLException {
        Stop A = new Stop(0, "0", 0, 0, 0);
        Stop B = new Stop(1, "1", 1, 1, 1);

        db.addStop(A);
        db.addStop(B);

        Route R = new Route(0,0,"0");
        db.addRoute(R);
        db.extendRoute(R, A);
        db.extendRoute(R, B);
        assertEquals(2, db.getAllStops().size());
        assertEquals(2, db.getAllStops(0).size());

        db.removeStop(A);
        assertEquals(1, db.getAllStops().size());
        assertEquals(1, db.getAllStops(0).size());

        assertEquals(B, db.getAllStops().get(0));
    }

    @Test
    void remove_event() throws SQLException {
        Event A = new Event(0,0, EventType.move_bus);
        Event B = new Event(1,1, EventType.move_bus);

        db.addEvent(A);
        db.addEvent(B);
        assertEquals(2, db.getAllEvents().size());

        db.removeEvent(A);
        assertEquals(1, db.getAllEvents().size());
        assertEquals(B, db.getAllEvents().get(0));
    }
}
