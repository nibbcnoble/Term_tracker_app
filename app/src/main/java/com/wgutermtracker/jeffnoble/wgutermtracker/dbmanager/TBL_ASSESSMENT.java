package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class TBL_ASSESSMENT {

    static final String ASSESSMENT_TABLE = "assessment_table";
    static final String ASSESSMENT_ID = "assessment_id";                   // primary key, not null auto increment
    static final String A_PARENT_COURSE_ID = "a_parent_course_id";         // FOREIGN KEY, INT NOT NULL
    static final String A_TITLE = "a_title";                               // VARCHAR
    static final String A_TYPE = "a_type";                                 // ENUM as text
    static final String GOAL_DATETIME = "goal_datetime";                   // DATETIME
    static final String CREATE_ASSESSMENT_TABLE = "CREATE TABLE " + ASSESSMENT_TABLE +
            "(" + ASSESSMENT_ID + " INTEGER PRIMARY KEY,"
            + A_PARENT_COURSE_ID + " INTEGER,"
            + A_TITLE + " TEXT,"
            + A_TYPE + " TEXT,"
            + GOAL_DATETIME + " TEXT"
            + " )";
}
