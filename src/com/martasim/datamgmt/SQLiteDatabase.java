package com.martasim.datamgmt;

import com.martasim.models.Bus;
import com.martasim.models.Route;
import com.martasim.models.Stop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase implements Database {

    final static int DEFAULT_TIMEOUT = 30;
    Connection connection;
    int timeout;

    public SQLiteDatabase() {
        this(DEFAULT_TIMEOUT);
    }

    public SQLiteDatabase(int timeout) {
        this.timeout = timeout;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:MartaSimulation.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);

            statement.executeUpdate("DROP TABLE IF EXISTS " + Bus.SQLITE_TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + Bus.SQLITE_DESCRIPTION);
            statement.executeUpdate("DROP TABLE IF EXISTS " + Route.SQLITE_TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + Route.SQLITE_DESCRIPTION);
            statement.executeUpdate("DROP TABLE IF EXISTS " + Stop.SQLITE_TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + Stop.SQLITE_DESCRIPTION);

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void add_bus(Bus bus) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("INSERT INTO " + Bus.SQLITE_TABLE_NAME + " values" + bus);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
    }

    @Override
    public void add_route(Route route) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("INSERT INTO " + Route.SQLITE_TABLE_NAME + " values" + route);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
    }

    @Override
    public void add_stop(Stop stop) {

    }

    @Override
    public void update_bus(Bus bus) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate(String.format("UPDATE %s SET route=%d, location=%d, passengers=%d, passengerCapacity=%d, fuel=%f, fuelCapacity=%f, speed=%f WHERE id=%d",
                    Bus.SQLITE_TABLE_NAME, bus.getRoute().getId(), bus.getLocation(), bus.getPassengers(), bus.getPassengerCapacity(), bus.getFuel(), bus.getFuelCapacity(), bus.getSpeed(), bus.getId()
            ));
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
    }

    @Override
    public void update_route(Route route) {

    }

    @Override
    public void update_stop(Stop stop) {

    }

    @Override
    public Bus get_bus(int id) {
        Bus bus = null;
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Bus.SQLITE_TABLE_NAME + " WHERE id=" + id);
            if (resultSet.next()) {
                bus = get_bus(resultSet);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return bus;
    }

    public Bus get_bus(ResultSet resultSet) {
        try {
            return new Bus(
                    resultSet.getInt("id"),
                    get_route(resultSet.getInt("route")),
                    resultSet.getInt("location"),
                    resultSet.getInt("passengers"),
                    resultSet.getInt("passengerCapacity"),
                    resultSet.getDouble("fuel"),
                    resultSet.getDouble("fuelCapacity"),
                    resultSet.getDouble("speed")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return null;
    }

    @Override
    public Route get_route(int id) {
        Route route = null;
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Route.SQLITE_TABLE_NAME + " WHERE id=" + id);
            if (resultSet.next()) {
                route = get_route(resultSet);
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return route;
    }

    private Route get_route(ResultSet resultSet) {
        try {
            return new Route(
                    resultSet.getInt("id"),
                    resultSet.getInt("number"),
                    resultSet.getString("name")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return null;
    }

    @Override
    public Stop get_stop(int id) {
        return null;
    }

    @Override
    public List<Bus> get_all_busses() {
        List<Bus> busses = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Bus.SQLITE_TABLE_NAME);
            while (rs.next()) {
                busses.add(new Bus(
                        rs.getInt("id"),
                        get_route(rs.getInt("route")),
                        rs.getInt("location"),
                        rs.getInt("passengers"),
                        rs.getInt("passengerCapacity"),
                        rs.getDouble("fuel"),
                        rs.getDouble("fuelCapacity"),
                        rs.getDouble("speed")
                ));
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return busses;
    }

    @Override
    public List<Route> get_all_routes() {
        List<Route> routes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Route.SQLITE_TABLE_NAME);
            while (resultSet.next()) {
                routes.add(get_route(resultSet));
            }
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            close();
            System.exit(0);
        }
        return routes;
    }

    @Override
    public List<Stop> get_all_stops() {
        return null;
    }

    @Override
    public void remove_bus(Bus bus) {

    }

    @Override
    public void remove_route(Route route) {

    }

    @Override
    public void remove_stop(Stop stop) {

    }
}
