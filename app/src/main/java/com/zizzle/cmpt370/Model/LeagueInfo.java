package com.zizzle.cmpt370.Model;

/**
 * Stores information about a particular League, facilitates easy database access to underlying league
 */
public class LeagueInfo implements InfoInterface{

    /** name of the league being represented by this object*/
    private String leagueName;

    /**
     * Creates a LeagueInfo object that stores information about the input league
     * @param league: League object, league to store information about
     */
    public LeagueInfo(League league){
        leagueName = league.getName();
    }

    public String getName(){
        return leagueName;
    }

    public String getDatabaseKey(){
        // Since each league name is unique, a league's name is also its key in the database
        return leagueName;
    }

}
