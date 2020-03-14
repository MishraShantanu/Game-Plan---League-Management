package com.zizzle.cmpt370.Model;

import java.io.Serializable;

/**
 * Stores information about a particular team, facilitates easy database access to underlying team
 */
public class TeamInfo implements InfoInterface, Serializable {

    /** name of the team represented by this object */
    private String name;

    /** key into the database that the underlying team can be accessed with, must be used from the correct
     * database path*/
    private String databaseKey;

    /**
     * Creates a TeamInfo object that stores information about the input team
     * @param team: team to store information for
     */
    public TeamInfo(Team team){
        name = team.getName();
        String leagueName = team.getLeagueInfo().getName();
        // team names must be unique per league, so we use leagueName-teamName as our database key
        databaseKey = leagueName + "-" + name;
    }

    /**
     * Creates a TeamInfo object that stores information about the input team
     * @param teamName: name of the team being represented
     * @param leagueName: name of the league this team is a part of
     */
    public TeamInfo(String teamName, String leagueName){
        this.name = teamName;
        this.databaseKey = leagueName + "-" + teamName;
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
     * Returns a string representation of the TeamInfo object
     */
    @Override
    public String toString(){
        // represent the TeamInfo object by team name
        return this.name;
    }



}
