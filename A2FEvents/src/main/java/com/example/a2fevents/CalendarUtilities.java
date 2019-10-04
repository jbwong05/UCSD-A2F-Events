package com.example.a2fevents;

import java.util.Calendar;

public class CalendarUtilities {

    private static final int HOUR_OFFSET = 12;

    public static int convertMonth(String month) {

        // Converts month from text to Calendar constant equivalent
        switch(month) {
            case "January":
            case "Jan":
                return Calendar.JANUARY;
            case "February":
            case "Feb":
                return Calendar.FEBRUARY;
            case "March":
            case "Mar":
                return Calendar.MARCH;
            case "April":
            case "Apr":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "June":
            case "Jun":
                return Calendar.JUNE;
            case "July":
            case "Jul":
                return Calendar.JULY;
            case "August":
            case "Aug":
                return Calendar.AUGUST;
            case "September":
            case "Sep":
                return Calendar.SEPTEMBER;
            case "October":
            case "Oct":
                return Calendar.OCTOBER;
            case "November":
            case "Nov":
                return Calendar.NOVEMBER;
            case "December":
            case "Dec":
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    public static boolean comesBefore(int firstMonth, int firstDay, int secondMonth, int secondDay) {
        // New year case one month in advance
        if(secondMonth == Calendar.JANUARY && firstMonth == Calendar.JANUARY) {
            return false;

        } else {
            return firstMonth < secondMonth || (firstMonth == secondMonth && firstDay < secondDay);
        }
    }

    public static int adjustTime(int originalTime, int ampm) {

        // Anything AM but 12 or 12 PM
        if((originalTime != 12 && ampm == Calendar.AM) || (originalTime == 12 && ampm == Calendar.PM)) {
            return originalTime;

            // Anything PM but 12
        } else if(originalTime != 12 && ampm == Calendar.PM) {
            return originalTime + HOUR_OFFSET;

            // 12 AM
        } else {
            return 0;
        }
    }
}