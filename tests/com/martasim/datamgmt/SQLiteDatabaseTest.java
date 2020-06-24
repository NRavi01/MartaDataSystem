package com.martasim.datamgmt;

import com.martasim.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    }

    @Test
    void get_route() throws SQLException {
        Route route = new Route(1, 0, "route 0");
        db.addRoute(route);

        assertEquals(route, db.getRoute(1));
    }

    @Test
    void update_bus() throws SQLException {
        Route routeA = new Route(1, 1, "Route 1");
        db.addRoute(routeA);
        Bus busA = new Bus(0, routeA, 1, 2, 10, 10, 20, 40);

        db.addBus(busA);
        assertEquals(busA, db.getBus(busA.getId()));

        busA.setFuel(5);
        busA.setPassengers(5);
        assertNotEquals(busA, db.getBus(busA.getId()));

        db.updateBus(busA);
        assertEquals(busA, db.getBus(busA.getId()));
    }

    @Test
    void update_route() throws SQLException {
        Route routeA = new Route(1, 1, "Route 1");
        db.addRoute(routeA);
        assertEquals(routeA, db.getRoute(routeA.getId()));

        routeA.setNumber(2);
        routeA.setName("Route 2");
        assertNotEquals(routeA, db.getRoute(routeA.getId()));

        db.updateRoute(routeA);
        assertEquals(routeA, db.getRoute(routeA.getId()));
    }

    @Test
    void extend_route() throws SQLException {
        Route routeA = new Route(1, 1,"Route 1");
        db.addRoute(routeA);
        assertEquals(0, db.getRoute(routeA.getId()).getStops().size());

        Stop stopA = new Stop(1, "Stop 1", 2, 10, 10);
        db.addStop(stopA);

        db.extendRoute(routeA, stopA);
        int numStopsInRoute = db.getRoute(routeA.getId()).getStops().size();
        assertEquals(1, numStopsInRoute);

        if (numStopsInRoute > 0) {
            assertEquals(stopA, db.getRoute(routeA.getId()).getStops().get(numStopsInRoute-1));
        }
    }

    @Test
    void update_stop() throws SQLException {
        Stop stopA = new Stop(1, "Stop 1", 3, 10, 10);
        db.addStop(stopA);
        assertEquals(stopA, db.getStop(stopA.getId()));

        stopA.setRiders(5);
        assertNotEquals(stopA, db.getStop(stopA.getId()));

        db.updateStop(stopA);
        assertEquals(stopA, db.getStop(stopA.getId()));

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
