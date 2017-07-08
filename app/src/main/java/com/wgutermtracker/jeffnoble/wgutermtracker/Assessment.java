package com.wgutermtracker.jeffnoble.wgutermtracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nibbc_000 on 6/3/2017.
 */

public class Assessment {

    private String title;
    private AssessmentType assessment_type;
    private Course parent_course;
    private Date goal_date;
    private ArrayList<Note> notes;
    private int id;
    public Assessment(String title, String at) {
        this.title = title;
        set_assessment_type(at);
    }

    public void set_id(int id) { this.id = id;}

    public int get_id() { return this.id; }

    public void set_title(String title) {
        this.title = title;
    }

    public String get_goal_formatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(this.goal_date);
    }

    public void set_goal_date(Date d) {
        this.goal_date =d;
    }

    public void add_notes(Note note) {
        notes.add(note);
    }

    public void set_notes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public ArrayList<Note> get_notes() {
        return this.notes;
    }

    public void set_goal_date_from_string(String goal_date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null;
        try {
            endDate = df.parse(goal_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.goal_date = endDate;
    }

    public Date get_goal_date() {
        return this.goal_date;
    }

    public String get_title() {
        return this.title;
    }

    public String get_assessment_type() {
        String type = "";

        switch (this.assessment_type) {
            case PERFORMANCE: type = "Performance";
                break;
            case OBJECTIVE: type = "Objective";
                break;
        }
        return type;
    }

    public void set_assessment_type(String atype) {

        switch (atype) {
            case "Performance": assessment_type = AssessmentType.PERFORMANCE;
                break;
            case "Objective": assessment_type = AssessmentType.OBJECTIVE;
                break;
        }

    }

    public AssessmentType get_assessment_type_enum() {
        return this.assessment_type;
    }

    public Course get_parent_course() {
        return this.parent_course;
    }

    public void set_parent_course(Course c) {
        this.parent_course = c;
    }
}
