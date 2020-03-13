package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Pair;


/**
 * Parent class, stores generic information about sports games
 */
public class Game {

    /** first team in the game */
    private TeamInfo team1Info;

    /** second team in the game */
    private TeamInfo team2Info;

    /** date (year, month, day, hour, minute) the game is being/was played */
    private GameTime date;

    /** name of the location the game is/was held at */
    private String location;

    /** name of the sport of the game */
    private String sport;

    private int team1Score;

    private int team2Score;

    /** boolean true if the game has been played, false otherwise */
    private boolean played;


    /**
     * Game constructor
     * @param team1Info: TeamInfo object, first of the teams playing in the game
     * @param team2Info: TeamInfo object, second of the teams playing in the game
     * @param gameDate: GameTime object specifying when this game is scheduled to occur
     * @param location: String name of the location the game is being held
     * @param sport: String name of the sport of the game
     * @throws IllegalArgumentException if the gameDate refers to a time in the past, games must be
     * scheduled for a time in the future
     */
    public Game(TeamInfo team1Info, TeamInfo team2Info, GameTime gameDate, String location, String sport) throws IllegalArgumentException{
        //TODO: Could have home and away teams, team1 could be home etc
        this.team1Info = team1Info;
        this.team2Info = team2Info;
        if(!gameDate.isInFuture()){
            throw new IllegalArgumentException("Game: input GameTime refers to a time in the past, games must be scheduled for the future");
        }
        this.date = gameDate;
        this.location = location;
        this.sport = sport;
        this.played = false;
        this.team1Score = 0;
        this.team2Score = 0;
    }

    /**
     * Returns the first team playing in the game
     * @return TeamInfo object of the first team playing
     */
    public TeamInfo getTeam1Info(){
        return this.team1Info;
    }

    /**
     * Returns the second team playing in the game
     * @return TeamInfo object the second team in the game
     */
    public TeamInfo getTeam2Info(){
        return this.team2Info;
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
     * Returns the time the game was scheduled to be played at
     * @return GameTime object describing the time the game is scheduled to be played
     */
    public GameTime getGameTime(){
        return this.date;
    }

    /**
     * Returns true if the start time of the game has been reached
     * @return true if the current time is past the start time of the game, false otherwise
     */
    public boolean hasGameStarted(){
        // if this game isn't scheduled for the future, it must have started
        return !this.date.isInFuture();
    }

    /**
     * Checks if the game has been played, a game is played when the setGameAsPlayed() method is used
     * @return true if the game has been played, false otherwise
     */
    public boolean hasBeenPlayed(){
        return this.played;
    }

    /**
     * Sets the final scores of each team of the game, this can only be done after the game has started,
     * ie hasGameStarted() is true, sets this game to played so that hasBeenPlayed() will now be true
     * @param team1Score: int final score of team 1 after the game has finished, cannot be negative
     * @param team2Score: int final score of team 2 after the game has finished, cannot be negative
     * @throws IllegalStateException if hasGameStarted() is false
     * @throws IllegalArgumentException if input scores are negative
     */
    public void setGameAsPlayed(int team1Score, int team2Score) throws IllegalStateException, IllegalArgumentException{
        // can only set score once game has started
        if(!hasGameStarted()){
            throw new IllegalStateException("Game: setScore() called before game has started");
        }
        // scores must be non negative
        if(team1Score < 0 || team2Score < 0) {
            throw new IllegalArgumentException("Game: setscore() called with negative score as argument");
        }
        // game has now been played
        this.played = true;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    /**
     * Retrieves team 1's score, this game must have been played in order to get scores
     * @return int team 1's score in this game
     * @throws IllegalStateException if the game hasn't been played yet
     */
    public int getTeam1Score() throws IllegalStateException{
        if(!this.hasBeenPlayed()){
            throw new IllegalStateException("Cannot get scores before this game has been played");
        }
        return this.team1Score;
    }

    /**
     * Retrieves team 2's score, this game must have been played in order to get scores
     * @return int team 2's score in this game
     * @throws IllegalStateException if the game hasn't been played yet
     */
    public int getTeam2Score() throws IllegalStateException{
        if(!this.hasBeenPlayed()){
            throw new IllegalStateException("Cannot get scores before this game has been played");
        }
        return this.team2Score;
    }

    /**
     * Reschedules the game to be played at a new time, cannot reschedule a game if it has already started
     * or been played
     * @param newTime: GameTime object, new time for the game to be played at
     * @throws IllegalStateException if newTime specifies a time in the past, or this game has already
     * started or been played
     */
    public void rescheduleGame(GameTime newTime) throws IllegalStateException{
        // can't reschedule game if it's already started or been played
        if(this.hasBeenPlayed() || this.hasGameStarted()){
            throw new IllegalStateException("Game: game cannot be rescheduled if it has already started or been played");
        }
        // cannot reschedule a game for a time not in the future
        else if(!newTime.isInFuture()){
            throw new IllegalStateException("Game: game must be rescheduled for a time in the future");
        }
        else{
            // new time is valid
            this.date = newTime;
        }
    }

    /**
     * Returns a string representation of the game
     * @return String representing the game, including the teams playing in the game, and the final scores if the game has been played
     */
    @Override
    @NonNull
    public String toString(){
        String gameString = this.team1Info + " vs " + this.team2Info;
        gameString += "\nFinal Score: ";
        if(this.hasBeenPlayed()){
            gameString += this.team1Score + "-" + this.team2Score;
        }
        else{
            gameString += "n/a";
        }
        return gameString;
    }

    /**
     * Determines if another object is equal to this
     * @param other: Object to determine if equal to this
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Game){
            // compare fields
            Game otherGame = (Game) other;
            return otherGame.getGameTime().equals(this.getGameTime()) && otherGame.team1Info.equals(this.team1Info) &&
                    otherGame.team2Info.equals(this.team2Info) && otherGame.location.equals(this.location) && otherGame.sport.equals(this.sport);
        }
        // Other isn't a game and can't be equal
        return false;

    }







}
