package com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wgutermtracker.jeffnoble.wgutermtracker.Assessment;
import com.wgutermtracker.jeffnoble.wgutermtracker.Course;
import com.wgutermtracker.jeffnoble.wgutermtracker.CourseMentor;
import com.wgutermtracker.jeffnoble.wgutermtracker.CourseStatus;
import com.wgutermtracker.jeffnoble.wgutermtracker.DBCompleteInterface;
import com.wgutermtracker.jeffnoble.wgutermtracker.Note;
import com.wgutermtracker.jeffnoble.wgutermtracker.Term;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static java.util.Arrays.asList;

/**
 * Created by nibbc_000 on 6/6/2017.
 */

public class DBHandler extends SQLiteOpenHelper {


    public DBHandler(Context context) {
        super(context, "wgu", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TBL_TERM.CREATE_TERM_TABLE);

        sqLiteDatabase.execSQL(TBL_COURSE.CREATE_COURSE_TABLE);

        sqLiteDatabase.execSQL(TBL_MENTOR.CREATE_MENTOR_TABLE);

        sqLiteDatabase.execSQL(TBL_ASSESSMENT.CREATE_ASSESSMENT_TABLE);

        sqLiteDatabase.execSQL(TBL_NOTE.CREATE_NOTE_TABLE);

        sqLiteDatabase.execSQL(TBL_PHOTO.CREATE_PHOTO_TABLE);

        sqLiteDatabase.execSQL(TBL_ASSMNT_NOTE.CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int prevVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_TERM.TERM_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_COURSE.COURSE_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_MENTOR.MENTOR_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_ASSESSMENT.ASSESSMENT_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_NOTE.NOTE_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_PHOTO.PHOTO_TABLE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBL_ASSMNT_NOTE.NOTE_TABLE);

        sqLiteDatabase.execSQL(TBL_TERM.CREATE_TERM_TABLE);

        sqLiteDatabase.execSQL(TBL_COURSE.CREATE_COURSE_TABLE);

        sqLiteDatabase.execSQL(TBL_MENTOR.CREATE_MENTOR_TABLE);

        sqLiteDatabase.execSQL(TBL_ASSESSMENT.CREATE_ASSESSMENT_TABLE);

        sqLiteDatabase.execSQL(TBL_NOTE.CREATE_NOTE_TABLE);

        sqLiteDatabase.execSQL(TBL_PHOTO.CREATE_PHOTO_TABLE);

        sqLiteDatabase.execSQL(TBL_ASSMNT_NOTE.CREATE_NOTE_TABLE);

    }


