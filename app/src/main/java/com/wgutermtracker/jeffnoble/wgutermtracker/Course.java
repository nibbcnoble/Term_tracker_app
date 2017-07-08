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

public class Course {

    private String title;
    private Date start_time;
    private Date end_time;
    private ArrayList<Assessment> performance_assessments;
    private ArrayList<Assessment> objective_assessments;
    private CourseStatus status;
    private ArrayList<CourseMentor> course_mentors;
    private ArrayList<Note> notes;
    private Term parent_term;
    private long course_id;

    public Course (String title, String status) {
        this.title = title;
        this.set_status(status);
        this.course_mentors =  new ArrayList<>();
        this.performance_assessments = new ArrayList<>();
        this.objective_assessments = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    public long get_course_id() {
        return this.course_id;
    }

    public void set_course_id(long id) {
        this.course_id = id;
    }

    public void set_course_timeline(Date start_time, Date end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String get_course_start_formatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(this.start_time);
    }

    public String get_course_end_formatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(this.end_time);
    }

    public void set_course_title(String title) {
        this.title = title;
    }

    public void set_course_timeline_from_strings(String start_time, String end_time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = df.parse(start_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.start_time = startDate;
        Date endDate = null;
        try {
            endDate = df.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.end_time = endDate;
    }

    public void add_assessment(Assessment a) {
        if (a.get_assessment_type_enum() == AssessmentType.OBJECTIVE) {
            objective_assessments.add(a);
        } else {
            performance_assessments.add(a);
        }
    }

    public void set_assessments(ArrayList<Assessment> a_ll) {
        for (Assessment as : a_ll) {
            as.set_parent_course(this);
            add_assessment(as);
        }
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

    public void set_status(CourseStatus s) {
        this.status = s;
    }

    public void set_parent_term(Term parent_term) {
        this.parent_term = parent_term;
    }
    public String get_status() {
        String status_string = "";

        switch (status) {
            case IN_PROGRESS:
                status_string = "In progress"; break;
            case COMPLETED:
                status_string = "Completed"; break;
            case DROPPED:
                status_string = "Dropped"; break;
            case PLAN_TO_TAKE:
                status_string = "Plan to take"; break;
        }
        return status_string;
    }

    public void set_status(String st) {
        switch (st) {
            case "In progress":
                this.status = CourseStatus.IN_PROGRESS; break;
            case "Completed":
                this.status = CourseStatus.COMPLETED; break;
            case "Dropped":
                this.status = CourseStatus.DROPPED; break;
            case "Plan to take":
                this.status = CourseStatus.PLAN_TO_TAKE; break;
        }

    }

    public ArrayList<Assessment> get_all_performance_assessments() {
        return this.performance_assessments;
    }

    public ArrayList<Assessment> get_all_objective_assessments() {
        return this.objective_assessments;
    }



    public void set_course_mentors(ArrayList<CourseMentor> m) {
        this.course_mentors = m; System.out.println("COURSE MENTORS :"+m.size());
    }

    public ArrayList<CourseMentor> get_course_mentors() {
        return this.course_mentors;
    }

    public Date get_start_time() {
        return this.start_time;
    }

    public Date get_end_time() {
        return this.end_time;
    }

    public String get_title() {
        return this.title;
    }

    public Term get_parent_term() {
        return this.parent_term;
    }


}
