package com.wgutermtracker.jeffnoble.wgutermtracker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.wgutermtracker.jeffnoble.wgutermtracker.dbmanager.DBHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TermCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermCreateFragment extends Fragment implements DBCompleteInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Activity activity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TermCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermCreateFragment newInstance(String param1, String param2) {
        TermCreateFragment fragment = new TermCreateFragment();
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

        final View view = inflater.inflate(R.layout.fragment_term_create, container, false);

        Button button= (Button) view.findViewById(R.id.CreateTerm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_term(view);

            }
        });

        return view;
    }

    private Term add_new_term(View view) {

        EditText termTitleView= (EditText) view.findViewById(R.id.TermCreateTitle);
        String term_title = termTitleView.getText().toString();

        DatePicker start_date_picker = (DatePicker) view.findViewById(R.id.TermCreateStartDate);
        Date start_date = get_date_from_date_picker(start_date_picker);

        DatePicker end_date_picker = (DatePicker) view.findViewById(R.id.TermCreateEndDate);
        Date end_date = get_date_from_date_picker(end_date_picker);
        Term t = new Term(term_title);
        System.out.println("END DATE"+end_date.toString());
        t.set_term_timeline(start_date,end_date);
        DBHandler db = new DBHandler(activity);
        db.insert_term(t, this);

        return t;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            activity=(Activity) context;
        }

    }

    @Override
    public void openFragment() {
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
