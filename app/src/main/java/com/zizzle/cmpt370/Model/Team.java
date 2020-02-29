package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;


/**
 * Team class for holding information of the team.
 */
public class Team {

    /** Name of the team. */
    private String name;

    /** Sport the team is playing */
    private String sport;

    /** Members of the team */
    private ArrayList<Member> members;

    /** Games previously played by the team, this list is ordered so games towards the front of the
     * list are more recent than those at the back */
    private ArrayList<Game> gamesPlayed;

    /** The league the team is a part of */
    private League league;

    /** Owner of the team */
    private Member owner;

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
     * @param owner: Member object, owner/creator of the team
     * @param sport: String sport the team plays, must match the sport of the league
     * @param league: League object, league the team is a part of, this adds the Team object to the league
     * @throws IllegalArgumentException if team name isn't unique for this league, or if league sport doesn't
     * match team sport
     */
    public Team(String name, Member owner, String sport, League league) throws IllegalArgumentException{
        this.league = league;
        // make sure the team name is unique to this league
        for (Team team : this.league.getTeams()){
            if(team.getName().equals(name)){
                // new name isn't unique for the league
                throw new IllegalArgumentException("Team name: " + name + " isn't unique in league: " + league);
            }
        }
        // make sure team and league sport match
        if(!this.league.getSport().equals(sport)){
            throw new IllegalArgumentException("Team sport: " + sport + " doesn't match league sport: " + league.getSport());
        }
        this.name = name;
        this.owner = owner;
        this.sport = sport;
        this.members = new ArrayList<>();
        // assume the owner is always a player on the team
        this.members.add(owner);
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.gamesPlayed = new ArrayList<>();
        this.scheduledGames = new ArrayList<>();
        // add team to league
        this.league.addTeam(this);
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
     * @return Member object owner of the team
     */
    public Member getOwner(){
        return this.owner;
    }

    /**
     * Retrieves the league the team is a part of
     * @return League the team is a part of
     */
    public League getLeague(){
        return this.league;
    }

    /**
     * Sets the owner of the team to the Member specified, if the new owner isn't part of the team,
     * they will be added to the members of the team, the old owner still remains part of the team
     * and can be removed using removeMember
     * @param newOwner: Member object, new owner of the team
     */
    public void setOwner(Member newOwner){
        // if newOwner isn't on the team, add them to the team members
        if(! members.contains(newOwner)){
            members.add(newOwner);
        }
        this.owner = newOwner;
    }

    /**
     * Removes the input Member from the team
     * @param memberToRemove: Member object to be removed from the team, this Member must be on the team
     *        and cannot be the owner of the team, a new owner of the team must be
     *        set before the old owner can be removed
     * @throws IllegalStateException if memberToRemove is the owner of the team
     * @throws IllegalArgumentException if memberToRemove isn't on the team
     */
    public void removeMember(Member memberToRemove) throws IllegalStateException, IllegalArgumentException{
        // make sure memberToRemove is on the team
        if(! this.members.contains(memberToRemove)){
            throw new IllegalArgumentException("Team: Member: " + memberToRemove.getFirstName() + " to remove from team: "
                    + this.name + " isn't a member of the team");
        }
        // make sure memberToRemove isn't the owner
        if(memberToRemove.equals(this.owner)){
            throw new IllegalStateException("Team: Member: " + memberToRemove.getFirstName() + " cannot be removed from team " +
                    this.name + " as this Member is the owner of the team");
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
     * Returns the number of ties the team has
     * @return int number of ties the team has
     */
    public int getTies(){
        return this.ties;
    }

    /**
     * Determines if the input member is on the team
     * @param member: Member to determine if on the team
     * @return true if member is on the team, false otherwise
     */
    public boolean teamHasMember(Member member){
        return this.members.contains(member);
    }

    /**
     * Adds the input member to the team
     * @param newMember: member to be added to the team
     * @throws IllegalStateException if the input member is already on the team
     */
    public void addMember(Member newMember) throws IllegalStateException{
        if(this.teamHasMember(newMember)){
            throw new IllegalStateException("Member: " + newMember.toString() + " is already on this team");
        }
        this.members.add(newMember);
    }


    /**
     * Returns an array of the members of the team
     * @return Member[], array of members of the team
     */
    public Member[] getTeamMembers(){
        return this.members.toArray(new Member[this.members.size()]);
    }

    /**
     * Returns the Member who has the specified name from the team
     * @param MemberFirstName: String name of a Member on the team, this name must belong to a Member on the team
     * @return Member with the name input
     * @throws IllegalArgumentException if Membername doesn't belong to any Member on the team
     */
    public Member getMemberByFirstName(String MemberFirstName) throws IllegalArgumentException{
        // linear search the list of team members for the input name
        for(Member currentMember : this.members){
            if(currentMember.getFirstName().equals(MemberFirstName)){
                // Member found
                return currentMember;
            }
        }
        // Member not found
        throw new IllegalArgumentException("Team: Member with name '" + MemberFirstName + "' not on team: " + this.name);
    }

    /**
     * increases the number of wins the team has by 1
     */
    public void incrementWins(){
        this.wins++;
    }

    /**
     * increases the number of losses the team has by 1
     */
    public void incrementLosses(){
        this.losses++;
    }

    /**
     * increases the number of ties the team has by 1
     */
    public void incrementTies(){
        this.ties++;
    }

    /**
     * Returns a String representation of the team including team name, sport, and win/loss/tie record
     * @return String described above
     */
    @Override
    @NonNull
    public String toString(){
        return this.getName();
    }

    /**
     * Checks where this Team is equal to the input parameter
     * @param other: Object to see if equal to this Team
     * @return true if other is equal to this, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Team){
            Team otherTeam = (Team) other;
            return this.name.equals(otherTeam.name) && this.sport.equals(otherTeam.sport) && this.league.equals(otherTeam.league) && this.owner.equals(otherTeam.getOwner());
        }
        // other isn't a Team, cannot be equal to this
        return false;
    }





    ////////////////////////////////////////////////////////
    // below methods involve games, keep these just in case
    ////////////////////////////////////////////////////////

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
        return this.scheduledGames.toArray(new Game[this.scheduledGames.size()]);
    }

    public void scheduleNewGame(Game newGame) throws IllegalStateException{
        // game must not have already been started
        if(newGame.hasGameStarted() || newGame.hasBeenPlayed()){
            throw new IllegalStateException("Team: new game scheduled has already started");
        }
        boolean foundPosition = false;
        for(int i=0;i<this.scheduledGames.size();i++){
            // linear search for a position to add this item, could binary search
            GameTime otherGameTime = this.scheduledGames.get(i).getGameTime();
            if(newGame.getGameTime().compareTo(otherGameTime)<0){
                // new game is scheduled to occur before the other game, place this in the list first
                this.scheduledGames.add(i,newGame);
                foundPosition = true;
                break;
            }
        }
        if(! foundPosition){
            // newGame must occur later than all other scheduled games, insert at the end of the list
            this.scheduledGames.add(newGame);
        }
    }


    public void markGameAsPlayed(Game playedGame){
        // make sure that playedGame involves our team
        if((! playedGame.getTeamOne().equals(this)) && (! playedGame.getTeamTwo().equals(this))){
            // neither team of the game is this team
            throw new IllegalArgumentException("Team: Game input to markGameAsPlayed doesn't involve this team");
        }
        // make sure input game has been played
        if(! playedGame.hasBeenPlayed()){
            throw new IllegalArgumentException("Team: Game input ot markGameAsPlayed hasn't yet been played");
        }
        if(playedGame.gameTied()){
            this.ties++;
        }
        else if(playedGame.getWinner().equals(this)){
            this.wins++;
        }
        else{
            this.losses++;
        }
        boolean foundPosition = false;
        // linear search, could binary search
        for(int i=0;i<this.gamesPlayed.size();i++){
            GameTime otherGameTime = this.gamesPlayed.get(i).getGameTime();
            if(playedGame.getGameTime().compareTo(otherGameTime)<0){
                // played game was scheduled before otherGame, insert played game before other game
                this.gamesPlayed.add(i,playedGame);
                foundPosition = true;
                break;
            }
        }
        if(!foundPosition){
            // playedGame was scheduled after every other game, insert this at the end of the list
            this.gamesPlayed.add(playedGame);
        }
    }

    /**
     * Cancels a game that the team was scheduled to play
     * @param gameToCancel: Game object that the team is scheduled to play
     * @throws IllegalArgumentException if the team isn't scheduled to play gameToCancel
     */
    public void cancelGame(Game gameToCancel) throws IllegalArgumentException{
        boolean removedSuccessfully = this.scheduledGames.remove(gameToCancel);
        // true is returned if gameToCancel is found and removed
        if(! removedSuccessfully){
            throw new IllegalArgumentException("Team: game: " + gameToCancel + " not scheduled to be played by this team");
        }
    }







}
