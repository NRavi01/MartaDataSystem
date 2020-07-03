package com.martasim.datamgmt;

import com.martasim.models.*;

import java.io.*;
import java.sql.SQLException;

public class GTFSParsingTest {

    String stops = "src/com/martasim/datasets/stops.txt";
    String routes = "datasets/routes.txt";

    String line = "";

    int stopId;
    String stopName;
    int stopRiders;
    double stopLat;
    double stopLong;

    public void parse(Database database) {

        try {
            BufferedReader stopsReader = new BufferedReader(new FileReader(stops));
            line = stopsReader.readLine(); //first line is column headings

            while ((line = stopsReader.readLine()) != null) { //reader is on 2nd line now where the actual data starts
                String[] stop = line.split(",");
                stopId = Integer.parseInt(stop[0]);
                stopName = stop[2];
                stopRiders = 0; //doesn't have riders at the start
                stopLat = Double.parseDouble(stop[3]);
                stopLong = Double.parseDouble(stop[4]);

                database.addStop(new Stop(stopId, stopName, stopRiders, stopLat, stopLong));
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + e.getMessage());
        } catch (IOException e ) {
            System.out.println("Problem reading file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLite database operation exception: " + e.getMessage());
        }
    }
}
