package com.zizzle.cmpt370.Model;

/**
 * Stores information about a particular League, facilitates easy database access to underlying league
 */
public class LeagueInfo implements InfoInterface {

    /**
     * name of the league being represented by this object
     */
    private String name;

    /**
     * Creates a LeagueInfo object that stores information about the input league
     *
     * @param league: League object, league to store information about
     */
    public LeagueInfo(League league) {
        name = league.getName();
    }

    /**
     * Creates a LeagueInfo object with the input league name, this should correspond to
     * an actual league
     *
     * @param leagueName: String name of the league being represented
     */
    public LeagueInfo(String leagueName) {
        this.name = leagueName;
    }

    /**
     * Blank constructor required for database
     */
    public LeagueInfo() {

    }

    public String getName() {
        return name;
    }

    public String getDatabaseKey() {
        // Since each league name is unique, a league's name is also its key in the database
        return name;
    }

}
