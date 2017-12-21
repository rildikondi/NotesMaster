package com.example.amarildo.notesmaster;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = 10;

    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    FragmentTransaction mCurTransaction;
    FragmentManager mFragmentManager;


    public MyPagerAdapter(FragmentManager fragmentManager)
    {

        super(fragmentManager);
        mCurTransaction = fragmentManager.beginTransaction();
        mFragmentManager = fragmentManager;
    }

    // Returns total number of pages
    @Override
    public int getCount()
    {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position)
    {

        return PageFragment.newInstance(position);
    }

    public  void increaseNumber(){
        NUM_ITEMS = NUM_ITEMS + 10;
        notifyDataSetChanged();
    }

}

