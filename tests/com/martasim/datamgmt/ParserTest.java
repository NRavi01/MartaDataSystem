package com.martasim.datamgmt;

import com.martasim.models.Route;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    void parse_route() throws SQLException {
        Database db = DatabaseFactory.createEmptyDatabase();
        try {
            ZipFile zip = new ZipFile("/Users/wilwarner6/vcs/wwarner7/github.gatech.edu/MartaSimDataMgmt/src/com/martasim/data/gtfs022118.zip");
            GtfsParser parser = new GtfsParser(db, zip);
            parser.parse();
            Route route = new Route("7634", "1", "Centennial Oly. Park/Coronet Way");
            assertEquals(db.getRoute("7634"), route);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
