package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;


/**
 * League class for holding the information about a league.
 */
public class League {

    /** Name of the league. */
    private String name;

    /** Owner of the league. */
    private Member owner;

    /** Sport played in the league. */
    private String sport;

    /** Description of the league. */
    private String description;

    /**
     * Teams involved in the league. This also acts as the standings for teams
     * with the teams sorted from best to worst.
     */
    private ArrayList<Team> teams;


    /**
     * Constructor for league object.
     * @param name: Name of the league.
     * @param owner: Member that owns the league.
     * @param sport: Type of the sport that the league plays.
     * @param description: Description of the league.
     */
    public League(String name, Member owner, String sport, String description) {
        this.name = name;
        this.owner = owner;
        this.sport = sport;
        this.description = description;
        this.teams = new ArrayList<>();
    }


    /**
     * Retrieves the name of the league.
     * @return name of the league.
     */
    public String getName() { return name; }


    /**
     * Retrieves the owner of the league.
     * @return owner of the league.
     */
    public Member getOwner() { return owner; }


    /**
     * Retrieves the sport the league plays.
     * @return sport the league plays.
     */
    public String getSport() { return sport; }


    /**
     * Retrieves the description of the league.
     * @return description of the league.
     */
    public String getDescription() { return description; }


    /**
     * Retrieves the list of teams in the league.
     * @return list of teams in the league.
     */
    public ArrayList<Team> getTeams() { return teams; }


    /**
     * Set the name the league to one provided.
     * @param name: new name for the league.
     */
    public void setName(String name) { this.name = name; }


    /**
     * Transfer ownership to the new owner provided.
     * @param owner: New owner of the league.
     */
    public void setOwner(Member owner) { this.owner = owner; }


    /**
     * Set the sport the league plays to one provided.
     * @param sport: Name of the sport.
     */
    public void setSport(String sport) { this.sport = sport; }


    /**
     * Set the description to the one provided.
     * @param description: String of the new team description.
     */
    public void setDescription(String description) { this.description = description; }


    /**
     * Add a team to the league.
     * @param team: Team to be added.
     */
    public void addTeam(Team team) {
        if (!teams.contains(team)) teams.add(team);
        else System.out.println("Error in addTeam():\n\tTeam " +
                team.getName() + " already in the league.");
    }


    /**
     * Remove a team from the list of teams in the league.
     * @param teamName: Name of the team to remove.
     */
    public void removeTeam(String teamName) {
        for (Team team : teams) {
            if (team.getName().equals(teamName)) {
                teams.remove(team);
            }
        }
    }


    /**
     * Sort the teams from best to worst.
     */
    public void sortTeams() {
        // TODO 18/01/2020 Sort teams based on the standings of teams.
    }


    /**
     * Retrieve a string of the information stored inside the League object.
     * @return string of information about the league.
     */
    @NonNull
    public String toString() {
        return this.getName();
    }
}
