package com.martasim.datamgmt;

import com.martasim.models.Route;
import com.martasim.models.Stop;

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
//            addBuses(zipFile.getInputStream(zipFile.getEntry("gtfs022118/RENAME ME.txt")));
//            addEvents(zipFile.getInputStream(zipFile.getEntry("gtfs022118/RENAME ME.txt")));
            addRoutes(zipFile.getInputStream(zipFile.getEntry("gtfs022118/routes.txt")));
            addStops(zipFile.getInputStream(zipFile.getEntry("gtfs022118/stops.txt")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void addBuses(InputStream inputStream) throws IOException {
        // TODO
    }

    private void addEvents(InputStream inputStream) throws IOException {
        // TODO
    }

    private void addRoutes(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        //initialize hash map with the first row of the file
        HashMap<String, String> map = new HashMap<>();
        String labels[] = br.readLine().split(",");
        for (String label : labels) {
            map.put(label, "");
        }

        String line;
        while ((line = br.readLine()) != null) {
            String st[] = (line + " ").split(",");

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
        String labels[] = br.readLine().split(",");
        for (String label: labels) {
            map.put(label, "");
        }

        String line;
        while ((line = br.readLine()) != null) {
            String st[] = (line + " ").split(",");

            try {
                int st_index = 0;
                for (int i = 0; i < labels.length; i++) {
                    if (st[st_index].startsWith("\"")) {
                        String str = st[st_index].concat("," + st[st_index + 1]);
                        str = str.replace("\"", "");
                        map.replace(labels[i], str);
                        st_index++;
                    } else {
                        map.replace(labels[i], st[st_index]);
                    }
                    st_index++;
                }
                database.addStop(new Stop(
                        map.get("stop_id"),
                        map.get("stop_name").replace("'", "''"),
                        0,
                        Double.parseDouble(map.get("stop_lat")),
                        Double.parseDouble(map.get("stop_lon"))
                ));
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        br.close();
    }
}
