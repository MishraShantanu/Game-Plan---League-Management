package com.zizzle.cmpt370;

import java.util.ArrayList;

/**
 * Team class for holding information of the team.
 */
public class Team {

    /** Name of the team. */
    private String name;

    /** Members of the team */
    private ArrayList<User> members;

    /** Games previously played by the team, this list is ordered so games towards the front of the
     * list are more recent than those at the back */
    private ArrayList<Game> gamesPlayed;

    // TODO add league field

    /** Owner of the team */
    private User owner;

    /** Next game the team is scheduled to play */
    private Game nextGame;

    /** Number of wins the team has */
    private int wins;

    /** number of losses the team has*/
    private int losses;


    /**
     * Constructor for a Team object
     * @param name: String name of the new team, team names must be unique for the league the team is in
     * @param owner: User object, owner/creator of the team
     */
    public Team(String name, User owner){
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<>();
        // assume the owner is always a player on the team
        this.members.add(owner);
        this.wins = 0;
        this.losses = 0;
    }


    /**
     * Retrieves the name of the team.
     * @return String name of the team.
     */
    public String getName() {
        return this.name;
    }
}
