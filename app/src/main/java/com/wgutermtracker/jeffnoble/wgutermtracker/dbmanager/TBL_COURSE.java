package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class TBL_COURSE {

    static final String COURSE_TABLE = "course_table";
    static final String COURSE_ID = "course_id";                           // primary key, not null auto increment
    static final String C_PARENT_TERM_ID = "c_parent_term_id";             // FOREIGN KEY, INT NOT NULL
    static final String COURSE_TITLE = "course_title";                     // VARCHAR
    static final String COURSE_START_TIME = "course_start_time";           // DATETIME
    static final String COURSE_END_TIME = "course_end_time";               // DATETIME
    static final String COURSE_STATUS = "course_status";                   // ENUM as text

    static final String CREATE_COURSE_TABLE = "CREATE TABLE " + COURSE_TABLE +
            "(" + COURSE_ID + " INTEGER PRIMARY KEY,"
            + C_PARENT_TERM_ID + " INTEGER,"
            + COURSE_TITLE + " TEXT,"
            + COURSE_START_TIME + " DATETIME,"
            + COURSE_END_TIME + " DATETIME,"
            + COURSE_STATUS + " TEXT"
            + " )";
}
