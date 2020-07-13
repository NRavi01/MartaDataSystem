package com.martasim.datamgmt;

import com.martasim.models.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.zip.ZipFile;

class GtfsParser extends Parser {

    GtfsParser(Database database, ZipFile zipFile) {
        super(database, zipFile);
    }

    @Override
    public void parse() {
        try {
            System.out.println("Parsing Route");
            addRoutes(zipFile.getInputStream(zipFile.getEntry("gtfs022118/routes.txt")));
            System.out.println("Finished Routes, Parsing Stops");
            addStops(zipFile.getInputStream(zipFile.getEntry("gtfs022118/stops.txt")));
            System.out.println("Finished Stops, Parsing Buses");
            addBuses(zipFile.getInputStream(zipFile.getEntry("gtfs022118/trips.txt")));
            System.out.println("Finished Buses, Parsing Events");
            addEvents(zipFile.getInputStream(zipFile.getEntry("gtfs022118/stop_times.txt")));
            System.out.println("Finished Events");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void addRoutes(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        //initialize hash map with the first row of the file
        HashMap<String, String> map = new HashMap<>();
        String[] labels = br.readLine().split(",");
        for (String label : labels) {
            map.put(label, "");
        }

        String line;
        while ((line = br.readLine()) != null) {
            String[] st = (line + " ").split(",");

            try {
                for (int i = 0; i < labels.length; i++) {
                    map.replace(labels[i], st[i]);
                }
                database.addRoute(new Route(map.get("route_id"), map.get("route_short_name"), map.get("route_long_name")));
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        br.close();
    }

    private void addStops(InputStream inputStream) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        //initialize hash map with the first row of the file
        HashMap<String, String> map = new HashMap<>();
        String[] labels = br.readLine().split(",");
        for (String label: labels) {
            map.put(label, "");
        }

        StringBuilder sb = null;
        int counter = 0;
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] st = (line + " ").split(",");
            int st_index = 0;
            //update strings including apostrophes to work with SQL INSERT command
            for (String label: labels) {
                if (st[st_index].startsWith("\"")) {
                    String str = st[st_index].concat("," + st[st_index + 1]);
                    str = str.replace("\"", "");
                    map.replace(label, str);
                    st_index++;
                } else {
                    map.replace(label, st[st_index]);
                }
                st_index++;
            }
            if (counter % 10000 == 0) {
                if (counter > 0) {
                    try {
                        ((SQLiteDatabase) database).executeUpdate(sb.toString());
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                sb = new StringBuilder("INSERT INTO stop values ");
            } else {
                sb.append(',');
            }
            counter++;

            String stopId = map.get("stop_id");
            String stopName = map.get("stop_name").replace("'", "''");
            double lat = Double.parseDouble(map.get("stop_lat"));
            double lon = Double.parseDouble(map.get("stop_lon"));
            sb.append(String.format(
                    "('%s', '%s', %d, %f, %f)",
                    stopId,
                    stopName,
                    0,
                    lat,
                    lon
            ));
        }

        if (counter % 10000 > 0) {
            try {
                ((SQLiteDatabase) database).executeUpdate(sb.toString());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        br.close();
    }

    private void addBuses(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        HashMap<String, Integer> map = new HashMap<>();
        String[] labels = br.readLine().split(",");
        for (int i = 0; i < labels.length; i++) {
            map.put(labels[i], i);
        }

//        String line;
//        while ((line = br.readLine()) != null && !line.isEmpty()) {
//            String st[] = (line + ", ").split(",");
//            String routeId = st[map.get("route_id")];
//            String serviceId = st[map.get("service_id")];
//            String busId = st[map.get("trip_id")];
//            boolean outbound = st[map.get("direction_id")].trim().charAt(0) == '0';
//
//            try {
//                // TODO: add bus here
//                database.addBus(new Bus(
//                        busId,
//                        database.getRoute(routeId),
//                        outbound,
//                        /* TODO: get latitude */,
//                        /* TODO: get longitude */,
//                        /* TODO: get passengers */,
//                        /* TODO: get passengerCapacity */,
//                        /* TODO: get fuel */,
//                        /* TODO: get fuelCapacity */,
//                        /* TODO: get speed */
//                        ));
//            } catch (SQLException sqlException) {
//                sqlException.printStackTrace();
//            }
//        }

        br.close();
    }

    private void addEvents(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        HashMap<String, Integer> map = new HashMap<>();
        String[] labels = br.readLine().split(",");
        for (int i = 0; i < labels.length; i++) {
            map.put(labels[i], i);
        }

        StringBuilder sb = null;
        int counter = 0;
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            if (counter % 10000 == 0) {
                if (counter > 0) {
                    try {
                        ((SQLiteDatabase) database).executeUpdate(sb.toString());
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                sb = new StringBuilder("INSERT INTO event values ");
            } else {
                sb.append(',');
            }
            counter++;

            String[] st = (line + " ").split(",");
            String busId = st[map.get("trip_id")];
            String stopId = st[map.get("stop_id")];
            int arrivalTime = getLogicalTimeFromTimeString(st[map.get("arrival_time")]);
            int departureTime = getLogicalTimeFromTimeString(st[map.get("departure_time")]);
            sb.append(String.format(
                    "('%s', '%s', %d, %d)",
                    busId,
                    stopId,
                    arrivalTime,
                    departureTime
            ));
        }

        if (counter % 10000 > 0) {
            try {
                ((SQLiteDatabase) database).executeUpdate(sb.toString());
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        br.close();
    }

    /**
     * @param timeString in the format HH:MM:SS
     * @return the number of seconds since 00:00:00
     */
    int getLogicalTimeFromTimeString(String timeString) {
        String[] time = timeString.split(":");
        int hours = Integer.parseInt(time[0]);
        int min = Integer.parseInt(time[0]);
        int sec = Integer.parseInt(time[0]);

        return sec + (60 * min) + (60 * 60 * hours);
    }
}
