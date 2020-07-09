package com.martasim.datamgmt;

import com.martasim.models.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;

class GtfsParser extends Parser {


    GtfsParser(Database database, ZipFile zipFile) {
        super(database, zipFile);
    }

    @Override
    public void parse() {
        // TODO: read the zip file, parse it, and add all information to the database

        try {
            addRoutes(zipFile.getInputStream(zipFile.getEntry("gtfs022118/routes.txt")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void addRoutes(InputStream inputStream) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        //initialize hashmap with first column of file
        HashMap<String, String> map = new HashMap<>();
        String labels[] = (br.readLine() + " ").split(",");
        for (int i = 0; i < labels.length; i++) {
            map.put(labels[i], "");
        }

        String line;
        while ((line = br.readLine()) != null) {
            String st[] = (line + " ").split(",");
            
            // TODO: Use st.next() to keep getting the next value in the csv
            // Parse integers with Integer.parseInt(next())
            // Parse doubles with Double.parseDouble(next())
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
}
