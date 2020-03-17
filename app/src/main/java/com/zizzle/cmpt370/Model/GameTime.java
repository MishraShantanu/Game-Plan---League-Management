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
    private Calendar time;

    /** format used to display the time of this game in the form dd/mm/yyyy */
    public static String DDMMYYYY_FORMAT = "dd/MM/yyyy";

    /** format used to display the time of this game in the form yyyy/mm/dd */
    public static String YYYYMMDD_FORMAT = "yyyy/MM/dd";

    /**
     * GameTime constructor
     * @param year: int, the year the game will take place e.g. 2020
     * @param month: int, 1-12, the month the game will take place, a month of 1 represents January and so on
     * @param day: int, the day of the month the game will take place, e.g. 15, cannot be greater than 31
     * @param hour: int, the hour of the day the game will take place 0-23, where 0 represents 12:00 AM
     * @param minutes: int, the minutes of the hour the game will take place, 0-59
     * @throws IllegalArgumentException: if any of the above arguments are outside of their specified bounds,
     * additionally a GameTime object can only represent a date in the future, if the input arguments
     * specify a date in the past, this exception is thrown
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
        this.time = Calendar.getInstance();
        // set the seconds field to 0 as a Game cannot be scheduled for a date so specific
        this.time.set(year,month,day,hour,minutes,0);
        // set milliseconds to 0
        this.time.set(Calendar.MILLISECOND,0);

        // make sure game isn't scheduled to occur in the past
        if(! this.isInFuture()){
            throw new IllegalArgumentException("GameTime: date specified isn't in the future");
        }

    }

    /**
     * Returns an array specifying the time the game will take place
     * @return int array of the form [year, month, day, hour, minute] specifying the date and time
     * the game takes place
     */
    public int[] getTimeArray(){
        // months are from 0-11, but we expect months to be from 1-12, increment months
        int[] timeArray = {this.time.get(Calendar.YEAR), this.time.get(Calendar.MONTH)+1, this.time.get(Calendar.DATE),
                this.time.get(Calendar.HOUR_OF_DAY), this.time.get(Calendar.MINUTE)};
        return timeArray;
    }

    /**
     * Compares this GameTime to another GameTime, this will produce a IllegalArgumentException if other isn't a GameTime
     * @param other: other GameTime object to compare this GameTime to
     * @return 0 if this and other represent the same time, < 0 if this represent a time before the time
     * represented by other, > 0 if this represents a time later than other
     */
    @Override
    public int compareTo(Object other){
        if(!(other instanceof GameTime)){
            // other is an invalid type
            throw new IllegalArgumentException("Cannot compare a GameTime object to an object of type: " + other.getClass().getName());
        }
        GameTime otherGameTime = (GameTime) other;
        // compare underlying calendars
        return this.time.compareTo(otherGameTime.time);
    }

    /**
     * Checks if the GameTime object specifies a time that is in the future
     * @return true if the GameTime specifies a time in the future, false otherwise
     */
    public boolean isInFuture(){
        Calendar now = Calendar.getInstance();
        return this.time.after(now);
    }

    /**
     * determines if another object is equal to this
     * @param other: Object being compared against this
     * @return true if other is equal to this game time, ie both other and this represent the same time
     */
    @Override
    public boolean equals(Object other){
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
     * Returns a String representation of the GameTime, this returns a time formatted as DD/MM/YYYY
     * @return String as described above
     */
    @Override
    @NonNull
    public String toString(){
        // use the standard day/month/year format
        return this.getDateWithFormat(DDMMYYYY_FORMAT);
    }

    public String getDateWithFormat(String format){
        // make sure that the format is part of the valid recognized formats
        if(!format.equals(DDMMYYYY_FORMAT) && !format.equals(YYYYMMDD_FORMAT)){
            throw new IllegalArgumentException(format + " is an invalid format");
        }
        // convert our Calendar object to a Date object, display using the input format under the local timezone
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.toString();
    }


}
