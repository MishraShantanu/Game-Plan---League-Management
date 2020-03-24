package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Class for representing the time a game will be played
 */
public class GameTime implements Comparable, Serializable {

    /** Calendar storing years, months, days, hours, minutes, seconds when Game will occur */
    private Calendar calendar;

    /** Epoch time in milliseconds represented by this GameTime */
    private Long timeInMilliseconds;

    /**
     * GameTime constructor
     * @param year: int, the year the game will take place e.g. 2020
     * @param month: int, 1-12, the month the game will take place, a month of 1 represents January and so on
     * @param day: int, the day of the month the game will take place, e.g. 15, cannot be greater than 31
     * @param hour: int, the hour of the day the game will take place 0-23, where 0 represents 12:00 AM
     * @param minutes: int, the minutes of the hour the game will take place, 0-59
     * @throws IllegalArgumentException: if any of the above arguments are outside of their specified bounds
     */
    public GameTime(int year, int month, int day, int hour, int minutes) throws IllegalArgumentException{
        // months normally range from 0-11, change the input bounds
        month--;
        if(month < 0 || month > 11){
            throw new IllegalArgumentException("GameTime: month out of bounds, valid bounds: 1-12" +
                    " actual month: " + month);
        }
        if(day > 31){
            throw new IllegalArgumentException("GameTime: days out of bounds, days specified greater than 31");
        }
        if(hour < 0 || hour > 23){
            throw new IllegalArgumentException("GameTime: hour out of bounds, valid bounds: 0-23" +
                    " actual hour: " + hour);
        }
        if(minutes < 0 || minutes > 59){
            throw new IllegalArgumentException("GameTime: minutes out of bounds, valid bounds: 0-59" +
                    " actual minutes: " + minutes);
        }
        // create and set fields of underlying calendar
        this.calendar = Calendar.getInstance();
        // set the seconds field to 0 as a Game cannot be scheduled for a date so specific
        this.calendar.set(year,month,day,hour,minutes,0);
        // set milliseconds to 0
        this.calendar.set(Calendar.MILLISECOND,0);
        this.timeInMilliseconds = this.calendar.getTimeInMillis();
    }

    /**
     * Blank constructor required by firebase to read GameTime objects from the database
     */
    public GameTime(){
    }

    /**
     * Returns the epoch time in milliseconds that this GameTime represents
     * @return long time in milliseconds represented by this GameTime
     */
    public Long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    /**
     * Since we cannot store a Calendar object on our database, we instead store the epoch time represented
     * by that calendar and can convert this time in milliseconds back to a Calendar object we can use
     */
    private void recreateCalendar(){
        if(this.calendar==null){
            // this GameTime has been read in from the database without its Calendar, recreate this underlying Calendar
            this.calendar = Calendar.getInstance();
            this.calendar.setTimeInMillis(this.timeInMilliseconds);
        }
    }

    /**
     * Returns an array specifying the time the game will take place
     * @return int array of the form [year, month, day, hour, minute] specifying the date and time
     * the game takes place
     */
    /*
    public int[] getTimeArray(){
        // months are from 0-11, but we expect months to be from 1-12, increment months
        int[] timeArray = {this.time.get(Calendar.YEAR), this.time.get(Calendar.MONTH)+1, this.time.get(Calendar.DATE),
                this.time.get(Calendar.HOUR_OF_DAY), this.time.get(Calendar.MINUTE)};
        return timeArray;
    }
    */

    /**
     * Compares this GameTime to another GameTime, this will produce a IllegalArgumentException if other isn't a GameTime
     * @param other: other GameTime object to compare this GameTime to
     * @return 0 if this and other represent the same time, < 0 if this represent a time before the time
     * represented by other, > 0 if this represents a time later than other
     */
    @Override
    public int compareTo(Object other){
        recreateCalendar();
        if(!(other instanceof GameTime)){
            // other is an invalid type
            throw new IllegalArgumentException("Cannot compare a GameTime object to an object of type: " + other.getClass().getName());
        }
        GameTime otherGameTime = (GameTime) other;
        // compare underlying calendars
        return this.calendar.compareTo(otherGameTime.calendar);
    }

    /**
     * Checks if the GameTime object specifies a time that is in the future
     * @return true if the GameTime specifies a time in the future, false otherwise
     */
    public boolean isInFuture(){
        recreateCalendar();
        Calendar now = Calendar.getInstance();
        return this.calendar.after(now);
    }

    /**
     * determines if another object is equal to this
     * @param other: Object being compared against this
     * @return true if other is equal to this game time, ie both other and this represent the same time
     */
    @Override
    public boolean equals(Object other){
        recreateCalendar();
        if(other instanceof GameTime){
            GameTime otherGameTime = (GameTime) other;
            return this.compareTo(otherGameTime)==0;
        }
        else{
            // other isn't a gameTime
            return false;
        }
    }

    /**
     * Returns the clock time represented by this GameTime, ie hour:minutes
     * @return String described above
     */
    public String getClockTime(){
        recreateCalendar();
        int hour = this.calendar.get(Calendar.HOUR_OF_DAY);
        // convert our hour from 0-23 to an hour 1-12 with an am/pm
        boolean isAM = false;
        if(hour<12){
            // hour is before 12 pm, represents time in the am
            isAM = true;
        }
        else{
            // convert our hours above 12 down to
            hour -= 12;
        }

        if(hour == 0){
            // 0 now represents the hour 12, either am or pm
            hour = 12;
        }

        String hourString = String.valueOf(hour);

        int minute = this.calendar.get(Calendar.MINUTE);
        String minuteString = "";
        // if our minute value is less than 10, add a leading 0, this gives us 12:07 instead of 12:7
        if(minute<10){
            minuteString = "0";
        }
        minuteString += String.valueOf(minute);

        // add the am/pm after the minutes
        if(isAM){
            minuteString+=" am";
        }
        else{
            minuteString+=" pm";
        }
        return hourString + ":" + minuteString;
    }

    /**
     * Returns the date represented by this GameTime in the format month-day-year
     * @return String described above
     */
    public String getDateString(){
        recreateCalendar();
        // months in calendars are normally from 0-11, convert these to 1-12
        int month = this.calendar.get(Calendar.MONTH)+1;
        String monthString = "";
        switch(month){
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
        }
        String dayString = String.valueOf(this.calendar.get(Calendar.DATE));
        String yearString = String.valueOf(this.calendar.get(Calendar.YEAR));
        return monthString + " " + dayString + ", " + yearString;
    }

    /**
     * Returns a String representation of the GameTime, this returns a time formatted as month-day-year-hour:minute
     * @return String as described above
     */
    @Override
    @NonNull
    public String toString(){
        recreateCalendar();
        return this.getDateString() + " " + this.getClockTime();
    }


}
