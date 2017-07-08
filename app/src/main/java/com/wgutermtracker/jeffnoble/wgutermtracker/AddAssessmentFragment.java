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
 * {@link AddAssessmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAssessmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddAssessmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAssessmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAssessmentFragment newInstance(String param1, String param2) {
        AddAssessmentFragment fragment = new AddAssessmentFragment();
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
        // get bundle with course id
        final long course_id = getArguments().getLong("course_id");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_assessment, container, false);

        // assessment type
        ArrayList<String> assessmentType = new ArrayList<>();
        assessmentType.add("Objective");
        assessmentType.add("Performance");

        Spinner statusSpinner = (Spinner) view.findViewById(R.id.AddAssessmentTypeSpinner);
        ArrayAdapter<String> assessmentTypeAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, assessmentType);
        statusSpinner.setAdapter(assessmentTypeAdapter);

        Button button= (Button) view.findViewById(R.id.addAssessmentBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_assessment(view,course_id);

            }
        });


        return view;
    }

    private void add_assessment(View view, long course_id) {

        // assessment name
        EditText assessment_name= (EditText) view.findViewById(R.id.addAssessmentNameText);
        String assessment_name_value = assessment_name.getText().toString();

        // assessment type
        Spinner statusSpinner = (Spinner) view.findViewById(R.id.AddAssessmentTypeSpinner);
        String assessmentTypeString = (String) statusSpinner.getSelectedItem();

        // assessment goal date
        DatePicker goal_date = (DatePicker) view.findViewById(R.id.addAssessmentGoalDate);
        Date goal_date_date = get_date_from_date_picker(goal_date);

        Assessment assessment = new Assessment(assessment_name_value, assessmentTypeString);
        assessment.set_goal_date(goal_date_date);

        DBHandler db = new DBHandler(view.getContext());
        db.insert_assessment(assessment, course_id);

        open_course(course_id);
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

    private Date get_date_from_date_picker(DatePicker datepicker) {
        int day = datepicker.getDayOfMonth();
        int month = datepicker.getMonth();
        int year = datepicker.getYear();
        Calendar calendar = new GregorianCalendar(year,month,day);

        Date d = calendar.getTime();
        return d;
    }
}
