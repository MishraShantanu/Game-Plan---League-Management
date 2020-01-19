package com.zizzle.cmpt370;

import android.util.Pair;

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

    /** win loss record of team, first entry in the pair is the number of wins, second is number of losses */
    private Pair<Integer,Integer> winLoss;








    public Team(){

    }


    /**
     * Retrieves the name of the team.
     * @return String name of the team.
     */
    public String getName() {
        return this.name;
    }
}
