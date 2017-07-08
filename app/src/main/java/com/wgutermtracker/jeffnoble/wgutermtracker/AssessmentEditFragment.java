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
 * {@link AssessmentEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AssessmentEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssessmentEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentEditFragment newInstance(String param1, String param2) {
        AssessmentEditFragment fragment = new AssessmentEditFragment();
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

        final View view = inflater.inflate(R.layout.fragment_assessment_edit, container, false);
        final long a_id = getArguments().getLong("a_id");

        DBHandler db = new DBHandler(view.getContext());

        final Assessment assessment = db.get_assessment(a_id);
        // set title
        EditText assessment_edit_title= (EditText) view.findViewById(R.id.assessmentEditTitle);
        assessment_edit_title.setText(assessment.get_title());

        // set type
        ArrayList<String> assessmentType = new ArrayList<>();
        assessmentType.add("Objective");
        assessmentType.add("Performance");

        Spinner typeSpinner = (Spinner) view.findViewById(R.id.assessmentEditTypeSpinner);
        ArrayAdapter<String> assessmentTypeAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, assessmentType);
        typeSpinner.setAdapter(assessmentTypeAdapter);

        int match_index=0;
        for (String type : assessmentType) {
            if (type.equals(assessment.get_assessment_type())) {
                break;
            }
            match_index++;
        }
        typeSpinner.setSelection(match_index);

        // set goal date
        DatePicker date_picker = (DatePicker) view.findViewById(R.id.assessmentEditDatePicker);

        Calendar cal = Calendar.getInstance();
        cal.setTime(assessment.get_goal_date());

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        date_picker.updateDate(year, month, day);

        // set update button
        Button button= (Button) view.findViewById(R.id.updateAssessmentBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_assessment(view,assessment, a_id);

            }
        });

        return view;
    }

    private void update_assessment(View view,Assessment assessment, long id) {

        // assessment name
        EditText assessment_name= (EditText) view.findViewById(R.id.assessmentEditTitle);
        String assessment_name_value = assessment_name.getText().toString();
        assessment.set_title(assessment_name_value);
        // assessment type
        Spinner statusSpinner = (Spinner) view.findViewById(R.id.assessmentEditTypeSpinner);
        String assessmentTypeString = (String) statusSpinner.getSelectedItem();
        assessment.set_assessment_type(assessmentTypeString);
        // assessment goal date
        DatePicker goal_date = (DatePicker) view.findViewById(R.id.assessmentEditDatePicker);
        Date goal_date_date = get_date_from_date_picker(goal_date);

        assessment.set_goal_date(goal_date_date);

        DBHandler db = new DBHandler(view.getContext());
        db.update_assessment(assessment, id);

        // save to database
        open_a(id);
    }

    private Date get_date_from_date_picker(DatePicker datepicker) {
        int day = datepicker.getDayOfMonth();
        int month = datepicker.getMonth();
        int year = datepicker.getYear();
        Calendar calendar = new GregorianCalendar(year,month,day);

        Date d = calendar.getTime();
        return d;
    }

    public void open_a(long a_id) {
        Fragment fragment = null;
        Class fragmentClass = AssessmentFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("a_id",a_id);
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
}
