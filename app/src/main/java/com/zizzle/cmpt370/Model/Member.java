package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Member class for holding information about a user.
 */
public class Member {

    /**
     * Name of the user
     */
    private String displayName;

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Phone number of the user.
     */
    private String phoneNumber;

    /** Unique ID identifying this user */
    private String userID;

    /**
     * TeamInfos of the teams the user belongs to, this is a map associating database key of teams with TeamInfo objects
     */
    private HashMap<String,TeamInfo> teamInfoMap = new HashMap<>();

    /**
     * Leagues the user belongs to.
     */
    private ArrayList<LeagueInfo> leaguesInfo = new ArrayList<>();


    /**
     * Constructor for the Member object.
     *
     * @param displayName: name of this user as displayed on the app
     * @param email:       email of member
     * @param phoneNumber: phone number of member
     * @param userID:      unique identifier for this member
     */
    public Member(String displayName, String email, String phoneNumber, String userID) {
        this.displayName = displayName;
        this.email = email;
        setPhoneNumber(phoneNumber);
        this.userID = userID;
    }

    /**
     * Blank constructor required for reassembling members read in from the database
     */
    public Member(){

    }


    /**
     * Retrieves the display name of the user.
     *
     * @return String display name of the user.
     */
    public String getDisplayName() {
        return displayName;
    }


    /**
     * Retrieves the email of the user.
     *
     * @return email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user ID of this member
     * @return String ID of this member
     */
    public String getUserID(){
        return this.userID;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Retrieves info about the teams the user belongs to.
     *
     * @return HashSet containing TeamInfo objects of the teams the user is a part of
     */
    public ArrayList<TeamInfo> getTeamsInfo() {
        // if this member isn't part of any teams and has been read in from the database, teamsInfoMap will be null
        if(this.teamInfoMap == null){
            // if this is the case, simply return an empty arraylist as this member isn't part of any teams
            return new ArrayList<>();
        }
        // otherwise convert teamsInfoMap into an arraylist
        return new ArrayList<>(this.teamInfoMap.values());
    }


    /**
     * Retrieves info about the leagues the user belongs to.
     * @return HashSet containing LeagueInfo objects with infor about the leagues the user belongs to.
     */
    public ArrayList<LeagueInfo> getLeaguesInfo() {
        return leaguesInfo;
    }


    /**
     * Sets the display name of the user to the given name.
     *
     * @param newName: new first name for the user.
     */
    public void setFirstName(String newName) {
        this.displayName = newName;
        // TODO write changes through to the database
    }


    /**
     * Set the email of the user to the given email.
     *
     * @param email: new email for the user.
     */
    public void setEmail(String email) {
        this.email = email;
        // TODO update email on database, may need to delete and recreate user
    }


    /**
     * Set the phone number of the member to the new phone number.
     * @param phoneNumber: New phone number for member.
     * @throws IllegalArgumentException if phone number isn't the correct length after remvoing non-
     * numeric characters (expected 11).
     */
    public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // Strip non-numeric characters from phone number.
        phoneNumber = phoneNumber.replaceAll("\\D", "");

        // Check the number is a full length phone number, we allow phone numbers of length 10 or 11
        if (phoneNumber.length() != 11 && phoneNumber.length() != 10){
            throw new IllegalArgumentException("Error in setPhoneNumber(): Phone number does not meet length requirements");
        }

        // convert into a formatted number.
        if(phoneNumber.length() == 10){
            // we must add a leading 1 to this phone number
            phoneNumber = "1" + phoneNumber;
        }
        this.phoneNumber = phoneNumber.substring(0, 1) + "-" + phoneNumber.substring(1, 4) + "-" +
                phoneNumber.substring(4, 7) + "-" + phoneNumber.substring(7, 11);
    }


    /**
     * Add a team to the users list of teams they are on.
     * @param teamToAdd: team to add to list.
     * @throws IllegalArgumentException if they are already part of the team.
     */
    public void addTeam(Team teamToAdd) throws IllegalArgumentException {
        TeamInfo newTeamInfo = new TeamInfo(teamToAdd);
        // Check if user is already part of this team.
        if (this.teamInfoMap.containsKey(newTeamInfo.getDatabaseKey())) throw new IllegalArgumentException("Error in addTeam(): " +
                "Member is already part of " + teamToAdd.getName());

        this.teamInfoMap.put(newTeamInfo.getDatabaseKey(),newTeamInfo);
        // TODO update team and user on the database to reflect this new team
    }


    /**
     * Remove a team from the users list of teams they are on, this is a no-op if the member isn't on the input team
     * @param teamToRemove: team to remove from the list.
     */
    public void removeTeam(Team teamToRemove) throws IllegalArgumentException {
        TeamInfo removeTeamInfo = new TeamInfo(teamToRemove);
        this.teamInfoMap.remove(removeTeamInfo.getDatabaseKey());
        // TODO remove this member from teamToRemove on the database
    }


    /**
     * Adds a league to the users list of leagues they are part of.
     * @param leagueToAdd: League to add to list.
     * @throws IllegalArgumentException if league already is part of users league list.
     */
    public void addLeague(League leagueToAdd) throws IllegalArgumentException {
        LeagueInfo leagueToAddInfo = new LeagueInfo(leagueToAdd);
        // Check if user is already part of this league.
        if (this.leaguesInfo.contains(leagueToAddInfo)) throw new IllegalArgumentException("Error in addLeague(): " +
                "Member is already part of " + leagueToAdd.getName());

        this.leaguesInfo.add(leagueToAddInfo);
        // TODO add this member to this league, add  this league to this member on the database
    }


    /**
     * Removes a league from the users list of leagues they are part of.
     * @param leagueToRemove: League to remove from list.
     * @throws IllegalArgumentException if league is not part of the list.
     */
    public void removeLeague(League leagueToRemove) throws IllegalArgumentException {
        LeagueInfo leagueToRemoveInfo = new LeagueInfo(leagueToRemove);
        // make sure leagueToRemove is part of the member's teams.
        if(! this.leaguesInfo.contains(leagueToRemoveInfo)){
            throw new IllegalArgumentException("League: " + leagueToRemove.getName() + " to remove from member: "
                    + this.displayName + " isn't a member of the league.");
        }

        this.leaguesInfo.remove(leagueToRemoveInfo);
        // TODO remove this league from the member on the database
    }


    /**
     * Return information about user in String format.
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        return this.getDisplayName();
    }

    /**
     * Determines if this is equal to the input object
     * @param other: Object being compared to this League
     * @return true if this is equal to other, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Member){
            // compare Member fields, equal members should have equal names, email, phone number, user ids, teams and leagues
            Member otherMember = (Member)other;
            boolean teamsEqual = this.teamInfoMap.equals(otherMember.teamInfoMap);
            boolean leaguesEqual = this.leaguesInfo.equals(otherMember.leaguesInfo);
            return teamsEqual && leaguesEqual && this.userID.equals(otherMember.userID) &&
                    this.displayName.equals(otherMember.displayName) && this.email.equals(otherMember.email) && this.phoneNumber.equals(otherMember.phoneNumber);
        }
        // other isn't a member, cannot be equal to a member
        return false;
    }
}
