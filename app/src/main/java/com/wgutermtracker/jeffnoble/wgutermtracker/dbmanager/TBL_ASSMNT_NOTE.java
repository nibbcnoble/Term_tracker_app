package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

/**
 * Created by nibbc_000 on 6/12/2017.
 */

public class TBL_ASSMNT_NOTE {
    static final String NOTE_TABLE = "a_note_table";
    static final String NOTE_ID = "note_id";                               // primary key, not null auto increment
    static final String N_PARENT_A_ID = "n_parent_assessment_id";               // FOREIGN KEY, INT NOT NULL
    static final String NOTE_STRING = "note_string";
    static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTE_TABLE +
            "(" + NOTE_ID + " INTEGER PRIMARY KEY,"
            + N_PARENT_A_ID + " INTEGER,"
            + NOTE_STRING + " TEXT"
            + " )";
}
