package com.example.amarildo.notesmaster;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;

import java.util.ArrayList;
import java.util.List;


public class TodayNoteFragment extends DialogFragment {


    Button btnOK;
    FloatingActionButton btnAdd;
    public ListView lvNotes;
    TextView tvStatus;
    int mNum;
    int width;
    int height;


    public String currentDate;
    String clickedDay;

    public int pozicioni;

    public ArrayAdapter mArrayAdapter;
    public List<Note> notes = new ArrayList<>();

    public void setDates(String clickedDay){
        this.clickedDay = clickedDay;
    }

    private OnNewNoteListener newNoteListener;

    public interface OnNewNoteListener {

        // TODO: Update argument type and name
        void onNewNoteInteraction(String clickedDay);

        void showSavedNote(String currentDay, String message);

        void closeTodayFragment();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnNewNoteListener) {
            newNoteListener = (OnNewNoteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewNoteListener");
        }


    }

    @Override
    public void onStart()
    {
        super.onStart();

     /* // safety check
        if (getDialog() == null)
            return;

        width = getActivity().getResources().getDisplayMetrics().widthPixels;
        height = getActivity().getResources().getDisplayMetrics().heightPixels;

        //int dialogWidth = 700; // specify a value here
        //int dialogHeight = 1; // specify a value here

        getDialog().getWindow().setLayout(width, height-100);*/
    }



    public TodayNoteFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodayNoteFragment newInstance(int num) {
        TodayNoteFragment fragment = new TodayNoteFragment();
        Bundle args = new Bundle();

        args.putInt("num", num);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            clickedDay = savedInstanceState.getString("clickedDay");
           // Toast.makeText(getActivity(),clickedDay, Toast.LENGTH_SHORT).show();
            notes = Note.find(Note.class, "clicked_date = ?", clickedDay);

        }else{


            notes = Note.find(Note.class, "clicked_date = ?", clickedDay);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_note, container, false);

        btnAdd = view.findViewById(R.id.newNoteButton);
        btnOK = view.findViewById(R.id.okButton);
        lvNotes = view.findViewById(R.id.notesListView);
        tvStatus = view.findViewById(R.id.stausOfNotes);


        //Toast.makeText(getContext(), notes.get(0).getMessage(), Toast.LENGTH_SHORT).show();

        final List dateTexts = new ArrayList<String>();

        for(int i = 0; i < notes.size(); i++){

            dateTexts.add(notes.get(i).getCurrentDate() + " " + notes.get(i).getMessage());
        }
        mArrayAdapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,dateTexts);

        lvNotes.setAdapter(mArrayAdapter);



        if(notes.size() > 0){

            tvStatus.setVisibility(View.INVISIBLE);
        }

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getActivity(),notes.get(i).getMessage(),Toast.LENGTH_SHORT).show();

                newNoteListener.showSavedNote(notes.get(i).getClickedDate(),notes.get(i).getMessage());
            }
        });

        lvNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                pozicioni = i;
                currentDate = notes.get(pozicioni).getCurrentDate();

                Toast.makeText(getActivity(), notes.get(pozicioni).getCurrentDate(),Toast.LENGTH_SHORT ).show();


                new AlertDialog.Builder(getActivity())
                        .setItems(R.array.choises_array, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which){

                                if(which == 1){

                                    notes.get(pozicioni).delete();
                                    dateTexts.remove(pozicioni);
                                    mArrayAdapter.notifyDataSetChanged();
                                }
                                if (which == 0){

                                    newNoteListener.showSavedNote(notes.get(pozicioni).getClickedDate(),notes.get(pozicioni).getMessage());
                                }
                            }
                        }).show();

                return true;
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View view) {

                if (view == btnAdd) {
                    if (newNoteListener != null) {

                        newNoteListener.onNewNoteInteraction(clickedDay);
                    }
                }
                if (view == btnOK){

                    newNoteListener.closeTodayFragment();
                }

            }
        };

        btnAdd.setOnClickListener(listener);
        btnOK.setOnClickListener(listener);

        return view ;
    }

    @Override
    public void onDetach(){

        super.onDetach();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("clickedDay", clickedDay);
    }


 /*   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clickedDay = savedInstanceState.getString("clickedDay");

    }*/


    @Override
    public void onResume() {

        super.onResume();

        mArrayAdapter.notifyDataSetChanged();
    }
}
