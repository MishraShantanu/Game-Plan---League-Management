package com.zizzle.cmpt370;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Calendar;

/**
 * Parent class, stores generic information about sports games
 */
public class Game {

    /** teams who are in the game */
    private Pair<Team,Team> teams;

    /** date (year, month, day, hour, minute) the game is being/was played */
    private Calendar date;

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
     * @param gameDate: int array of the form [year, month, day, hour, minute], months are from 0-11,
     *               hour is on a 24 hr clock ie 0-23, minutes are from 0-59, gameDate must be after
     *               the current date
     * @param location: String name of the location the game is being held
     * @param sport: String name of the sport of the game
     * @throws IllegalArgumentException if time fields in gameDate are out of the bounds specified or if
     * the game is scheduled to occur in the past
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
        date = Calendar.getInstance();
        // by default set the last field, seconds to 0
        date.set(gameDate[0],gameDate[1],gameDate[2], gameDate[3], gameDate[4], 0);
        // make sure game isn't scheduled to occur in the past
        Calendar now = Calendar.getInstance();
        if(now.after(this.date)){
            // Game is scheduled before now and so is scheduled for the past
            throw new IllegalArgumentException("Game: gameDate specified is in the past");
        }
        this.location = location;
        this.sport = sport;
        this.played = false;
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
            return this.teams.first;
        }
        else{
            return this.teams.second;
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
            return this.teams.first;
        }
        else{
            return this.teams.second;
        }
    }

    public void rescheduleGame(int[] newTime) throws IllegalStateException{
        // check that time fields are valid
        int month = newTime[1];
        if(month < 0 || newTime[1] > 11){
            throw new IllegalArgumentException("Game: gameDate month out of bounds, valid bounds: 0-11" +
                    " actual month: " + month);
        }
        int hour = newTime[3];
        if(hour < 0 || hour > 23){
            throw new IllegalArgumentException("Game: gameDate hour out of bounds, valid bounds: 0-23" +
                    " actual hour: " + hour);
        }
        int minutes = newTime[4];
        if(minutes < 0 || minutes > 59){
            throw new IllegalArgumentException("Game: gameDate minutes out of bounds, valid bounds: 0-59" +
                    " actual minutes: " + minutes);
        }
        Calendar newDate = Calendar.getInstance();
        // by default set the last field, seconds to 0
        newDate.set(newTime[0],newTime[1],newTime[2], newTime[3], newTime[4], 0);
        // make sure game isn't scheduled to occur in the past
        Calendar now = Calendar.getInstance();
        if(now.after(newDate)){
            // Game is scheduled before now and so is scheduled for the past
            throw new IllegalArgumentException("Game: gameDate specified is in the past");
        }
        // new time is valid
        this.date = newDate;
    }

    /**
     * Returns an array of the form [days, hours, minutes] until the game is scheduled
     * to occur
     * @return long[] of the form above
     * @throws IllegalStateException if the game has already started
     */
    public long[] timeUntilGame() throws IllegalStateException{
        if(this.hasGameStarted()){
            throw new IllegalStateException("Game: timeUntilGame called after game has started, use timeSinceGameStarted() instead");
        }
        Calendar currentTime = Calendar.getInstance();
        long currentMilliseconds = currentTime.getTimeInMillis();
        long gameMilliseconds = this.date.getTimeInMillis();
        long millisecondsUntilGame = gameMilliseconds-currentMilliseconds;
        // convert the milliseconds to days, hours, minutes, manually
        final long millisecondsPerMinute = 1000*60;
        final long millisecondsPerHour = millisecondsPerMinute*60;
        final long millisecondsPerDay = millisecondsPerHour*24;
        long daysUntilGame = millisecondsUntilGame/millisecondsPerDay;
        // update millisecondsUntilDay now that days have been considered
        millisecondsUntilGame = millisecondsUntilGame%millisecondsPerDay;
        long hoursUntilGame = millisecondsUntilGame/millisecondsPerHour;
        millisecondsUntilGame = millisecondsUntilGame%millisecondsPerHour;
        long minutesUntilGame = millisecondsUntilGame/millisecondsPerMinute;
        long[] timeDifferenceArray = {daysUntilGame,hoursUntilGame,minutesUntilGame};
        return timeDifferenceArray;
    }

    /**
     * Returns an array of the form [days, hours, minutes] since the game has started
     * @return long[] of the form above
     * @throws IllegalStateException if the game hasn't yet started
     */
    public long[] timeSinceGameStarted() throws IllegalStateException{
        if(! this.hasGameStarted()){
            throw new IllegalStateException("Game: timeSinceGameStart called before game has started, use timeUntilGame() instead");
        }
        Calendar currentTime = Calendar.getInstance();
        long currentMilliseconds = currentTime.getTimeInMillis();
        long gameMilliseconds = this.date.getTimeInMillis();
        long millisecondsSinceGame = currentMilliseconds - gameMilliseconds;
        // convert the milliseconds to days, hours, minutes, manually
        final long millisecondsPerMinute = 1000*60;
        final long millisecondsPerHour = millisecondsPerMinute*60;
        final long millisecondsPerDay = millisecondsPerHour*24;
        long daysUntilGame = millisecondsSinceGame/millisecondsPerDay;
        // update millisecondsUntilDay now that days have been considered
        millisecondsSinceGame = millisecondsSinceGame%millisecondsPerDay;
        long hoursUntilGame = millisecondsSinceGame/millisecondsPerHour;
        millisecondsSinceGame = millisecondsSinceGame%millisecondsPerHour;
        long minutesUntilGame = millisecondsSinceGame/millisecondsPerMinute;
        long[] timeDifferenceArray = {daysUntilGame,hoursUntilGame,minutesUntilGame};
        return timeDifferenceArray;
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
        gameString += "\nTeams: " + this.teams.first + " vs " + this.teams.second;
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







}
