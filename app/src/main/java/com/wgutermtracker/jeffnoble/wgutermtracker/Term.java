package com.wgutermtracker.jeffnoble.wgutermtracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nibbc_000 on 6/3/2017.
 */

public class Term {

    private String title;
    private Date start_time;
    private Date end_time;
    private ArrayList<Course> courses;
    private int term_id;


    public Term (String title) {
        this.title = title;
        courses = new ArrayList<>();
    }

    public void set_term_timeline(Date start_time, Date end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public void set_term_id(int id) {
        this.term_id = id;
    }

    public int get_term_id() {
        return this.term_id;
    }

    public void set_term_timeline_from_string(String start_time, String end_time) {
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

    public  String get_term_dates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd", Locale.getDefault());
        if (this.start_time!=null) {
            System.out.println(this.end_time);
            return dateFormat.format(this.start_time) + " - " + dateFormat.format(this.end_time);
        } else {
            System.out.println(this.start_time.toString());
            return "none";
        }

    }

    public String get_title() {
        return this.title;
    }

    public void add_course(Course c) {
        this.courses.add(c);
    }

    public void set_courses(ArrayList<Course> c) {
        this.courses = c;
        for (Course course : c) {
            course.set_parent_term(this);
        }
    }

    public ArrayList<Course> get_courses() {
        return this.courses;
    }

    public Date get_start_time() {
        return this.start_time;
    }

    public Date get_end_time() {
        return this.end_time;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
