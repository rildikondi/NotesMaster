package com.example.amarildo.notesmaster;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PipedOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class PageFragment  extends Fragment implements DaysAdapter.OnGridItemSelectedListener{

    private int mNum;
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private DaysAdapter daysAdapter;
    private GridView calendarView;
    private Calendar _calendar;
    private int month, year;
    TextView monthName;
    private OnFragmentNoteListener mListener;


    public interface OnFragmentNoteListener {
        // TODO: Update argument type and name
        void onFragmentNoteInteraction(String clikedDate);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentNoteListener) {
            mListener = (OnFragmentNoteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // things to do to destroy the interface
    }

    public static PageFragment newInstance(int num) {


        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

        //List<EventObjects> mEvents = new ArrayList<EventObjects>();
        //Toast.makeText(getContext(),months[month-1], Toast.LENGTH_SHORT).show();
        // Toast.makeText(getContext(), ""+month+" / "+ year, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();

        /*monthName = getActivity().findViewById(R.id.monthNameTextView);
        monthName.setText("Fragment #" + mNum);*/
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        calendarView = view.findViewById(R.id.gridview);

        // Set the month value
        monthName = view.findViewById(R.id.monthNameTextView);

        //Calendar cali = (Calendar) formatter.getCalendar();

        _calendar = Calendar.getInstance(Locale.getDefault());

        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        int a = month + mNum;
        int b = a%12;


        if(a < 1){

            month = 12;
            year--;
        }
        else if(a >= 12 && b == 0){

            month = 12;
            year = (year + a/12)-1;
        }
        else if(a > 12 && b == 1)
        {

            year = year + (a/12);
            month = 1;
        }
        else if(a >12 && b > 1)
        {

            year = year + (a/12);
            month = b;
        }else if(a >= 1 && a <12){

            month = a;
        }

        monthName.setText(months[month-1] + " / " + year);

        daysAdapter = new DaysAdapter(this.getContext(),R.id.calendar_day_gridcell,month, year, this);

        calendarView.setAdapter(daysAdapter);
        daysAdapter.notifyDataSetChanged();


        calendarView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                //Toast.makeText(getActivity().getApplicationContext(), "From grid adapterListener", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void setGridCellAdapterToDate(int month, int year) {

        daysAdapter = new DaysAdapter(getContext(), R.id.calendar_day_gridcell, month, year,this);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        //currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        daysAdapter.notifyDataSetChanged();

        calendarView.setAdapter(daysAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onArticleSelected(String clickedDate) {

        //Toast.makeText(getActivity(),theDay + " /" + theMonth + " /" + theYear, Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onFragmentNoteInteraction(clickedDate);
        }

    }
}