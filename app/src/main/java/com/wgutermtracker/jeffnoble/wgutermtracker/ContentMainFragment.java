package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContentMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentMainFragment newInstance(String param1, String param2) {
        ContentMainFragment fragment = new ContentMainFragment();
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
        View view = inflater.inflate(R.layout.fragment_content_main, container, false);

        // set current courses
        LinearLayout courseListLayout = (LinearLayout) view.findViewById(R.id.currentCoursesView);

        DBHandler db = new DBHandler(view.getContext());

        ArrayList<Course> courses = db.get_all_current_courses();

        for (final Course course : courses) {
            Button cbutton = new Button(view.getContext());
            cbutton.setText(course.get_title());
            cbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_course(course.get_course_id());

                }
            });
            courseListLayout.addView(cbutton);
        }

        // see latest notes
        LinearLayout notes_layout = (LinearLayout) view.findViewById(R.id.latestNotes);

        ArrayList<Note> notes  = db.get_most_recent_notes();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean is_img_shown = sharedPref.getBoolean("is_img_shown", true);

        for (Note note : notes) {
            TextView note_text = new TextView(view.getContext());
            note_text.setText(note.get_note_string());
            note_text.setLayoutParams(params);
            note_text.setGravity(Gravity.TOP);
            notes_layout.addView(note_text);

            if (note.get_photos().size()>0 && is_img_shown) {
                ArrayList<String> photos = note.get_photos();
                Uri image_note = Uri.parse(photos.get(0));
                LinearLayout img_layout = new LinearLayout(view.getContext());

                ImageView img = new ImageView(view.getContext());
                img.setImageURI(image_note);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setAdjustViewBounds(true);
                img.setRotation(90);

                img_layout.addView(img);
                notes_layout.addView(img_layout);
            }

            TextView spacer = new TextView(view.getContext());
            spacer.setText("---");
            spacer.setGravity(Gravity.TOP);
            spacer.setLayoutParams(params);
            notes_layout.addView(spacer);

        }
        if (!is_notifications_checked_today) {
            check_for_due_assignments(view);
        }

        return view;
    }
    boolean is_notifications_checked_today = false;
    private void check_for_due_assignments(View view) {
        is_notifications_checked_today=true;

        DBHandler db = new DBHandler(view.getContext());
        ArrayList<Course> coursesdue = db.get_courses_due_today();
        for (Course course : coursesdue) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(view.getContext())
                            .setSmallIcon(R.drawable.ic_add_circle_outline_black_24dp)
                            .setContentTitle("Course Due for completion")
                            .setContentText(course.get_title()+" is due to be completed today.");
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) view.getContext().getSystemService(view.getContext().NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

        ArrayList<Assessment> assessments = db.get_assessments_due();
        for (Assessment assessment : assessments) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(view.getContext())
                            .setSmallIcon(R.drawable.ic_add_circle_outline_black_24dp)
                            .setContentTitle("Assessment Due for completion")
                            .setContentText(assessment.get_title()+" is due to be completed today.");
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) view.getContext().getSystemService(view.getContext().NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

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
