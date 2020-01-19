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

    /** number of ties the team has */
    private int ties;


    /**
     * Constructor for a Team object
     * @param name: String name of the new team, team names must be unique for the league the team is in
     * @param owner: User object, owner/creator of the team
     */
    public Team(String name, User owner){
        //TODO check if new team name is unique for the league the team is in
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<>();
        // assume the owner is always a player on the team
        this.members.add(owner);
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
    }

    /**
     * Retrieves the name of the team.
     * @return String name of the team.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the owner of the team
     * @return User object owner of the team
     */
    public User getOwner(){
        return this.owner;
    }

    /**
     * Sets the owner of the team to the user specified, if the new owner isn't part of the team,
     * they will be added to the members of the team, the old owner still remains part of the team
     * and can be removed using removeMember
     * @param newOwner: User object, new owner of the team
     */
    public void setOwner(User newOwner){
        // if newOwner isn't on the team, add them to the team members
        if(! members.contains(newOwner)){
            members.add(newOwner);
        }
        this.owner = newOwner;
    }

    /**
     * Removes the input User from the team
     * @param memberToRemove: User object to be removed from the team, this User must be on the team
     *        and cannot be the owner of the team, a new owner of the team must be
     *        set before the old owner can be removed
     * @throws IllegalStateException if memberToRemove is the owner of the team
     * @throws IllegalArgumentException if memberToRemove isn't on the team
     */
    public void removeMember(User memberToRemove) throws IllegalStateException, IllegalArgumentException{
        // make sure memberToRemove is on the team
        if(! this.members.contains(memberToRemove)){
            throw new IllegalArgumentException("Team: User: " + memberToRemove.getName() + " to remove from team: "
                    + this.name + " isn't a member of the team");
        }
        // make sure memberToRemove isn't the owner
        if(memberToRemove.equals(this.owner)){
            throw new IllegalStateException("Team: User: " + memberToRemove.getName() + " cannot be removed from team " +
                    this.name + " as this user is the owner of the team");
        }

        this.members.remove(memberToRemove);
    }

    /**
     * Returns the number of wins the team has
     * @return int number of wins the team has
     */
    public int getWins(){
        return this.wins;
    }

    /**
     * Returns the number of losses the team has
     * @return int number of losses the team has
     */
    public int getLosses(){
        return this.losses;
    }

    /**
     * Increases the number of wins the team has by 1
     */
    public void incrementWins(){
        this.wins++;
    }

    /**
     * Increases the number of losses the team has by 1
     */
    public void incrementLosses(){
        this.losses++;
    }

    /**
     * Returns the number of ties the team has
     * @return int number of ties the team has
     */
    public int getTies(){
        return this.ties;
    }

    /**
     * Increases the number of ties the team has by 1
     */
    public void incrementTies(){
        this.ties++;
    }


}
