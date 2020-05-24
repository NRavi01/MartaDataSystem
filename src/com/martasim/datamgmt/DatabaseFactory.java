package com.martasim.datamgmt;

import java.io.File;
import java.sql.SQLException;

public class DatabaseFactory {
    public static Database createDatabaseFromGtfs(File file) throws SQLException {
        Database database = new SQLiteDatabase();
        (new GtfsParser(database, file)).parse();
        return database;
    }
}
