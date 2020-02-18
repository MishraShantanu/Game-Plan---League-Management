package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Pair;


/**
 * Parent class, stores generic information about sports games
 */
public class Game {

    /** first team in the game */
    private Team team1;

    /** second team in the game */
    private Team team2;

    /** date (year, month, day, hour, minute) the game is being/was played */
    private GameTime date;

    /** name of the location the game is/was held at */
    private String location;

    /** name of the sport of the game */
    private String sport;

    /** final scores of the teams who are in the game, can only be set after the game has been played */
    private Pair<Integer,Integer> finalScores;

    /** boolean true if the game has been played, false otherwise */
    private boolean played;


    /**
     * Game constructor
     * @param team1: Team object, first of the teams playing in the game
     * @param team2: Team object, second of the teams playing in the game
     * @param gameDate: GameTime object specifying when this game is scheduled to occur
     * @param location: String name of the location the game is being held
     * @param sport: String name of the sport of the game
     * @throws IllegalArgumentException if the gameDate refers to a time in the past, games must be
     * scheduled for a time in the future
     */
    public Game(Team team1, Team team2, GameTime gameDate, String location, String sport) throws IllegalArgumentException{
        //TODO: Could have home and away teams, team1 could be home etc
        this.team1 = team1;
        this.team2 = team2;
        if(!gameDate.isInFuture()){
            throw new IllegalArgumentException("Game: input GameTime refers to a time in the past, games must be scheduled for the future");
        }
        this.date = gameDate;
        this.location = location;
        this.sport = sport;
        this.played = false;
    }

    /**
     * Returns the first team playing in the game
     * @return Team object of the first team playing
     */
    public Team getTeamOne(){
        return this.team1;
    }

    /**
     * Returns the second team playing in the game
     * @return Team object the second team in the game
     */
    public Team getTeamTwo(){
        return this.team2;
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
        this.finalScores = new Pair<>(team1Score,team2Score);
    }

    /**
     * Checks if the game has ended in a tie, game must have been played, ie hasBeenPlayed() must be true
     * @return true if the game has been played, false otherwise
     * @throws IllegalStateException if game hasn't yet been played
     */
    public boolean gameTied() throws IllegalStateException{
        // check that game has been played and we have valid scores
        if(! this.hasBeenPlayed()){
            throw new IllegalStateException("Game: gameTied() called before game has been played");
        }
        // game has tied if both teams have the same score
        return this.finalScores.first.equals(this.finalScores.second);
    }

    /**
     * Returns the team who won the game, can only be called after the game has been played ie hasBeenPlayed() is true
     * @return Team object, winner of the game if one exists ie the teams didn't tie
     * @throws IllegalStateException if hasBeenPlayed() is false or the teams have tied ie gameTied() is true
     */
    public Team getWinner() throws IllegalStateException{
        // make sure game has been played
        if(! this.hasBeenPlayed()){
            throw new IllegalStateException("Game: getWinner() called before game has been played");
        }
        // make sure the game didn't end in a tie
        if(this.gameTied()){
            throw new IllegalStateException("Game: getWinner() called when the teams have tied");
        }
        // the winner is the team with higher score
        if(this.finalScores.first.compareTo(this.finalScores.second) > 0){
            // team 1 has a higher score and is winner
            return this.team1;
        }
        else{
            return this.team2;
        }
    }

    /**
     * Returns the team who lost the game, can only be called after the game has been played, ie hasBeenPlayed() is true
     * @return Team object, loser of the game if one exists, ie game didn't end in a tie
     * @throws IllegalStateException if hasBeenPlayed() is false or the teams have tied ie gameTied() is true
     */
    public Team getLoser() throws IllegalStateException{
        // make sure game has been played
        if(! this.hasBeenPlayed()){
            throw new IllegalStateException("Game: getLoser() called before game has been played");
        }
        // make sure the game didn't end in a tie
        if(this.gameTied()){
            throw new IllegalStateException("Game: getLoser() called when the teams have tied");
        }
        // the loser is the team with lower score
        if(this.finalScores.first.compareTo(this.finalScores.second) < 0){
            // team 1 has a lower score and is loser
            return this.team1;
        }
        else{
            return this.team2;
        }
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
     * @return String representing the game, including the sport, teams playing in the game, the scheduled time
     * of the game and the final scores if the game has been played
     */
    @Override
    @NonNull
    public String toString(){
        String gameString = "Sport: " + this.sport;
        gameString += "\nTeams: " + this.team1 + " vs " + this.team2;
        gameString += "\nFinal Score: ";
        if(this.hasBeenPlayed()){
            gameString += this.finalScores.first + " to " + this.finalScores.second;
        }
        else{
            // game scores haven't yet been decided
            gameString += "TBD";
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
            return otherGame.getGameTime().equals(this.getGameTime()) && otherGame.team1.equals(this.team1) &&
                    otherGame.team2.equals(this.team2) && otherGame.location.equals(this.location) && otherGame.sport.equals(this.sport);
        }
        // Other isn't a game and can't be equal
        return false;

    }







}