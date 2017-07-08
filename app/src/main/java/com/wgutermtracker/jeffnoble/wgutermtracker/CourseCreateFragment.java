package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseCreateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseCreateFragment newInstance(String param1, String param2) {
        CourseCreateFragment fragment = new CourseCreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_course_create, container, false);

        DBHandler db = new DBHandler(view.getContext());

        ArrayList<Term> terms = db.get_all_terms();

        Spinner spinner = (Spinner) view.findViewById(R.id.CourseCreateTermSpinner);
        ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, terms);
        spinner.setAdapter(adapter);

        ArrayList<String> courseStatuses = new ArrayList<>();
        courseStatuses.add("In progress");
        courseStatuses.add("Dropped");
        courseStatuses.add("Completed");
        courseStatuses.add("Plan to take");

        Spinner statusSpinner = (Spinner) view.findViewById(R.id.CourseCreateStatusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, courseStatuses);
        statusSpinner.setAdapter(statusAdapter);

        Button button= (Button) view.findViewById(R.id.CourseCreateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_course(view);

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private Course add_new_course(View view) {

        EditText course_title_view= (EditText) view.findViewById(R.id.CourseCreateTitleText);
        String course_title = course_title_view.getText().toString();

        DatePicker start_date_picker = (DatePicker) view.findViewById(R.id.CourseCreateStartDate);
        Date start_date = get_date_from_date_picker(start_date_picker);

        DatePicker end_date_picker = (DatePicker) view.findViewById(R.id.CourseCreateEndDate);
        Date end_date = get_date_from_date_picker(end_date_picker);

        Spinner statusSpinner = (Spinner) view.findViewById(R.id.CourseCreateStatusSpinner);
        String status = (String) statusSpinner.getSelectedItem();

        Course course = new Course(course_title,status);
        course.set_course_timeline(start_date,end_date);

        Spinner spinner = (Spinner) view.findViewById(R.id.CourseCreateTermSpinner);
        Term term = (Term) spinner.getSelectedItem();

        DBHandler db = new DBHandler(view.getContext());

        long course_id = db.insert_course(course,term.get_term_id());

        EditText course_mentor_name_text= (EditText) view.findViewById(R.id.CourseCreateMentorName);
        String course_mentor_name = course_mentor_name_text.getText().toString();

        EditText course_mentor_phone_text= (EditText) view.findViewById(R.id.CourseCreateMentorPhone);
        String course_mentor_phone = course_mentor_phone_text.getText().toString();

        EditText course_mentor_email_text= (EditText) view.findViewById(R.id.CourseCreateMentorEmail);
        String course_mentor_email = course_mentor_email_text.getText().toString();

        CourseMentor mentor = new CourseMentor(course_mentor_name,course_mentor_phone,course_mentor_email);

        db.insert_mentor(mentor, course_id);
        openFragment();
        return course;
    }

    private Date get_date_from_date_picker(DatePicker datepicker) {
        int day = datepicker.getDayOfMonth();
        int month = datepicker.getMonth();
        int year = datepicker.getYear();
        Calendar calendar = new GregorianCalendar(year,month,day);

        Date d = calendar.getTime();
        return d;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void openFragment() {
        Fragment fragment = null;
        Class fragmentClass = CourseListFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}
