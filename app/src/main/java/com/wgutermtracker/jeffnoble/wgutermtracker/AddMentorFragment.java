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
import android.widget.EditText;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMentorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMentorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMentorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddMentorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMentorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMentorFragment newInstance(String param1, String param2) {
        AddMentorFragment fragment = new AddMentorFragment();
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
        final View view = inflater.inflate(R.layout.fragment_add_mentor, container, false);


        Button button = (Button) view.findViewById(R.id.MentorBtnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_mentor(view,course_id);

            }
        });
        return view;
    }

    private void add_mentor(View view , long course_id) {
        EditText mentor_name_text= (EditText) view.findViewById(R.id.MentorNameAdd);
        String mentor_name = mentor_name_text.getText().toString();

        EditText mentor_phone_text= (EditText) view.findViewById(R.id.MentorPhoneAdd);
        String mentor_phone = mentor_phone_text.getText().toString();

        EditText mentor_email_text= (EditText) view.findViewById(R.id.MentorEmailAdd);
        String mentor_email = mentor_email_text.getText().toString();

        CourseMentor mentor = new CourseMentor(mentor_name,mentor_phone,mentor_email);

        DBHandler db = new DBHandler(view.getContext());
        db.insert_mentor(mentor, course_id);

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
}
