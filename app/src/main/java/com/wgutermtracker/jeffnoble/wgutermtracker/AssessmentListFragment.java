package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssessmentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssessmentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentListFragment newInstance(String param1, String param2) {
        AssessmentListFragment fragment = new AssessmentListFragment();
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
        final View view = inflater.inflate(R.layout.fragment_assessment_list, container, false);
        LinearLayout assessment_list_layout = (LinearLayout) view.findViewById(R.id.assessmentListLayout);

        DBHandler db = new DBHandler(view.getContext());

        ArrayList<Assessment> assessments = db.get_assessments();

        for (final Assessment assessment : assessments) {
            Button abutton = new Button(view.getContext());
            abutton.setText(assessment.get_title());
            abutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // open_course(course.get_course_id());
                    open_a(assessment.get_id());
                }
            });
            assessment_list_layout.addView(abutton);
        }
        return view;
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
