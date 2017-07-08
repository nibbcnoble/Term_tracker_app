package com.wgutermtracker.jeffnoble.wgutermtracker;

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
 * {@link AssessmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AssessmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssessmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentFragment newInstance(String param1, String param2) {
        AssessmentFragment fragment = new AssessmentFragment();
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
        // Inflate the layout for this
        final View view = inflater.inflate(R.layout.fragment_assessment, container, false);
        final long a_id = getArguments().getLong("a_id");

        DBHandler db = new DBHandler(view.getContext());
        System.out.println("Assessment ID:"+a_id);
        final Assessment assessment = db.get_assessment(a_id);

        // set title
        TextView a_title = (TextView) view.findViewById(R.id.assessmentFragmentTitle);
        a_title.setText(assessment.get_title());

        // set type
        TextView a_status = (TextView) view.findViewById(R.id.assessmentFragmentType);
        a_status.setText(assessment.get_assessment_type());

        // set goal date
        TextView a_goal = (TextView) view.findViewById(R.id.assessmentFragmentGoalDate);
        a_goal.setText(assessment.get_goal_formatted());

        // set notes
        LinearLayout notes_list_layout = (LinearLayout) view.findViewById(R.id.AssessmentNotesLayout);

        ArrayList<Note> notes = assessment.get_notes();
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

        // set add note button
        Button add_note_btn = (Button) view.findViewById(R.id.assessmentFragmentAddNoteBtn);
        add_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteFragment(a_id);

            }
        });
        // set edit button
        Button edit_assessment_btn = (Button) view.findViewById(R.id.assessmentFragmentEditBtn);
        edit_assessment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAssessmentEdit(a_id);

            }
        });
        // set delete button
        Button delete_assessment_button = (Button) view.findViewById(R.id.assessmentFragmentDeleteBtn);
        delete_assessment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Assessment")
                        .setMessage("Do you really want to delete this assessment?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DBHandler db = new DBHandler(view.getContext());
                                db.delete_assessment(a_id);
                                Toast.makeText(view.getContext(), "Assessment Deleted", Toast.LENGTH_SHORT).show();
                                show_assessment_list();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        return view;
    }

    public void openAddNoteFragment(long a_id) {
        Fragment fragment = null;
        Class fragmentClass = AddNoteFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("a_id",a_id);
        bundle.putBoolean("is_course",false);
        // set Fragmentclass Arguments

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void openAssessmentEdit(long a_id) {
        Fragment fragment = null;
        Class fragmentClass = AssessmentEditFragment.class;

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

    public void show_assessment_list() {
        Fragment fragment = null;
        Class fragmentClass = AssessmentListFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
