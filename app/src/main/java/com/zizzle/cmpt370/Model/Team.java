package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * Team class for holding information of the team.
 */
public class Team {

    /** Name of the team. */
    private String name;

    /** Sport the team is playing */
    private String sport;

    /** Info about Members of the team in the form of a map associating user IDs to MemberInfo objects */
    private HashMap<String,MemberInfo> membersInfoMap;

    /** TreeMap with the database keys of games played as keys and the Game objects played as values */
    private TreeMap<String,Game> gamesPlayedMap;
    //TODO these treemaps won't work, we require string keys, use old idea of unique keys, to get the most recent game, must get a values list then sort
    //TODO could however add an instance variable keeping track of the next most recently scheduled game, how can we check this when writing to the database

    /** Info about the league the team is a part of */
    private LeagueInfo leagueInfo;

    /** Info about the owner of the team */
    private MemberInfo ownerInfo;

    /** TreeMap with the database keys of games schedules as keys and the Game objects schedules as values */
    private TreeMap<String,Game> scheduledGamesMap;

    /** Number of wins the team has */
    private int wins;

    /** number of losses the team has*/
    private int losses;

    /** number of ties the team has */
    private int ties;


    /**
     * Constructor for a Team object
     * @param name: String name of the new team, team names must be unique for the league the team is in
     * @param ownerInfo: MemberInfo object, represents the owner/creator of the team
     * @param sport: String sport the team plays, must match the sport of the league
     * @param leagueInfo: LeagueInfo object, representing the league this team belongs to
     */
    public Team(String name, MemberInfo ownerInfo, String sport, LeagueInfo leagueInfo) {
        this.leagueInfo = leagueInfo;
        this.name = name;
        this.ownerInfo = ownerInfo;
        this.sport = sport;
        this.membersInfoMap = new HashMap<>();
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.gamesPlayedMap = new TreeMap<>();
        this.scheduledGamesMap = new TreeMap<>();
    }

    public Team(){

    }

    /**
     * Retrieves the name of the team.
     * @return String name of the team.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrievs a MemberInfo object with info about the owner of the team
     * @return MemberInfo object describing the owner of the team
     */
    public MemberInfo getOwnerInfo(){
        return this.ownerInfo;
    }


    public LeagueInfo getLeagueInfo(){
        return this.leagueInfo;
    }

    /**
     * Sets the owner of the team to the MemberInfo specified, if the new owner isn't part of the team,
     * they will be added to the members of the team, the old owner still remains part of the team
     * and can be removed using removeMember
     * @param newOwnerInfo: MemberInfo object representing the new owner of the team
     */
    public void setOwner(MemberInfo newOwnerInfo){
        // if newOwner isn't on the team, add them to the team members
        if(membersInfoMap!= null && ! membersInfoMap.containsKey(newOwnerInfo.getDatabaseKey())){
            membersInfoMap.put(newOwnerInfo.getDatabaseKey(),newOwnerInfo);
            // update this team on the database to include this new member
        }
        this.ownerInfo = newOwnerInfo;
        // update this team on the database to include this new owner
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
        MemberInfo memberToRemoveInfo = new MemberInfo(memberToRemove);
        if(! this.membersInfoMap.containsKey(memberToRemoveInfo.getDatabaseKey())){
            throw new IllegalArgumentException("Team: Member: " + memberToRemove.getDisplayName() + " to remove from team: "
                    + this.name + " isn't a member of the team");
        }
        // make sure memberToRemove isn't the owner
        if(memberToRemoveInfo.equals(this.ownerInfo)){
            throw new IllegalStateException("Team: Member: " + memberToRemove.getDisplayName() + " cannot be removed from team " +
                    this.name + " as this Member is the owner of the team");
        }

        this.membersInfoMap.remove(memberToRemoveInfo.getDatabaseKey());
        // TODO remove memberToRemove from this team on the database
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
        // TODO: could take in memberInfo
        return this.membersInfoMap.containsKey(member.getUserID());
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
        MemberInfo newMemberInfo = new MemberInfo(newMember);
        this.membersInfoMap.put(newMemberInfo.getDatabaseKey(),newMemberInfo);
        // TODO add this new member to the database for this team
    }


