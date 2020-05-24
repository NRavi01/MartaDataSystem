package com.martasim.datamgmt;

import com.martasim.models.Route;
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
}
