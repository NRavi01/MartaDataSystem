package com.martasim.datamgmt;

import com.martasim.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

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
        Collection<Route> routes = db.getAllRoutes();
        assertEquals(0, routes.size());

        Route route = new Route(0, 0, "route 0");
        db.addRoute(route);

        routes = db.getAllRoutes();

        assertEquals(1, routes.size());
        assertEquals(true, routes.contains(route));
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
        assertEquals(2, db.getAllBuses().size());

        db.removeBus(A);
        assertEquals(1, db.getAllBuses().size());
        assertEquals(false, db.getAllBuses().contains(A));
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
        assertEquals(true, db.getAllRoutes().contains(B));
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

        assertEquals(true, db.getAllStops().contains(B));
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
        assertEquals(true, db.getAllEvents().contains(B));
    }

    @Test
    void read_route() throws SQLException {
        Route A = new Route(0, 0, "0");

        db.addRoute(A);

        assertEquals(A, db.getRoute(A.getId()));
    }

    @Test
    void read_bus() throws SQLException {
        Route A = new Route(0, 0, "0");
        Bus B = new Bus(0, A, 0, 0, 0, 0, 0, 0);

        db.addRoute(A);
        db.addBus(B);

        assertEquals(B, db.getBus(B.getId()));
    }

    @Test
    void read_stop() throws SQLException {
        Stop S = new Stop(0, "Stop 0", 0, 0, 0);

        db.addStop(S);

        assertEquals(S, db.getStop(S.getId()));
    }

    @Test
    void read_event() throws SQLException {
        Event E = new Event(0, 0, EventType.move_bus);

        db.addEvent(E);

        assertEquals(E, db.getEvent(E.getId()));
    }

    @Test
    void read_all_buses() throws SQLException {
        Route A = new Route(0, 0, "0");
        Route B = new Route(1, 1, "1");
        Route C = new Route(2, 2, "2");
        Bus X = new Bus(0, A, 0, 0, 0, 0, 0, 0);
        Bus Y = new Bus(1, B, 1, 5, 5, 0, 0, 0);
        Bus Z = new Bus(2, C, 2, 10, 10, 0, 0, 0);
        Collection<Bus> buses = new HashSet<>(Arrays.asList(X, Y, Z));

        db.addRoute(A);
        db.addRoute(B);
        db.addRoute(C);
        db.addBus(X);
        db.addBus(Y);
        db.addBus(Z);
        assertEquals(3, db.getAllBuses().size());

        assertEquals(buses, new HashSet<>(db.getAllBuses()));
    }

    @Test
    void read_all_routes() throws SQLException {
        Route A = new Route(0, 0, "0");
        Route B = new Route(1, 1, "1");
        Route C = new Route(2, 2, "2");
        Collection<Route> routes = new HashSet<>(Arrays.asList(A, B, C));

        db.addRoute(A);
        db.addRoute(B);
        db.addRoute(C);
        assertEquals(3, db.getAllRoutes().size());

        assertEquals(routes, new HashSet<>(db.getAllRoutes()));
    }

    @Test
    void read_all_stops() throws SQLException {
        Stop A = new Stop(0, "Stop 0", 0, 0, 0);
        Stop B = new Stop(1, "Stop 1", 5, 0, 0);
        Stop C = new Stop(2, "Stop 2", 10, 0, 0);
        Collection<Stop> stops = new HashSet<>(Arrays.asList(A, B, C));

        db.addStop(A);
        db.addStop(B);
        db.addStop(C);
        assertEquals(3, db.getAllStops().size());

        assertEquals(stops, new HashSet<>(db.getAllStops()));
    }

    @Test
    void read_all_events() throws SQLException {
        Event A = new Event(0, 0, EventType.move_bus);
        Event B = new Event(1, 2, EventType.move_bus);
        Event C = new Event(2, 5, EventType.move_bus);
        Collection<Event> events = new HashSet<>(Arrays.asList(A, B, C));


        db.addEvent(A);
        db.addEvent(B);
        db.addEvent(C);
        assertEquals(3, db.getAllEvents().size());

        assertEquals(events, new HashSet<>(db.getAllEvents()));
    }
}
