package com.example.amarildo.notesmaster;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;


public class AddNoteFragment extends DialogFragment {

    int mNum;
    int width;
    int height;

    EditText enterNote;
    TextView tv_mytitle;
    Button discardNote;
    Button addToSave;

    String clickedDay = "";
    String message = "";

    String sDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy", Locale.ENGLISH);
    Calendar _calendar;
    public List<Note> notes = new ArrayList<>();

    private OnAddNoteListener newAddNoteListener;

    public interface OnAddNoteListener {

        void closeAddNoteFragment();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof AddNoteFragment.OnAddNoteListener) {
            newAddNoteListener = (AddNoteFragment.OnAddNoteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewNoteListener");
        }

    }


    public void setClickedDate(String clickedDay){

       this.clickedDay = clickedDay;
    }

    public void showSavedNote(String clickedDay,String messg){
        this.clickedDay= clickedDay;
        this.message = messg;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        //if (getDialog() == null) return;
        //width = getActivity().getResources().getDisplayMetrics().widthPixels;
        //height = getActivity().getResources().getDisplayMetrics().heightPixels;
        //int dialogWidth = 700; // specify a value here
        //int dialogHeight = 1; // specify a value here
        // getDialog().getWindow().setLayout(width, height-100);
    }


    static AddNoteFragment newInstance(int num) {

        AddNoteFragment f = new AddNoteFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        enterNote = view.findViewById(R.id.noteFieldEditText);
        discardNote = view.findViewById(R.id.dicardButton);
        addToSave = view.findViewById(R.id.addtoSaveButton);
        tv_mytitle = view.findViewById(R.id.myTitle);


        if(message != "") {

            enterNote.setText(message, TextView.BufferType.EDITABLE);
            discardNote.setText("Cancel");
            addToSave.setText("Save");
            tv_mytitle.setText("Edit Note");
        }

        View.OnClickListener listener = new View.OnClickListener() {

            public void onClick(View view) {

                if (view == addToSave) {

                    if(message != "") {

                        notes = Note.find(Note.class, "clicked_date = ? and message = ?", clickedDay, message);

                        notes.get(0).delete();

                        String contentNote = enterNote.getText().toString();
                        _calendar = Calendar.getInstance(Locale.getDefault());
                        sDate = formatter.format(_calendar.getTime());

                        Note myNote = new Note(contentNote, sDate, clickedDay);
                        myNote.save();

                        List<Note> notes = Note.find(Note.class, "clicked_date = ?", clickedDay);
                        Toast.makeText(getContext(), notes.get(notes.size() - 1).getMessage(), Toast.LENGTH_SHORT).show();

                        closeFragment();
                    }
                    else
                    {

                        String contentNote = enterNote.getText().toString();
                        _calendar = Calendar.getInstance(Locale.getDefault());
                        sDate = formatter.format(_calendar.getTime());

                        Note myNote = new Note(contentNote, sDate, clickedDay);
                        myNote.save();

                        List<Note> notes = Note.find(Note.class, "clicked_date = ?", clickedDay);
                        //Toast.makeText(getContext(), notes.get(notes.size() - 1).getMessage(), Toast.LENGTH_SHORT).show();

                        closeFragment();
                    }
                }
                if(view == discardNote){

                    //newAddNoteListener.closeAddNoteFragment();
                    closeFragment();
                }
            }
        };

        addToSave.setOnClickListener(listener);
        discardNote.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("num", mNum);
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void closeFragment(){
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialogAddNote");

        if (prev != null) {

            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}