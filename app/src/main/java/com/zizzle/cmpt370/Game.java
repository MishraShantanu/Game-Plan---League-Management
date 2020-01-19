package com.zizzle.cmpt370;

import android.util.Pair;

import java.util.Calendar;

/**
 * Parent class, stores generic information about sports games
 */
public class Game {

    /** teams who are in the game */
    private Pair<Team,Team> teams;

    /** date (year, month, day, time) the game is being/was played */
    private Calendar date;

    /** name of the location the game is/was held at */
    private String location;

    /** name of the sport of the game */
    private String sport;


    /**
     * Game constructor
     * @param team1: Team object, first of the teams playing in the game
     * @param team2: Team object, second of the teams playing in the game
     * @param gameDate: int array of the form [year, month, day, hour, minute], months are from 0-11,
     *               hour is on a 24 hr clock ie 0-23, minutes are from 0-59
     * @param location: String name of the location the game is being held
     * @param sport: String name of the sport of the game
     */
    public Game(Team team1, Team team2, int[] gameDate, String location, String sport) throws IllegalArgumentException{
        //TODO: Could have home and away teams, team1 could be home etc
        this.teams = new Pair<>(team1,team2);

        // check that time fields are valid
        int month = gameDate[1];
        if(month < 0 || gameDate[1] > 11){
            throw new IllegalArgumentException("Game: gameDate month out of bounds, valid bounds: 0-11" +
                    " actual month: " + month);
        }
        
        date.set(gameDate[0],gameDate[1],gameDate[2], gameDate[3], gameDate[4]);
        this.location = location;
        this.sport = sport;
    }

    /**
     * Returns the teams playing in the game
     * @return Pair object of the teams playing
     */
    public Pair<Team, Team> getTeams(){
        return this.teams;
    }

    /**
     * Returns the location of the game
     * @return String name of the location of the game
     */
    public String getLocation(){
        return this.location;
    }

    /**
     * Returns the year the game is scheduled to be on
     * @return int year the game is being played on
     */
    public int getGameYear(){
        // return the 0th (year) field of the calendar
        return this.date.get(0);
    }

    /**
     * Returns the month the game is being played on
     * @return int 0-11 the month number the game is played on
     */
    public int getGameMonth(){
        return this.date.get(1);
    }

    /**
     * Returns the day the game is being played on
     * @return int day the game is being played on
     */
    public int getGameDay(){
        return this.date.get(2);
    }

    /**
     * Returns the hour the game is being played on
     * @return int 0-23 the hour the game is being played on
     */
    public int getGameHour(){
        return this.date.get(3);
    }

    /**
     * Returns the minutes of the hour the game is being played on
     * @return int minutes 0-59 of the hour the game is being played on
     */
    public int getGameMinutes(){
        return this.date.get(4);
    }

    //TODO: timeUntilGame method in game not yet played object







}
