package com.martasim.datamgmt;

import com.martasim.models.Bus;
import com.martasim.models.Route;
import com.martasim.models.Stop;

import java.util.List;

public interface Database {

    void close();

    void add_bus(Bus bus);

    void add_route(Route route);

    void add_stop(Stop stop);

    void update_bus(Bus bus);

    void update_route(Route route);

    void update_stop(Stop stop);

    Bus get_bus(int id);

    Route get_route(int id);

    Stop get_stop(int id);

    List<Bus> get_all_busses();

    List<Route> get_all_routes();

    List<Stop> get_all_stops();

    void remove_bus(Bus bus);

    void remove_route(Route route);

    void remove_stop(Stop stop);
}
