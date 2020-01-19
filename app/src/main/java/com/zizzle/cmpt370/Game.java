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

    /** final scores of the teams who are in the game, can only be set after the game has been played */
    private Pair<Integer,Integer> finalScores;


    /**
     * Game constructor
     * @param team1: Team object, first of the teams playing in the game
     * @param team2: Team object, second of the teams playing in the game
     * @param gameDate: int array of the form [year, month, day, hour, minute], months are from 0-11,
     *               hour is on a 24 hr clock ie 0-23, minutes are from 0-59
     * @param location: String name of the location the game is being held
     * @param sport: String name of the sport of the game
     * @throws IllegalArgumentException if time fields in gameDate are out of the bounds specified
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
        int hour = gameDate[3];
        if(hour < 0 || hour > 23){
            throw new IllegalArgumentException("Game: gameDate hour out of bounds, valid bounds: 0-23" +
                    " actual hour: " + hour);
        }
        int minutes = gameDate[4];
        if(minutes < 0 || minutes > 59){
            throw new IllegalArgumentException("Game: gameDate minutes out of bounds, valid bounds: 0-59" +
                    " actual minutes: " + minutes);
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
     * Returns the sport of the game
     * @return String name of the sport of the game
     */
    public String getSport(){
        return this.sport;
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

    /**
     * Returns true if the start time of the game has been reached
     * @return true if the current time is past the start time of the game, false otherwise
     */
    public boolean hasGameStarted(){
        Calendar now = Calendar.getInstance();
        // is now after the start of the game
        return now.after(this.date);
    }

    /**
     * Sets the final scores of each team of the game, this can only be done after the game has started,
     * ie hasGameStarted() is true
     * @param team1Score: int final score of team 1 after the game has finished, cannot be negative
     * @param team2Score: int final score of team 2 after the game has finished, cannot be negative
     * @throws IllegalStateException if hasGameStarted() is false
     * @throws IllegalArgumentException if input scores are negative
     */
    public void setScore(int team1Score, int team2Score) throws IllegalStateException, IllegalArgumentException{
        // can only set score once game has started
        if(!hasGameStarted()){
            throw new IllegalStateException("Game: setScore() called before game has started");
        }
        // scores must be non negative
        if(team1Score < 0 || team2Score < 0) {
            throw new IllegalArgumentException("Game: setscore() called with negative score as argument");
        }

        this.finalScores = new Pair<>(team1Score,team2Score);
    }

    //TODO: timeUntilGame method in game not yet played object







}
