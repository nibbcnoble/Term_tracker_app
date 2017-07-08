package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class TBL_MENTOR {

    static final String MENTOR_TABLE = "mentor_table";
    static final String MENTOR_ID = "mentor_id";                           // primary key, not null auto increment
    static final String M_PARENT_COURSE_ID = "m_parent_course_id";         // FOREIGN KEY, INT NOT NULL
    static final String MENTOR_NAME = "mentor_name";                       // VARCHAR
    static final String MENTOR_PHONE = "mentor_phone";                     // VARCHAR
    static final String MENTOR_EMAIL = "mentor_email";                     // VARCHAR
    static final String CREATE_MENTOR_TABLE = "CREATE TABLE " + MENTOR_TABLE +
            "(" + MENTOR_ID + " INTEGER PRIMARY KEY,"
            + M_PARENT_COURSE_ID + " INTEGER,"
            + MENTOR_NAME + " TEXT,"
            + MENTOR_PHONE + " TEXT,"
            + MENTOR_EMAIL + " TEXT"
            + " )";
}
