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

    /** List of games the team is scheduled to play, this list is ordered such that closer games are
     * towards the front of the list and further away games are towards the end of the list */
    private ArrayList<Game> scheduledGames;

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
        this.gamesPlayed = new ArrayList<>();
        this.scheduledGames = new ArrayList<>();
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

    /**
     * Returns an array of the members of the team
     * @return User[], array of members of the team
     */
    public User[] getTeamMembers(){
        return (User[]) this.members.toArray();
    }

    /**
     * Returns the User who has the specified name from the team
     * @param username: String name of a user on the team, this name must belong to a user on the team
     * @return User with the name input
     * @throws IllegalArgumentException if username doesn't belong to any user on the team
     */
    public User getMemberByName(String username) throws IllegalArgumentException{
        // linear search the list of team members for the input name
        for(User currentUser : this.members){
            if(currentUser.getName().equals(username)){
                // user found
                return currentUser;
            }
        }
        // user not found
        throw new IllegalArgumentException("Team: User with name '" + username + "' not on team: " + this.name);
    }

    /**
     * Checks if the team has at least 1 game scheduled in the future
     * @return true if the team has at least 1 game scheduled in the future, false otherwise
     */
    public boolean hasGamesScheduled(){
        return this.scheduledGames.size() > 0;
    }

    /**
     * Gets the closest upcoming game the team has scheduled
     * @return Game object that is scheduled to be played closest to now
     * @throws IllegalStateException if the team has no games scheduled
     */
    public Game getClosestScheduledGame() throws IllegalStateException{
        // make sure there is a game scheduled
        if(! this.hasGamesScheduled()){
            throw new IllegalStateException("Team: team '" + this.name + "' has no games scheduled");
        }

        // the closest game is always at the front of the list of scheduled games
        return this.scheduledGames.get(0);
    }

    /**
     * Checks if the team has played at least 1 game before
     * @return true if the team has played a game, false otherwise
     */
    public boolean hasPlayedGame(){
        return this.gamesPlayed.size() > 0;
    }

    /**
     * Gets the game the team most recently played
     * @return Game object, most recently played game
     * @throws IllegalStateException if the team hasn't played any games
     */
    public Game getMostRecentPlayedGame() throws IllegalStateException{
        // make sure the team has played at least 1 game
        if(! this.hasPlayedGame()){
            throw new IllegalStateException("Team: team '" + this.name + "' hasn't yet played any games");
        }
        // most recently played game is always at the front of the list of played games
        return this.gamesPlayed.get(0);
    }

    /**
     * Returns an array of the games played by the team in order, the first game is most recently played
     * the last game is least recently played, the team must have played at least 1 game
     * @return Game[] specified above
     * @throws IllegalStateException if the team hasn't played a game yet
     */
    public Game[] getPlayedGames() throws IllegalStateException{
        // make sure the team has played at least 1 game
        if(! this.hasPlayedGame()){
            throw new IllegalStateException("Team: team '" + this.name + "' hasn't yet played any games");
        }
        return (Game[]) this.gamesPlayed.toArray();
    }

    /**
     * Returns an array of games the team is scheduled to play, this array is sorted so the first element
     * is the closest upcoming game, and the last element is the game scheduled furthest in the future,
     * the team must have at least 1 game scheduled
     * @return Game[] specified above
     * @throws IllegalStateException if the team doesn't have at least 1 game scheduled
     */
    public Game[] getScheduledGames() throws IllegalStateException{
        // make sure the team has at least 1 game scheduled
        if(! this.hasGamesScheduled()){
            throw new IllegalStateException("Team: team '" + this.name + "' doesn't have any games scheduled");
        }
        return (Game[]) this.scheduledGames.toArray();
    }







    //TODO methods to schedule new games, assert the times of these games are after now
    // TODO method to set a game as played, require scores etc, can move game from scheduled to played, assert the game's start time has passed







}
