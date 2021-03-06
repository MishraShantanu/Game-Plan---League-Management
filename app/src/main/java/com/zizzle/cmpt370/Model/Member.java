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

    /**
     * Unique ID identifying this user
     */
    private String userID;

    /**
     * TeamInfos of the teams the user belongs to, this is a map associating database key of teams with TeamInfo objects
     */
    private HashMap<String, TeamInfo> teamInfoMap = new HashMap<>();

    /**
     * HashMap with string league names as keys and LeagueInfo objects as values, representing the leagues this Member is an owner of
     */
    private HashMap<String, LeagueInfo> ownedLeaguesInfoMap = new HashMap<>();

    /**
     * Integer wins this Member has throughout all games they've played
     */
    private int careerWins;

    /**
     * Integer losses this Member has throughout all games they've played
     */
    private int careerLosses;

    /**
     * Integer ties this Member has throughout all games they've played
     */
    private int careerTies;


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
        // initialize stats for this new member
        this.careerWins = 0;
        this.careerLosses = 0;
        this.careerTies = 0;
    }

    /**
     * Blank constructor required for reassembling members read in from the database
     */
    public Member() {

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
     *
     * @return String ID of this member
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" +
                phoneNumber.substring(6, 10);
    }

    /**
     * returns the number of wins this Member has had throughout their career
     *
     * @return Integer number of career wins for this user
     */
    public int getCareerWins() {
        return this.careerWins;
    }

    /**
     * Returns the number of losses this Member has had throughout their career
     *
     * @return Integer number of career losses for this user
     */
    public int getCareerLosses() {
        return this.careerLosses;
    }

    /**
     * Returns the number of ties this Member has had throughout their career
     *
     * @return Integer number of career ties for this user
     */
    public int getCareerTies() {
        return this.careerTies;
    }

    /**
     * Returns a HashMap with string Team database keys as keys and TeamInfo values representing the teams
     * this user is a part of
     *
     * @return HashMap<String, TeamInfo> described above
     */
    public HashMap<String, TeamInfo> getTeamInfoMap() {
        // This method is required so Firebase is aware of and stores the teamInfoMap for a Member
        return this.teamInfoMap;
    }

    /**
     * Returns an ArrayList of TeamInfos of the teams that this Member is a part of
     *
     * @return ArrayList<TeamInfo> of teams this Member is a part of
     */
    public ArrayList<TeamInfo> getTeamsInfo() {
        // if this member isn't part of any teams and has been read in from the database, teamsInfoMap will be null
        if (this.teamInfoMap == null) {
            // if this is the case, simply return an empty arraylist as this member isn't part of any teams
            return new ArrayList<>();
        }
        // otherwise convert teamsInfoMap into an arraylist
        return new ArrayList<>(this.teamInfoMap.values());
    }

    /**
     * Returns a HashMap with String league names as keys and LeagueInfo objects as values, representing
     * the leagues this Member owns
     *
     * @return HashMap<String, LeagueInfo> described above
     */
    public HashMap<String, LeagueInfo> getOwnedLeaguesInfoMap() {
        // this method is required so Firebase will recognize the ownedLeaguesInfo attribute and store this on the database
        return this.ownedLeaguesInfoMap;
    }

    /**
     * Returns an ArrayList of LeagueInfos of the leagues that this Member owns
     *
     * @return ArrayList<LeagueInfo> of leagues this Member owns
     */
    public ArrayList<LeagueInfo> getOwnedLeaguesList() {
        // if this member isn't part of any teams and has been read in from the database, ownedLeaguesInfoMap will be null
        if (this.ownedLeaguesInfoMap == null) {
            // if this is the case, simply return an empty arraylist as this member doesn't own any leagues
            return new ArrayList<>();
        }
        // otherwise convert ownedLeaguesInfoMap into an arraylist
        return new ArrayList<>(this.ownedLeaguesInfoMap.values());
    }


    /**
     * Sets the display name of the user to the given name.
     *
     * @param newName: new first name for the user.
     */
    public void setFirstName(String newName) {
        this.displayName = newName;
    }


    /**
     * Set the email of the user to the given email.
     *
     * @param email: new email for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Set the phone number of the member to the new phone number.
     *
     * @param phoneNumber: New phone number for member.
     * @throws IllegalArgumentException if phone number isn't the correct length after remvoing non-
     *                                  numeric characters (expected 11).
     */
    public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // Strip non-numeric characters from phone number.
        phoneNumber = phoneNumber.replaceAll("\\D", "");

        // Check the number is a full length phone number, we allow phone numbers of length 10 or 11
        if (phoneNumber.length() != 10 && phoneNumber.length() != 11) {
            throw new IllegalArgumentException("Error in setPhoneNumber(): Phone number does not meet length requirements");
        }

        this.phoneNumber = phoneNumber;
    }


    /**
     * Add a team to the users list of teams they are on.
     *
     * @param teamToAdd: team to add to list.
     * @throws IllegalArgumentException if they are already part of the team.
     */
    public void addTeam(Team teamToAdd) throws IllegalArgumentException {
        TeamInfo newTeamInfo = new TeamInfo(teamToAdd);
        // Check if user is already part of this team.
        if (this.teamInfoMap.containsKey(newTeamInfo.getDatabaseKey()))
            throw new IllegalArgumentException("Error in addTeam(): " +
                    "Member is already part of " + teamToAdd.getName());

        this.teamInfoMap.put(newTeamInfo.getDatabaseKey(), newTeamInfo);
    }


    /**
     * Remove a team from the users list of teams they are on, this is a no-op if the member isn't on the input team
     *
     * @param teamToRemove: team to remove from the list.
     */
    public void removeTeam(Team teamToRemove) throws IllegalArgumentException {
        TeamInfo removeTeamInfo = new TeamInfo(teamToRemove);
        this.teamInfoMap.remove(removeTeamInfo.getDatabaseKey());
    }


    /**
     * Return information about user in String format.
     *
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        return this.getDisplayName();
    }

    /**
     * Determines if this is equal to the input object
     *
     * @param other: Object being compared to this League
     * @return true if this is equal to other, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Member) {
            // compare Member fields, equal members should have equal names, email, phone number, user ids, teams and leagues
            Member otherMember = (Member) other;
            boolean teamsEqual = this.teamInfoMap.equals(otherMember.teamInfoMap);
            boolean leaguesEqual = this.ownedLeaguesInfoMap.equals(otherMember.ownedLeaguesInfoMap);
            return teamsEqual && leaguesEqual && this.userID.equals(otherMember.userID) &&
                    this.displayName.equals(otherMember.displayName) && this.email.equals(otherMember.email) && this.phoneNumber.equals(otherMember.phoneNumber);
        }
        // other isn't a member, cannot be equal to a member
        return false;
    }
}
