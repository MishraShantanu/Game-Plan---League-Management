package com.zizzle.cmpt370.Model;

import java.io.Serializable;

/**
 * Stores information about a particular team, facilitates easy database access to underlying team
 */
public class TeamInfo implements InfoInterface, Serializable {

    /** name of the team represented by this object */
    private String teamName;

    /** key into the database that the underlying team can be accessed with, must be used from the correct
     * database path*/
    private String teamKey;

    /**
     * Creates a TeamInfo object that stores information about the input team
     * @param team: team to store information for
     */
    public TeamInfo(Team team){
        teamName = team.getName();
        String leagueName = team.getLeagueInfo().getName();
        // team names must be unique per league, so we use leagueName-teamName as our database key
        teamKey = leagueName + "-" + teamName;
    }

    /**
     * Blank constructor required for database
     */
    public TeamInfo(){

    }

    public String getName(){
        return teamName;
    }

    public String getDatabaseKey(){
        return teamKey;
    }


}
