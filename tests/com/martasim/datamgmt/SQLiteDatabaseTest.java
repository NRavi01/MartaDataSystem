package com.martasim.datamgmt;

import com.martasim.models.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLiteDatabaseTest {

    SQLiteDatabase db;

    @BeforeEach
    void setup() {
        db = new SQLiteDatabase();
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void add_route() {
        List<Route> routes = db.get_all_routes();
        assertEquals(0, routes.size());

        Route route = new Route(0, 0, "route 0");
        db.add_route(route);

        routes = db.get_all_routes();

        assertEquals(1, routes.size());
        assertEquals(route, routes.get(0));
    }

    @Test
    void get_route() {
        Route route = new Route(1, 0, "route 0");
        db.add_route(route);

        assertEquals(route, db.get_route(1));
    }
}
