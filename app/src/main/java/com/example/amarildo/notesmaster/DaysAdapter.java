package com.example.amarildo.notesmaster;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;


public class DaysAdapter extends BaseAdapter{

    private final Context _context;
    private final List<String> list;

    private static final int DAY_OFFSET = 1;
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private int currentDayOfMonth;
    private int currentWeekDay;

    private final HashMap<String, Integer> eventsPerMonthMap;
    Calendar calendar;
    int textViewResourceId;

    String sDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    OnGridItemSelectedListener mCallback;


    public interface OnGridItemSelectedListener {

        void onArticleSelected(String clickedDate);
    }


    // Days in Current Month
    public DaysAdapter(Context context, int textViewResourceId, int month, int year,OnGridItemSelectedListener callback ) {

        super();
        this._context = context;
        this.list = new ArrayList<>();
        this.textViewResourceId = textViewResourceId;
        this.mCallback = callback;
        calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

        // Print Month
        printMonth(month, year);

        // Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    public String getItem(int position) {
        return list.get(position);
    }



    @Override
    public int getCount() {
        return list.size();
    }

    private void printMonth(int mm, int yy) {

        int trailingSpaces;
        int daysInPrevMonth;
        int prevMonth;
        int prevYear;
        int nextMonth;
        int nextYear;

        int currentMonth = mm - 1;
        int daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 0);

        if(currentMonth == 11){

            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        }else if(currentMonth == 0){

            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
        }else{

            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
        }

        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1){

            ++daysInMonth;
        }

        // Trailing Month days
        for(int i = 0; i < trailingSpaces; i++) {

            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        // Current Month Days
        for(int i = 1; i <= daysInMonth; i++) {

            if(i == getCurrentDayOfMonth() && yy == calendar.get(Calendar.YEAR) )

                list.add(String.valueOf(i) + "-BLUE" + "-" + currentMonth + "-" + yy);
            else

                list.add(String.valueOf(i) + "-WHITE" + "-" + currentMonth + "-" + yy);
        }

        // Leading Month days
        for(int i = 0; i < list.size() % 7; i++) {

            list.add(String.valueOf(i + 1) + "-GREY" + "-" + nextMonth + "-" + nextYear);
        }
    }

    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {

        return new HashMap<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        DayHolder holder = null;

        if(row == null) {

            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);

            holder = new DayHolder();

            holder.nameView = (TextView) row.findViewById(R.id.num_events_per_day);
            holder.buttonView = (Button) row.findViewById(R.id.calendar_day_gridcell);

            row.setTag(holder);

        }else{

            holder = (DayHolder) row.getTag();
        }


        Integer rowPosition = position;
        holder.buttonView.setTag(rowPosition);


        // ACCOUNT FOR SPACING

        String[] day_color = list.get(position).split("-");
        final String theday = day_color[0];
        final String themonth = day_color[2];
        final String theyear = day_color[3];

        if((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {

            if(eventsPerMonthMap.containsKey(theday)) {

                Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                holder.nameView.setText(numEvents.toString());
            }
        }

        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Date sot = new Date(Integer.parseInt(theday),Integer.parseInt(themonth),Integer.parseInt(theyear));

                sDate = theday + "-" + themonth + "-" + theyear;

                mCallback.onArticleSelected(sDate);
            }
        });

        // Set the Day Button

        holder.buttonView.setText(theday);
        holder.buttonView.setTag(theday + "-" + themonth + "-" + theyear);

        if(day_color[1].equals("GREY"))  holder.buttonView.setTextColor(Color.LTGRAY);
        if(day_color[1].equals("WHITE"))  holder.buttonView.setTextColor(Color.WHITE);
        if(day_color[1].equals("BLUE"))  holder.buttonView.setTextColor(Color.BLUE);

        return row;
    }


    private int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {

        this.currentDayOfMonth = currentDayOfMonth;
    }

    private void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    private int getCurrentWeekDay() {
        return currentWeekDay;
    }


  /*  View.OnClickListener dayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //mCallback.onArticleSelected(position);

            //Integer viewPosition = (Integer) v.getTag();
            //Place p = mData[viewPosition];

            Toast.makeText(v.getContext(), "View clicked", Toast.LENGTH_SHORT).show();
        }
    };
*/


    private static class DayHolder {

        TextView nameView;
        Button buttonView;
    }



}

