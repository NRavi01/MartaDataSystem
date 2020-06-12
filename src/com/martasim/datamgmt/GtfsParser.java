package com.martasim.datamgmt;

import java.io.File;

class GtfsParser extends Parser {


    GtfsParser(Database database, File file) {
        super(database, file);
    }

    @Override
    public void parse() {
        // TODO (5/24/20): read the file, parse it, and add all information to the database
    }
}
