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
    void parse_route() throws SQLException, IOException {
        Database db = DatabaseFactory.createEmptyDatabase();
        
        ZipFile zip = new ZipFile("/Users/wilwarner6/vcs/wwarner7/github.gatech.edu/MartaSimDataMgmt/src/com/martasim/data/gtfs022118.zip");
        GtfsParser parser = new GtfsParser(db, zip);
        parser.parse();
        Route route = new Route("7634", "1", "Centennial Oly. Park/Coronet Way");
        Route last_route = new Route("8747", "RED", "RED-North South North Springs Line");

        assertEquals(db.getRoute("7634"), route);
        assertEquals(db.getRoute("8747"), last_route);

    }
}
