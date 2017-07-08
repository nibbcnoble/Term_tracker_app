package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        final View view = inflater.inflate(R.layout.fragment_course, container, false);

        final long course_id = getArguments().getLong("course_id");

        DBHandler db = new DBHandler(view.getContext());
        final Course course = db.get_course(course_id);

        LinearLayout course_layout = (LinearLayout) view.findViewById(R.id.CourseFragmentLayout);
        TextView course_title_text = (TextView) view.findViewById(R.id.CourseFragmentTitle);
        course_title_text.setText(course.get_title()+" ("+course.get_status()+")");
        TextView start_date = (TextView) view.findViewById(R.id.CourseFragmentStartDateText);
        start_date.setText(course.get_course_start_formatted());
        TextView end_date = (TextView) view.findViewById(R.id.CourseFragmentEndDateText);
        end_date.setText(course.get_course_end_formatted());

        LinearLayout notes_list_layout = (LinearLayout) view.findViewById(R.id.CourseFragmentNotesLayout);

        ArrayList<Note> notes = course.get_notes();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        for (final Note note : notes) {
            TextView note_text = new TextView(view.getContext());
            note_text.setText(note.get_note_string());
            note_text.setLayoutParams(params);
            note_text.setGravity(Gravity.TOP);
            notes_list_layout.addView(note_text);

            final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = note.get_note_string();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Course Note");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            if (note.get_photos().size()>0) {
                ArrayList<String> photos = note.get_photos();
                Uri image_note = Uri.parse(photos.get(0));
                LinearLayout img_layout = new LinearLayout(view.getContext());

                ImageView img = new ImageView(view.getContext());
                img.setImageURI(image_note);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setAdjustViewBounds(true);
                img.setRotation(90);

                img_layout.addView(img);
                notes_list_layout.addView(img_layout);

                sharingIntent.putExtra(Intent.EXTRA_STREAM, image_note);
            }
            Button shareNote = new Button(view.getContext());
            shareNote.setText("Share Note");
            shareNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
            shareNote.setGravity(Gravity.CENTER_HORIZONTAL);
            shareNote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
            notes_list_layout.addView(shareNote);
            TextView spacer = new TextView(view.getContext());
            spacer.setText("---");
            spacer.setGravity(Gravity.TOP);
            spacer.setLayoutParams(params);
            notes_list_layout.addView(spacer);


        }

        LinearLayout assessments_layout = (LinearLayout) view.findViewById(R.id.CourseFragmentAssessmentLayout);
        ArrayList<Assessment> assessments_o = course.get_all_objective_assessments();

        for (final Assessment assessment : assessments_o) {
            Button a_btn = new Button(view.getContext());
            a_btn.setText(assessment.get_title());
            a_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_a(assessment.get_id());

                }
            });
            assessments_layout.addView(a_btn);
        }

        LinearLayout performance_layout = (LinearLayout) view.findViewById(R.id.CourseFragmentPerformanceLayout);

        ArrayList<Assessment> assessments_p = course.get_all_performance_assessments();

        for (final Assessment assessment : assessments_p) {

            Button a_btn = new Button(view.getContext());
            a_btn.setText(assessment.get_title());
            a_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_a(assessment.get_id());

                }
            });
            performance_layout.addView(a_btn);
        }

        LinearLayout mentor_layout = (LinearLayout) view.findViewById(R.id.CourseMentorLayoutList);

        ArrayList<CourseMentor> mentors = course.get_course_mentors();

        for (CourseMentor mentor : mentors) {
            TextView mentor_name = new TextView(view.getContext());
            mentor_name.setText(mentor.get_name());
            mentor_layout.addView(mentor_name);
            TextView mentor_phone = new TextView(view.getContext());
            mentor_phone.setText(mentor.get_phone_number());
            mentor_layout.addView(mentor_phone);
            TextView mentor_email = new TextView(view.getContext());
            mentor_email.setText(mentor.get_email_address());
            mentor_layout.addView(mentor_email);

            TextView spacer = new TextView(view.getContext());
            spacer.setText("---");
            mentor_layout.addView(spacer);
        }


        Button add_note_btn = (Button) view.findViewById(R.id.CourseFragmentAddNote);
        add_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteFragment(course_id);

            }
        });
        Button add_assessment_btn = (Button) view.findViewById(R.id.CourseFragmentAssessmentAdd);
        add_assessment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAssessmentFragment(course_id);

            }
        });
        Button add_mentor_btn = (Button) view.findViewById(R.id.CourseFragmentAddMentor);
        add_mentor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMentorFragment(course_id);

            }
        });
        Button edit_course_btn = (Button) view.findViewById(R.id.editCourseButton);
        edit_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_course_edit(course_id);

            }
        });
        Button delete_course_btn = (Button) view.findViewById(R.id.deleteCourseButton);
        delete_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Course")
                        .setMessage("Do you really want to delete this course?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBHandler db = new DBHandler(view.getContext());
                                db.delete_course(course_id);
                                Toast.makeText(view.getContext(), "Course Deleted", Toast.LENGTH_SHORT).show();
                                show_course_list();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });



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

    public void show_course_list() {
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

    public void show_course_edit(long course_id) {
        Fragment fragment = null;
        Class fragmentClass = CourseEditFragment.class;

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

    public void openAddMentorFragment(long course_index) {
        Fragment fragment = null;
        Class fragmentClass = AddMentorFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("course_id",course_index);
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void openAddAssessmentFragment(long course_index) {
        Fragment fragment = null;
        Class fragmentClass = AddAssessmentFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("course_id",course_index);
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void openAddNoteFragment(long course_index) {
        Fragment fragment = null;
        Class fragmentClass = AddNoteFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("course_id",course_index);
        bundle.putBoolean("is_course",true);
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}
