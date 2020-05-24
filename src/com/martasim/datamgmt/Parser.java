package com.martasim.datamgmt;

import java.io.File;

public abstract class Parser {
    Database database;
    File file;

    Parser(Database database, File file) {
        this.database = database;
        this.file = file;
    }

    abstract void parse();
}
