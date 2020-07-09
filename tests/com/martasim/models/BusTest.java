package com.martasim.models;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BusTest {

    @Test
    void setCurrentStopIndex_to_less_than_negative_one() {
        Bus bus = new Bus(0, null, 0, 0, 0, 0, 0, 0, 0);
        assertThrows(RuntimeException.class, () -> bus.setCurrentStopIndex(-2));
    }

    @Test
    void setCurrentStopIndex_with_null_route() {
        Bus bus = new Bus(0, null, 0, 0, 0, 0, 0, 0, 0);
        assertThrows(RuntimeException.class, () -> bus.setCurrentStopIndex(0));
    }

    @Test
    void setCurrentStopIndex_too_large() {
        Route route = new Route(
                "0",
                "0",
                "0",
                Arrays.asList(
                        new Stop(0, "0", 0, 0, 0),
                        new Stop(1, "1", 1, 1, 1)
                )
        );
        Bus bus = new Bus(0, route, 0, 0, 0, 0, 0, 0, 0);
        assertThrows(RuntimeException.class, () -> bus.setCurrentStopIndex(2));
    }

    @Test
    void setCurrentStopIndex() {
        final Bus a = new Bus(0, null, 0, 0, 0, 0, 0, 0, 0);
        assertDoesNotThrow(() -> a.setCurrentStopIndex(-1));

        Stop firstStop = new Stop(0, "0", 0, 0, 0);
        Stop secondStop = new Stop(1, "1", 1, 1, 1);
        Route route = new Route("0", "0", "0", Arrays.asList(firstStop, secondStop));

        final Bus b = new Bus(1, route, 1, 1, 1, 1, 1, 1, 1);
        assertEquals(null, b.getCurrentStop());

        assertDoesNotThrow(() -> b.setCurrentStopIndex(0));
        assertEquals(0, b.getCurrentStopIndex());
        assertEquals(firstStop, b.getCurrentStop());

        assertDoesNotThrow(() -> b.setCurrentStopIndex(1));
        assertEquals(1, b.getCurrentStopIndex());
        assertEquals(secondStop, b.getCurrentStop());
    }

    @Test
    void getNextStopIndex_with_null_route() {
        Bus bus = new Bus(0, null, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(-1, bus.getNextStopIndex());
    }

    @Test
    void getNextStopIndex() {
        Stop firstStop = new Stop(0, "0", 0, 0, 0);
        Stop secondStop = new Stop(1, "1", 1, 1, 1);
        Route route = new Route("0", "0", "0", Arrays.asList(firstStop, secondStop));

        Bus bus = new Bus(0, route, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(-1, bus.getNextStopIndex());

        bus.setCurrentStopIndex(0);
        assertEquals(1, bus.getNextStopIndex());

        bus.setCurrentStopIndex(1);
        assertEquals(0, bus.getNextStopIndex());
    }

    @Test
    void getNextStop_with_null_route() {
        final Bus bus = new Bus(0, null, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(null, bus.getNextStop());
    }

    @Test
    void getNextStop() {
        Stop firstStop = new Stop(0, "0", 0, 0, 0);
        Stop secondStop = new Stop(1, "1", 1, 1, 1);
        Route route = new Route("0", "0", "0", Arrays.asList(firstStop, secondStop));

        Bus bus = new Bus(0, route, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(null, bus.getNextStop());

        bus.setCurrentStopIndex(0);
        assertEquals(secondStop, bus.getNextStop());

        bus.setCurrentStopIndex(1);
        assertEquals(firstStop, bus.getNextStop());
    }

    @Test
    void goToNextStop() {
        Stop firstStop = new Stop(0, "0", 0, 0, 0);
        Stop secondStop = new Stop(1, "1", 1, 1, 1);
        Route route = new Route("0", "0", "0", Arrays.asList(firstStop, secondStop));

        Bus bus = new Bus(0, route, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(-1, bus.getNextStopIndex());
        bus.goToNextStop();
        assertEquals(-1, bus.getNextStopIndex());

        bus.setCurrentStopIndex(0);
        assertEquals(1, bus.getNextStopIndex());

        bus.goToNextStop();
        assertEquals(0, bus.getNextStopIndex());
    }
}
