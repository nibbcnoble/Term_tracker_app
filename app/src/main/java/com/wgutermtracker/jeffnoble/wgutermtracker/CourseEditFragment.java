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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseEditFragment newInstance(String param1, String param2) {
        CourseEditFragment fragment = new CourseEditFragment();
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
        final View view = inflater.inflate(R.layout.fragment_course_edit, container, false);
        final long course_id = getArguments().getLong("course_id");


        DBHandler db = new DBHandler(view.getContext());

        final Course course = db.get_course(course_id);

        ArrayList<Term> terms = db.get_all_terms();

        Spinner spinner = (Spinner) view.findViewById(R.id.CourseEditTermSpinner);
        ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, terms);
        spinner.setAdapter(adapter);
        long term_parent_id = db.get_course_parent_id(course_id);
        int match_index=0;
        for (Term term : terms) {
            if (term.get_term_id() == term_parent_id) {
                break;
            }
            match_index++;
        }
        spinner.setSelection(match_index);

        ArrayList<String> courseStatuses = new ArrayList<>();
        courseStatuses.add("In progress");
        courseStatuses.add("Dropped");
        courseStatuses.add("Completed");
        courseStatuses.add("Plan to take");

        Spinner statusSpinner = (Spinner) view.findViewById(R.id.CourseEditStatusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, courseStatuses);
        statusSpinner.setAdapter(statusAdapter);

        match_index=0;
        for (String status : courseStatuses) {
            if (status.equals(course.get_status())) {
                break;
            }
            match_index++;
        }
        statusSpinner.setSelection(match_index);

        // set start date
        DatePicker start_date_picker = (DatePicker) view.findViewById(R.id.CourseEditStartDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(course.get_start_time());

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        start_date_picker.updateDate(year, month, day);

        DatePicker end_date_picker = (DatePicker) view.findViewById(R.id.CourseEditEndDate);

        Calendar ecal = Calendar.getInstance();
        ecal.setTime(course.get_end_time());

        int emonth = ecal.get(Calendar.MONTH);
        int eday = ecal.get(Calendar.DAY_OF_MONTH);
        int eyear = ecal.get(Calendar.YEAR);

        end_date_picker.updateDate(eyear,emonth,eday);


        EditText course_title= (EditText) view.findViewById(R.id.CourseEditTitle);
        course_title.setText(course.get_title());

        Button button= (Button) view.findViewById(R.id.updateCourseBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_course(view, course);

            }
        });

        return view;
    }

    public void save_course(View view, Course course) {
        DBHandler db = new DBHandler(view.getContext());

        EditText course_title_view= (EditText) view.findViewById(R.id.CourseEditTitle);
        String course_title = course_title_view.getText().toString();
        course.set_course_title(course_title);

        DatePicker start_date_picker = (DatePicker) view.findViewById(R.id.CourseEditStartDate);
        Date start_date = get_date_from_date_picker(start_date_picker);

        DatePicker end_date_picker = (DatePicker) view.findViewById(R.id.CourseEditEndDate);
        Date end_date = get_date_from_date_picker(end_date_picker);

        course.set_course_timeline(start_date,end_date);

        Spinner statusSpinner = (Spinner) view.findViewById(R.id.CourseEditStatusSpinner);
        String status = (String) statusSpinner.getSelectedItem();
        course.set_status(status);

        Spinner spinner = (Spinner) view.findViewById(R.id.CourseEditTermSpinner);
        Term term = (Term) spinner.getSelectedItem();

        db.update_course(course, course.get_course_id(),term.get_term_id());

        open_course(course.get_course_id());
    }

    public void open_course(long course_id) {
        Fragment fragment = null;
        Class fragmentClass = CourseFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("course_id",course_id);
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private Date get_date_from_date_picker(DatePicker datepicker) {
        int day = datepicker.getDayOfMonth();
        int month = datepicker.getMonth();
        int year = datepicker.getYear();
        Calendar calendar = new GregorianCalendar(year,month,day);

        Date d = calendar.getTime();
        return d;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
