package com.martasim.datamgmt;

import com.martasim.models.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            addRoutes(zipFile.getInputStream(zipFile.getEntry("routes.txt")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void addRoutes(InputStream inputStream) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            
            // TODO: Use st.next() to keep getting the next value in the csv
            // Parse integers with Integer.parseInt(next())
            // Parse doubles with Double.parseDouble(next())
        }

        br.close();
    }
}
