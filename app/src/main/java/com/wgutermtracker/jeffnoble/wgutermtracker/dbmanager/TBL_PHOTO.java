package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class TBL_PHOTO {

    static final String PHOTO_TABLE = "photo_table";
    static final String PHOTO_ID = "photo_id";                             // primary key, not null auto increment
    static final String P_PARENT_NOTE_ID = "p_parent_note_id";             // FOREIGN KEY, INT NOT NULL
    static final String PHOTO_FILE = "photo_file";
    static final String CREATE_PHOTO_TABLE = "CREATE TABLE " + PHOTO_TABLE +
            "(" + PHOTO_ID + " INTEGER PRIMARY KEY,"
            + P_PARENT_NOTE_ID + " INTEGER,"
            + PHOTO_FILE + " TEXT"
            + " )";

}
