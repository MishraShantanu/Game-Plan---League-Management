package com.zizzle.cmpt370.Model;

import java.io.Serializable;

/**
 * Stores information about a particular team, facilitates easy database access to underlying team
 */
public class TeamInfo implements InfoInterface, Serializable, Comparable {

    /** name of the team represented by this object */
    private String name;

    /** key into the database that the underlying team can be accessed with, must be used from the correct
     * database path*/
    private String databaseKey;

    /** name of league this team is a part of*/
    private String leagueName;

    /** number of wins the team represented by this info has */
    private int wins;

    /**
     * Creates a TeamInfo object that stores information about the input team
     * @param team: team to store information for
     */
    public TeamInfo(Team team){
        this.name = team.getName();
        String leagueName = team.getLeagueInfo().getName();
        // team names must be unique per league, so we use leagueName-teamName as our database key
        this.databaseKey = leagueName + "-" + name;
        this.leagueName = leagueName;
        this.wins = team.getWins();
    }

    /**
     * Creates a TeamInfo object that stores information about the input team
     * @param teamName: name of the team being represented
     * @param leagueName: name of the league this team is a part of
     * @param wins: int wins this team has
     */
    public TeamInfo(String teamName, String leagueName, int wins){
        this.name = teamName;
        this.databaseKey = leagueName + "-" + teamName;
        this.leagueName = leagueName;
        this.wins = wins;
    }

    /**
     * Blank constructor required for database
     */
    public TeamInfo(){

    }

    public String getName(){
        return name;
    }

    public String getDatabaseKey(){
        return databaseKey;
    }

    /**
     * Returns the String name of the league this team is a part of
     * @return String league name this team is a part of
     */
    public String getLeagueName(){
        return this.leagueName;
    }

    /**
     * Returns the number of wins the team represented by this TeamInfo has
     * @return int wins of this team
     */
    public int getWins(){
        return this.wins;
    }

    /**
     * Returns a string representation of the TeamInfo object
     */
    @Override
    public String toString(){
        // represent the TeamInfo object by team name
        return this.name;
    }

    /**
     * Determines whether other is equal to this
     * @param other: Object being compared to this
     * @return true if other and this are equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof TeamInfo)){
            // other isn't a TeamInfo cannot be equal to this TeamInfo object
            return false;
        }
        TeamInfo otherTeamInfo = (TeamInfo) other;
        // 2 TeamInfos are equal if they have the same name, league and database key
        return this.name.equals(otherTeamInfo.name) && this.leagueName.equals(otherTeamInfo.leagueName) && this.databaseKey.equals(otherTeamInfo.databaseKey);
    }

    /**
     * Compares this to the input Object other
     * @param other: Object to be compared to this, must be a TeamInfo object
     * @return < 0 if this<other, > 0 if this>other, 0 if this==other
     */
    @Override
    public int compareTo(Object other){
        if(!(other instanceof TeamInfo)){
            throw new IllegalArgumentException("Cannot compare a TeamInfo to a " + other.getClass());
        }
        TeamInfo otherTeamInfo = (TeamInfo) other;
        // TeamInfos are compared based on the number of wins, a TeamInfo with more wins is considered greater
        if(this.wins>otherTeamInfo.getWins()){
            return 1; // > 0, as this is greater than otherTeamInfo
        }
        else if(this.wins<otherTeamInfo.getWins()){
            return -1; // < 0, as this is lesser than otherTeamInfo
        }
        else{
            // both TeamInfos are equal
            return 0;
        }
    }



}