    // TERM Read ALL
    public ArrayList<Term> get_all_terms() {
        ArrayList<Term> termList = new ArrayList<Term>();
        String selectQuery = "SELECT  * FROM " + TBL_TERM.TERM_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Term term = new Term(cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_TITLE)));
                term.set_term_id(cursor.getInt(cursor.getColumnIndex(TBL_TERM.TERM_ID)));
                term.set_term_timeline_from_string(cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_START_DATETIME)),cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_END_DATETIME)));
                term.set_courses(get_courses_for_term(cursor.getInt(cursor.getColumnIndex(TBL_TERM.TERM_ID))));
               // System.out.println("TERM START TIME INSERT"+term.get_start_time().toString());
                termList.add(term);
            } while (cursor.moveToNext());

        }
        return termList;
    }

    // TERM READ ONE
    public Term get_term(int index) {
        String selectQuery = "SELECT  * FROM " + TBL_TERM.TERM_TABLE+" WHERE "+ TBL_TERM.TERM_ID + " = "+index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Term term = null;
        if (cursor.moveToFirst()) {
            term = new Term(cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_TITLE)));
            // get all courses for term
            term.set_term_timeline_from_string(cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_START_DATETIME)),cursor.getString(cursor.getColumnIndex(TBL_TERM.TERM_END_DATETIME)));
            term.set_courses(get_courses_for_term(cursor.getInt(cursor.getColumnIndex(TBL_TERM.TERM_ID))));
        }
        return term;
    }

    // TERM INSERT ONE
    public void insert_term(Term t, DBCompleteInterface callb) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TBL_TERM.TERM_TITLE, t.get_title());
        values.put(TBL_TERM.TERM_START_DATETIME, getDateTime(t.get_start_time()));
        values.put(TBL_TERM.TERM_END_DATETIME, getDateTime(t.get_end_time()));
        sqLiteDatabase.insert(TBL_TERM.TERM_TABLE, null, values);
        sqLiteDatabase.close();

        callb.openFragment();
    }

    // TERM UPDATE FIELDS
    public void update_term(Term t, int index, String field) {

    }

    // DELETE TERM
    public void delete_term(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_TERM.TERM_TABLE, TBL_TERM.TERM_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // COURSE Read ALL
    public ArrayList<Course> get_all_courses() {
        ArrayList<Course> courseList = new ArrayList<Course>();
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = course = unpack_course_cursor(cursor);
                courseList.add(course);
            } while (cursor.moveToNext());

        }
        return courseList;
    }

    // COURSE Read ALL for TERM
    public ArrayList<Course> get_courses_for_term(int term_index) {
        ArrayList<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE + " WHERE " + TBL_COURSE.C_PARENT_TERM_ID + " = " + term_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = course = unpack_course_cursor(cursor);

                courseList.add(course);
            } while (cursor.moveToNext());

        }
        return courseList;
    }

    // COURSE Read ONE
    public Course get_course(long course_index) {
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE + " WHERE " + TBL_COURSE.COURSE_ID+ " = " + course_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        Course course = null;
        if (cursor.moveToFirst()) {
            course = unpack_course_cursor(cursor);

        }
        return course;
    }

    private Course unpack_course_cursor(Cursor cursor) {
        String status  = cursor.getString(cursor.getColumnIndex(TBL_COURSE.COURSE_STATUS));
        String course_title = cursor.getString(cursor.getColumnIndex(TBL_COURSE.COURSE_TITLE));
        Course course = new Course(course_title, status);
        course.set_course_timeline_from_strings(cursor.getString(cursor.getColumnIndex(TBL_COURSE.COURSE_START_TIME)),cursor.getString(cursor.getColumnIndex(TBL_COURSE.COURSE_END_TIME)));
        course.set_course_id(cursor.getInt(cursor.getColumnIndex(TBL_COURSE.COURSE_ID)));
        // add assessments
        course.set_assessments(get_assessments_for_course(cursor.getInt(cursor.getColumnIndex(TBL_COURSE.COURSE_ID))));
        // notes
        course.set_notes(get_notes_for_course(cursor.getInt(cursor.getColumnIndex(TBL_COURSE.COURSE_ID))));
        // mentors
        course.set_course_mentors(get_mentors_for_course(cursor.getInt(cursor.getColumnIndex(TBL_COURSE.COURSE_ID))));
        return course;
    }

    // COURSE INSERT ONE
    public long insert_course(Course c, int term_index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_COURSE.C_PARENT_TERM_ID, term_index);
        values.put(TBL_COURSE.COURSE_END_TIME, getDateTime(c.get_end_time()));
        values.put(TBL_COURSE.COURSE_START_TIME, getDateTime(c.get_start_time()));
        values.put(TBL_COURSE.COURSE_STATUS, c.get_status());
        values.put(TBL_COURSE.COURSE_TITLE, c.get_title());
        long course_id = sqLiteDatabase.insert(TBL_COURSE.COURSE_TABLE, null, values);
        sqLiteDatabase.close();


        return course_id;
    }

    public void update_course(Course c, long course_index, long term_index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_COURSE.C_PARENT_TERM_ID, term_index);
        values.put(TBL_COURSE.COURSE_END_TIME, getDateTime(c.get_end_time()));
        values.put(TBL_COURSE.COURSE_START_TIME, getDateTime(c.get_start_time()));
        values.put(TBL_COURSE.COURSE_STATUS, c.get_status());
        values.put(TBL_COURSE.COURSE_TITLE, c.get_title());
        String[] args = new String[]{""+course_index};
        sqLiteDatabase.update(TBL_COURSE.COURSE_TABLE, values, TBL_COURSE.COURSE_ID + "=?", args);
        sqLiteDatabase.close();



    }

    public long get_course_parent_id(long course_index) {
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE + " WHERE " + TBL_COURSE.COURSE_ID+ " = " + course_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        long course_parent = 0;
        if (cursor.moveToFirst()) {
            course_parent = cursor.getInt(cursor.getColumnIndex(TBL_COURSE.C_PARENT_TERM_ID));

        }
        return course_parent;
    }

    // COURSE UPDATE FIELD
    public void update_course(Course c, int index, String field) {

    }

    // DELETE COURSE
    public void delete_course(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_COURSE.COURSE_TABLE, TBL_COURSE.COURSE_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // MENTOR INSERT ONE
    public void insert_mentor(CourseMentor m, long course_index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_MENTOR.MENTOR_NAME, m.get_name());
        values.put(TBL_MENTOR.MENTOR_PHONE, m.get_phone_number());
        values.put(TBL_MENTOR.MENTOR_EMAIL, m.get_email_address());
        values.put(TBL_MENTOR.M_PARENT_COURSE_ID, course_index);
        sqLiteDatabase.insert(TBL_MENTOR.MENTOR_TABLE, null, values);
        sqLiteDatabase.close();
    }

    // MENTOR Read ALL for COURSE
    public ArrayList<CourseMentor> get_mentors_for_course(int course_index) {
        ArrayList<CourseMentor> mentors = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_MENTOR.MENTOR_TABLE + " WHERE " + TBL_MENTOR.M_PARENT_COURSE_ID + " = " + course_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(TBL_MENTOR.MENTOR_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(TBL_MENTOR.MENTOR_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(TBL_MENTOR.MENTOR_EMAIL));

                CourseMentor mentor = new CourseMentor(name,phone,email);

                mentors.add(mentor);
            } while (cursor.moveToNext());

        }
        System.out.println("Number of mentors in DB:"+mentors.size());
        return mentors;
    }

    // DELETE MENTOR
    public void delete_mentor(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_MENTOR.MENTOR_TABLE, TBL_MENTOR.MENTOR_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // ASSESSMENT Read All
    public ArrayList<Assessment> get_assessments() {
        ArrayList<Assessment> assessments = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_ASSESSMENT.ASSESSMENT_TABLE ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TITLE));
                String a_type = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TYPE));

                Assessment assessment = new Assessment(title,a_type);
                assessment.set_goal_date_from_string(cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.GOAL_DATETIME)));
                assessment.set_notes(get_notes_for_assessment(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID))));
                assessment.set_id(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID)));
                assessments.add(assessment);
            } while (cursor.moveToNext());

        }
        return assessments;
    }

    // ASSESSMENT Read ALL for Course
    public ArrayList<Assessment> get_assessments_for_course(int course_index) {
        ArrayList<Assessment> assessments = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_ASSESSMENT.ASSESSMENT_TABLE + " WHERE " + TBL_ASSESSMENT.A_PARENT_COURSE_ID + " = " + course_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TITLE));
                String a_type = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TYPE));

                Assessment assessment = new Assessment(title,a_type);
                assessment.set_goal_date_from_string(cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.GOAL_DATETIME)));
                assessment.set_notes(get_notes_for_assessment(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID))));
                assessment.set_id(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID)));
                assessments.add(assessment);
            } while (cursor.moveToNext());

        }
        System.out.println("ASSESSMENTS:"+assessments.size());
        return assessments;
    }

    public Assessment get_assessment(long assessment_id) {
        String selectQuery = "SELECT  * FROM " + TBL_ASSESSMENT.ASSESSMENT_TABLE + " WHERE " + TBL_ASSESSMENT.ASSESSMENT_ID + " = " + assessment_id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Assessment assessment = null;
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TITLE));
            String a_type = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TYPE));

            assessment = new Assessment(title,a_type);
            assessment.set_goal_date_from_string(cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.GOAL_DATETIME)));
            assessment.set_notes(get_notes_for_assessment(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID))));
            assessment.set_id(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID)));

        }

        return assessment;
    }

    // ASSESSMENT INSERT ONE
    public void insert_assessment(Assessment a, long course_index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_ASSESSMENT.A_TITLE, a.get_title());
        values.put(TBL_ASSESSMENT.A_TYPE, a.get_assessment_type());
        values.put(TBL_ASSESSMENT.GOAL_DATETIME, getDateTime(a.get_goal_date()));
        values.put(TBL_ASSESSMENT.A_PARENT_COURSE_ID, course_index);
        sqLiteDatabase.insert(TBL_ASSESSMENT.ASSESSMENT_TABLE, null, values);
        sqLiteDatabase.close();
    }

    // update assessment
    public void update_assessment(Assessment a, long a_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_ASSESSMENT.A_TITLE, a.get_title());
        values.put(TBL_ASSESSMENT.A_TYPE, a.get_assessment_type());
        values.put(TBL_ASSESSMENT.GOAL_DATETIME, getDateTime(a.get_goal_date()));
        String[] args = new String[]{""+a_id};
        sqLiteDatabase.update(TBL_ASSESSMENT.ASSESSMENT_TABLE, values, TBL_ASSESSMENT.ASSESSMENT_ID + "=?", args);
        sqLiteDatabase.close();



    }

    // DELETE ASSESSMENT
    public void delete_assessment(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_ASSESSMENT.ASSESSMENT_TABLE, TBL_ASSESSMENT.ASSESSMENT_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // NOTE Read ALL for Course
    public ArrayList<Note> get_notes_for_course(int course_index) {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_NOTE.NOTE_TABLE + " WHERE " + TBL_NOTE.N_PARENT_COURSE_ID + " = " + course_index;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getInt(cursor.getColumnIndex(TBL_NOTE.NOTE_ID));
                String note_string = cursor.getString(cursor.getColumnIndex(TBL_NOTE.NOTE_STRING));
                Note note = new Note(note_string);
                note.set_photos(get_photos_for_note(index));
                notes.add(note);
            } while (cursor.moveToNext());

        }
        return notes;
    }

    // NOTE INSERT ONE
    public void insert_note(Note note, long course_index) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_NOTE.NOTE_STRING, note.get_note_string());
        values.put(TBL_NOTE.N_PARENT_COURSE_ID, course_index);
        long id = sqLiteDatabase.insert(TBL_NOTE.NOTE_TABLE, null, values);
        sqLiteDatabase.close();

        for(String p : note.get_photos()) {
            this.insert_photo(p, id);
        }
    }

    // DELETE NOTE
    public void delete_note(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_NOTE.NOTE_TABLE, TBL_NOTE.NOTE_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    public ArrayList<Note> get_notes_for_assessment(int a_id) {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_ASSMNT_NOTE.NOTE_TABLE + " WHERE " + TBL_ASSMNT_NOTE.N_PARENT_A_ID + " = " + a_id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getInt(cursor.getColumnIndex(TBL_ASSMNT_NOTE.NOTE_ID));
                String note_string = cursor.getString(cursor.getColumnIndex(TBL_ASSMNT_NOTE.NOTE_STRING));
                Note note = new Note(note_string);
                note.set_photos(get_photos_for_note(index));
                notes.add(note);
            } while (cursor.moveToNext());

        }
        return notes;
    }

    // NOTE INSERT ONE
    public void insert_a_note(Note note, long a_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_ASSMNT_NOTE.NOTE_STRING, note.get_note_string());
        values.put(TBL_ASSMNT_NOTE.N_PARENT_A_ID, a_id);
        long id = sqLiteDatabase.insert(TBL_ASSMNT_NOTE.NOTE_TABLE, null, values);
        sqLiteDatabase.close();

        for(String p : note.get_photos()) {
            this.insert_photo(p, id);
        }
    }

    // DELETE NOTE
    public void delete_assessment_note(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_ASSMNT_NOTE.NOTE_TABLE, TBL_ASSMNT_NOTE.NOTE_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // PHOTO Read ALL for Note

    public ArrayList<String> get_photos_for_note(int note_id) {
        ArrayList<String> photos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_PHOTO.PHOTO_TABLE + " WHERE " + TBL_PHOTO.P_PARENT_NOTE_ID + " = " + note_id;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String photo_title = cursor.getString(cursor.getColumnIndex(TBL_PHOTO.PHOTO_FILE));
                photos.add(photo_title);
            } while (cursor.moveToNext());

        }
        return photos;
    }

    // PHOTO INSERT One
    public void insert_photo(String photo_name, long parent_note_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TBL_PHOTO.PHOTO_FILE, photo_name);
        values.put(TBL_PHOTO.P_PARENT_NOTE_ID, parent_note_id);
        sqLiteDatabase.insert(TBL_PHOTO.PHOTO_TABLE, null, values);
        sqLiteDatabase.close();
    }

    // DELETE PHOTO
    public void delete_photo(long id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TBL_PHOTO.PHOTO_TABLE, TBL_PHOTO.PHOTO_ID + " = ?",
                new String[] { String.valueOf(id)});
        sqLiteDatabase.close();
    }

    // get all current courses
    public ArrayList<Course> get_all_current_courses() {
        ArrayList<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE + " WHERE " + TBL_COURSE.COURSE_STATUS + " = 'In progress'" ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = course = unpack_course_cursor(cursor);

                courseList.add(course);
            } while (cursor.moveToNext());

        }
        return courseList;
    }

    // get most recent course notes
    public ArrayList<Note> get_most_recent_notes() {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_NOTE.NOTE_TABLE + " ORDER BY "+TBL_NOTE.NOTE_ID+" DESC LIMIT 2";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getInt(cursor.getColumnIndex(TBL_NOTE.NOTE_ID));
                String note_string = cursor.getString(cursor.getColumnIndex(TBL_NOTE.NOTE_STRING));
                Note note = new Note(note_string);
                note.set_photos(get_photos_for_note(index));
                notes.add(note);
            } while (cursor.moveToNext());

        }
        return notes;
    }

    // get courses ending on today

    public ArrayList<Course> get_courses_due_today() {
        ArrayList<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_COURSE.COURSE_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = course = unpack_course_cursor(cursor);
                if (isDueToday(course.get_end_time())) {
                    courseList.add(course);
                }

            } while (cursor.moveToNext());

        }
        return courseList;
    }

    // ASSESSMENT Read All
    public ArrayList<Assessment> get_assessments_due() {
        ArrayList<Assessment> assessments = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TBL_ASSESSMENT.ASSESSMENT_TABLE ;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TITLE));
                String a_type = cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.A_TYPE));

                Assessment assessment = new Assessment(title,a_type);
                assessment.set_goal_date_from_string(cursor.getString(cursor.getColumnIndex(TBL_ASSESSMENT.GOAL_DATETIME)));
                assessment.set_notes(get_notes_for_assessment(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID))));
                assessment.set_id(cursor.getInt(cursor.getColumnIndex(TBL_ASSESSMENT.ASSESSMENT_ID)));
                if (isDueToday(assessment.get_goal_date())) {
                    assessments.add(assessment);
                }

            } while (cursor.moveToNext());

        }
        return assessments;
    }

    private String getDateTime(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(d);
    }

    private boolean isDueToday(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());

        String dueDate = dateFormat.format(d);

        return dueDate.equals(dateFormat.format(Calendar.getInstance().getTime()));
    }

}
