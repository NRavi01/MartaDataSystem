package com.martasim.datamgmt;

import java.io.File;
import java.sql.SQLException;

public class DatabaseFactory {

    public static Database createEmptyDatabase() throws SQLException {
        Database database = new SQLiteDatabase();
        database.clear();
        return database;
    }

    public static Database createEmptyDatabase(File file) throws SQLException {
        Database database = new SQLiteDatabase(file);
        database.clear();
        return database;
    }

    public static Database createDatabaseFromDb(File file) throws SQLException {
        return new SQLiteDatabase(file);
    }

    public static Database createDatabaseFromGtfs(File file) throws SQLException {
        Database database = new SQLiteDatabase();
        (new GtfsParser(database, file)).parse();
        return database;
    }
}