    /**
     * Returns a hashset of the members of the team
     * @return HashSet containing info of the members of the team
     */
    public ArrayList<MemberInfo> getTeamMembersInfo(){
        // if there are no members on this team, and we read this object from the database, membersInfoMap will be null
        if(this.membersInfoMap == null){
            // in this case just return an empty arraylist, as there are no members on the team
            return new ArrayList<>();
        }
        // otherwise convert the values of our map into an arraylist
        return new ArrayList<>(this.membersInfoMap.values());
    }

    /**
     * Returns the MemberInfo who has the specified user ID from the team
     * @param userID: String user ID of a Member on the team, if this user ID doesn't belong to a member on the team null is returned
     * @return MemberInfo representing a member with the input userID
     */
    public MemberInfo getMemberInfoByUserID(String userID) {
        return this.membersInfoMap.get(userID);
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
            return this.name.equals(otherTeam.name) && this.sport.equals(otherTeam.sport) && this.leagueInfo.equals(otherTeam.getLeagueInfo()) && this.ownerInfo.equals(otherTeam.getOwnerInfo());
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
        // scheduledGamesMap may be null if this object has been read from the database without having any games
        if(this.scheduledGamesMap == null){
            return false;
        }
        return this.scheduledGamesMap.size() > 0;
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
        // the firstKey of this map will always be the key with lowest value, this lowest value key must
        // be correspond to the game with closest date
        return this.scheduledGamesMap.get(this.scheduledGamesMap.firstKey());
    }

    /**
     * Checks if the team has played at least 1 game before
     * @return true if the team has played a game, false otherwise
     */
    public boolean hasPlayedGame(){
        return this.gamesPlayedMap.size() > 0;
    }

    /**
     * Gets the game the team most recently played
     * @return Game object, most recently played game
     * @throws IllegalStateException if the team hasn't played any games
     */
    /*
    public Game getMostRecentPlayedGame() throws IllegalStateException{
        // make sure the team has played at least 1 game
        if(! this.hasPlayedGame()){
            throw new IllegalStateException("Team: team '" + this.name + "' hasn't yet played any games");
        }
        // most recently played game is always at the front of the list of played games
        return this.gamesPlayed.get(0);
    }
     */

    /**
     * Returns an array of the games played by the team in order, the first game is most recently played
     * the last game is least recently played, the team must have played at least 1 game
     * @return Game[] specified above
     * @throws IllegalStateException if the team hasn't played a game yet
     */
    /*
    public Game[] getPlayedGames() throws IllegalStateException{
        // make sure the team has played at least 1 game
        if(! this.hasPlayedGame()){
            throw new IllegalStateException("Team: team '" + this.name + "' hasn't yet played any games");
        }
        return (Game[]) this.gamesPlayed.toArray();
    }
     */

    /**
     * Returns an array of games the team is scheduled to play, this array is sorted so the first element
     * is the closest upcoming game, and the last element is the game scheduled furthest in the future,
     * the team must have at least 1 game scheduled
     * @return Game[] specified above
     * @throws IllegalStateException if the team doesn't have at least 1 game scheduled
     */
    /*
    public Game[] getScheduledGames() throws IllegalStateException{
        // make sure the team has at least 1 game scheduled
        if(! this.hasGamesScheduled()){
            throw new IllegalStateException("Team: team '" + this.name + "' doesn't have any games scheduled");
        }
        return this.scheduledGames.toArray(new Game[this.scheduledGames.size()]);
    }
     */

    /*
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

     */

/*
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

 */

    /**
     * Cancels a game that the team was scheduled to play
     * @param gameToCancel: Game object that the team is scheduled to play
     * @throws IllegalArgumentException if the team isn't scheduled to play gameToCancel
     */
    /*
    public void cancelGame(Game gameToCancel) throws IllegalArgumentException{
        boolean removedSuccessfully = this.scheduledGames.remove(gameToCancel);
        // true is returned if gameToCancel is found and removed
        if(! removedSuccessfully){
            throw new IllegalArgumentException("Team: game: " + gameToCancel + " not scheduled to be played by this team");
        }
    }
     */
}
