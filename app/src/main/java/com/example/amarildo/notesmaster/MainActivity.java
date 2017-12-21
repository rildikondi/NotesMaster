package com.example.amarildo.notesmaster;


import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements PageFragment.OnFragmentNoteListener,
        TodayNoteFragment.OnNewNoteListener, AddNoteFragment.OnAddNoteListener{

    private int mStackLevel = 0;

    ViewPager viewPager;
    MyPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

         viewPager = findViewById(R.id.pager);
        //viewPager.setCurrentItem(Integer.MAX_VALUE / 2);


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == adapterViewPager.getCount() - 2){
                     adapterViewPager.increaseNumber();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

 /*   public void loadDetailsScreen(Fragment selectedPageFragment) {

        // mainLinearLayout.animate().alpha((float)0.1).setDuration(500);
        getSupportFragmentManager().beginTransaction().replace(R.id.hiddenFragFrameLayout, selectedPageFragment)
                .addToBackStack(selectedPageFragment.getId() + "")
                .commit();
    }*/

    public void notifyMyAdapter(){
        adapterViewPager.notifyDataSetChanged();
    }

    void showDialog(String clickedDay){

        mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogAddNote");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        TodayNoteFragment newFragment = TodayNoteFragment.newInstance(mStackLevel);
        newFragment.setDates(clickedDay);

        //newFragment.setDates(currentDate,clickedDay);

        newFragment.show(ft, "dialogTodayNote");
    }

    void showDialog2(String clickedDay) {

        mStackLevel++;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogTodayNote");

        if (prev != null) {

            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        //DialogFragment newFragment = AddNoteFragment.newInstance(mStackLevel);

        AddNoteFragment newFragment = AddNoteFragment.newInstance(mStackLevel);
        newFragment.setClickedDate(clickedDay);
        newFragment.show(ft, "dialogAddNote");
    }

    public void showDialog3(String clickedDate,String message){

        mStackLevel++;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogTodayNote");

        if (prev != null) {

            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        //DialogFragment newFragment = AddNoteFragment.newInstance(mStackLevel);

        AddNoteFragment newFragment = AddNoteFragment.newInstance(mStackLevel);
        newFragment.showSavedNote(clickedDate, message);
        newFragment.show(ft, "dialogAddNote");


    }

    @Override
    public void onFragmentNoteInteraction(String clickedDay ) {

        Toast.makeText(this, clickedDay , Toast.LENGTH_SHORT).show();
        showDialog(clickedDay);
    }


    @Override
    public void onNewNoteInteraction(String clickedDay) {
        showDialog2(clickedDay);
    }

    @Override
    public void showSavedNote(String clickedDate,String message) {

        showDialog3(clickedDate, message);
    }

    @Override
    public void closeTodayFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogTodayNote");

        if (prev != null) {

            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }

        ft.addToBackStack(null);
    }

    @Override
    public void closeAddNoteFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialogAddNote");

        if (prev != null) {

            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }

        ft.addToBackStack(null);
    }
}
