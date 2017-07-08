package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView imageForNote;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNoteFragment newInstance(String param1, String param2) {
        AddNoteFragment fragment = new AddNoteFragment();
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

        final boolean is_course_note = getArguments().getBoolean("is_course");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        imageForNote = (ImageView) view.findViewById(R.id.pictureTakenImageView);
        imageForNote.setScaleType(ImageView.ScaleType.FIT_XY);
        imageForNote.setAdjustViewBounds(true);
        imageForNote.setRotation(90);

        ImageButton take_photo_button = (ImageButton) view.findViewById(R.id.attachPhotoButton);
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open camera
                dispatchTakePictureIntent(view);
            }
        });
        if (is_course_note) {
            final long course_id = getArguments().getLong("course_id");
            Button add_note_button = (Button) view.findViewById(R.id.addNoteButton);
            add_note_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_note(view,course_id,true);

                }
            });
        } else {
            final long a_id = getArguments().getLong("a_id");
            Button add_note_button = (Button) view.findViewById(R.id.addNoteButton);
            add_note_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add_note(view,a_id,false);

                }
            });
        }

        return view;
    }

    private void add_note(View view, long id, boolean is_course) {

        EditText note_text= (EditText) view.findViewById(R.id.AddNoteText);
        String note_text_value = note_text.getText().toString();
        Note note = new Note(note_text_value);
        // add photo from where it is stored


        if (uriSavedImage!=null) {
            String photo_location = uriSavedImage.toString();
            note.add_photo(photo_location);

        }

        DBHandler db = new DBHandler(view.getContext());
        if (is_course) {
            db.insert_note(note, id);
            open_course(id);
        } else {
            db.insert_a_note(note,id);
            open_assessment(id);
        }

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri uriSavedImage= null;

    private void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                uriSavedImage = FileProvider.getUriForFile(view.getContext(), view.getContext().getApplicationContext().getPackageName() + ".provider", createImageFile(view));
            } catch (IOException e) {
                e.printStackTrace();
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(takePictureIntent, 1);
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

    public void open_assessment(long a_id) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //uriSavedImage

                imageForNote.setImageURI(uriSavedImage);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile(View view) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = view.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
