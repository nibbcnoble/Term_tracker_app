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
import android.widget.TextView;
import android.widget.Toast;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TermFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TermFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermFragment newInstance(String param1, String param2) {
        TermFragment fragment = new TermFragment();
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
        final View view = inflater.inflate(R.layout.fragment_term, container, false);

        LinearLayout courses_layout = (LinearLayout) view.findViewById(R.id.TermFragmentCourseList);
        final int term_id = getArguments().getInt("term_id");
        DBHandler db = new DBHandler(view.getContext());

        final Term term = db.get_term(term_id);
        final ArrayList<Course> courses = term.get_courses();

        // set term title
        TextView term_title_view = (TextView) view.findViewById(R.id.TermFragmentTitle);
        term_title_view.setText(term.get_title());

        // set term range
        TextView term_dates_label = (TextView) view.findViewById(R.id.TermFragmentDates);
        term_dates_label.setText(term.get_term_dates());

        // set courses
        for (final Course course : courses) {
            Button cbutton = new Button(view.getContext());
            cbutton.setText(course.get_title());
            cbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_course(course.get_course_id());
                }
            });
            courses_layout.addView(cbutton);
        }

        Button button= (Button) view.findViewById(R.id.TermFragmentDeleteTerm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete term stub
                if (courses.size()>0) {
                    // make toast notification

                    CharSequence text = "You can not delete a term that has courses in it!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(view.getContext(), text, duration);
                    toast.show();

                } else {
                    // delete term
                    // send me to list of terms
                    DBHandler db = new DBHandler(view.getContext());
                    db.delete_term(term_id);
                    // show toast that term was deleted

                    CharSequence text = term.get_title()+" Deleted";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(view.getContext(), text, duration);
                    toast.show();
                    show_term_list();
                }
            }
        });

        return view;
    }

    public void show_term_list() {
        Fragment fragment = null;
        Class fragmentClass = TermListFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
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
