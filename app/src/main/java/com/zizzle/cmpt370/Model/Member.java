package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;

import java.util.ArrayList;


/**
 * Member class for holding information about a user.
 */
public class Member {

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Phone number of the user.
     */
    private String phoneNumber;

    /**
     * Teams the user belongs to.
     */
    private ArrayList<Team> teams = new ArrayList<>();

    /**
     * Teams the user belongs to.
     */
    private ArrayList<League> leagues = new ArrayList<>();


    /**
     * Constructor for the Member object.
     *
     * @param firstName:   First name
     * @param lastName:    Last name
     * @param email:       email of member
     * @param phoneNumber: phone number of member
     */
    public Member(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        //setPhoneNumber(phoneNumber);
    }


    /**
     * Retrieves the first name of the user.
     *
     * @return First name of the user.
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Retrieves the last name of the user.
     *
     * @return Last name of the user.
     */
    public String getLastName() {
        return lastName;
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
     * Retrieves the phone number of the user.
     *
     * @return phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }


    /**
     * Retrieves the teams the user belongs to.
     *
     * @return ArrayList of teams user belongs to.
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }


    /**
     * Retrieves the leagues the user belongs to.
     * @return ArrayList of the leagues the user belongs to.
     */
    public ArrayList<League> getLeagues() {
        return leagues;
    }


    /**
     * Sets the first name of the user to the given name.
     *
     * @param firstName: new first name for the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Sets the last name of the user to the given name.
     *
     * @param lastName: new last name for the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @param phoneNumber: New phone number for member.
     * @throws IllegalArgumentException if phone number isn't the correct length after remvoing non-
     * numeric characters (expected 11).
     */
    public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // Strip non-numeric characters from phone number.
//        phoneNumber = phoneNumber.replaceAll("\\D", "");
//
//        // Check the number is a full length phone number.
//        if (phoneNumber.length() != 11) throw new IllegalArgumentException("Error in setPhoneNumber(): Phone number does not meet length requirements");
//
//        // convert into a formatted number.
//        this.phoneNumber = phoneNumber.substring(0, 1) + "-" + phoneNumber.substring(1, 4) + "-" +
//                phoneNumber.substring(4, 7) + "-" + phoneNumber.substring(7, 11);
    }


    /**
     * Add a team to the users list of teams they are on.
     * @param teamToAdd: team to add to list.
     * @throws IllegalArgumentException if they are already part of the team.
     */
    public void addTeam(Team teamToAdd) throws IllegalArgumentException {
        // Check if user is already part of this team.
        if (this.teams.contains(teamToAdd)) throw new IllegalArgumentException("Error in addTeam(): " +
                "Member is already part of " + teamToAdd.getName());

        this.teams.add(teamToAdd);
    }


    /**
     * Remove a team from the users list of teams they are on.
     * @param teamToRemove: team to remove from the list.
     * @throws IllegalArgumentException if they are not part of this team.
     */
    public void removeTeam(Team teamToRemove) throws IllegalArgumentException {
        // make sure teamToRemove is part of the member's teams.
        if(! this.teams.contains(teamToRemove)){
            throw new IllegalArgumentException("Team: " + teamToRemove.getName() + " to remove from member: "
                    + this.firstName + " " + this.lastName + " isn't a member of the team");
        }

        this.teams.remove(teamToRemove);
    }


    /**
     * Adds a league to the users list of leagues they are part of.
     * @param leagueToAdd: League to add to list.
     * @throws IllegalArgumentException if league already is part of users league list.
     */
    public void addLeague(League leagueToAdd) throws IllegalArgumentException {
        // Check if user is already part of this league.
        if (this.leagues.contains(leagueToAdd)) throw new IllegalArgumentException("Error in addLeague(): " +
                "Member is already part of " + leagueToAdd.getName());

        this.leagues.add(leagueToAdd);
    }


    /**
     * Removes a league from the users list of leagues they are part of.
     * @param leagueToRemove: League to remove from list.
     * @throws IllegalArgumentException if league is not part of the list.
     */
    public void removeLeague(League leagueToRemove) throws IllegalArgumentException {
        // make sure leagueToRemove is part of the member's teams.
        if(! this.leagues.contains(leagueToRemove)){
            throw new IllegalArgumentException("League: " + leagueToRemove.getName() + " to remove from member: "
                    + this.firstName + " " + this.lastName + " isn't a member of the league.");
        }

        this.leagues.remove(leagueToRemove);
    }


    /**
     * Return information about user in String format.
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        return this.getFirstName();
    }
}
