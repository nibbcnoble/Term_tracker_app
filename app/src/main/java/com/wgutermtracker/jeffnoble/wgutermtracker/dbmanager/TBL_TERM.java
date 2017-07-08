package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class TBL_TERM {

    static final String TERM_TABLE = "term_table";
    static final String TERM_ID = "term_id";                               // primary key, not null auto increment
    static final String TERM_TITLE = "term_title";                         // VARCHAR
    static final String TERM_START_DATETIME = "term_end_datetime";         // DATETIME
    static final String TERM_END_DATETIME = "term_start_datetime";         // DATETIME
    static final String CREATE_TERM_TABLE = "CREATE TABLE " + TERM_TABLE +
            "(" + TERM_ID + " INTEGER PRIMARY KEY,"
            + TERM_TITLE + " TEXT,"
            + TERM_START_DATETIME + " DATETIME,"
            + TERM_END_DATETIME + " DATETIME"
            + " )";
}
