package com.zizzle.cmpt370;

import java.util.Calendar;

/**
 * Class for representing the time a game will be played
 */
public class GameTime {

    /** Calendar storing years, months, days, hours, minutes, seconds when Game will occur */
    private Calendar time;

    /**
     * GameTime constructor
     * @param year: int, the year the game will take place e.g. 2020
     * @param month: int, 1-12, the month the game will take place, a month of 1 represents January and so on
     * @param day: int, the day of the month the game will take place, e.g. 15
     * @param hour: int, the hour of the day the game will take place 0-23, where 0 represents 12:00 AM
     * @param minutes: int, the minutes of the hour the game will take place, 0-59
     * @throws IllegalArgumentException: if any of the above arguments are outside of their specified bounds,
     * additionally a GameTime object can only represent a date in the future, if the input arguments
     * specify a date in the past, this exception is thrown
     */
    public GameTime(int year, int month, int day, int hour, int minutes) throws IllegalArgumentException{
        if(month < 0 || month > 11){
            throw new IllegalArgumentException("GameTime: month out of bounds, valid bounds: 0-11" +
                    " actual month: " + month);
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

        // make sure game isn't scheduled to occur in the past
        Calendar now = Calendar.getInstance();
        if(this.time.before(now)){
            // Game is scheduled before now and so is scheduled for the past
            throw new IllegalArgumentException("GameTime: date specified is in the past");
        }

    }


}
