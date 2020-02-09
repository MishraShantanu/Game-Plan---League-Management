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
     * Constructor for the Member object.
     *
     * @param firstName:   First name
     * @param lastName:    Last name
     * @param email:       email of member
     * @param phoneNumber: phone number of memeber
     */
    public Member(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPhoneNumber(phoneNumber);
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
     * Set the phone number of a user to new phone number.
     *
     * @param phoneNumber: String value of the phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        // Strip non-numeric characters from phone number.
        phoneNumber = phoneNumber.replaceAll("\\D", "");

        if (phoneNumber.length() != 11)
            System.out.println("Error in setPhoneNumber(): Invalid phone number: " + phoneNumber);
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(phoneNumber.substring(0, 1)).append("-");
            sb.append(phoneNumber.substring(1, 4)).append("-");
            sb.append(phoneNumber.substring(4, 7)).append("-");
            sb.append(phoneNumber.substring(7, 11));
            this.phoneNumber = sb.toString();
        }
    }


    /**
     * Adds a team to the list of teams user belongs to.
     *
     * @param team: Team object to add to list of teams.
     */
    public void addTeam(Team team) {
        // Check if user is already part of this team.
        if (!this.teams.contains(team)) {
            this.teams.add(team);
        } else {
            System.out.println("Error in addTeam(): Member is already part of " + team.getName());
        }
    }


    /**
     * Removes a team from the list of teams the user belongs to.
     *
     * @param teamName: Name of the team to remove.
     */
    public void removeTeam(String teamName) {
        // Check if team is part of user's teams.
        for (Team team : this.teams) {
            if (team.getName().equals(teamName)) {
                this.teams.remove(team);
                return;
            }
        }
        System.out.println("Error in removeTeam(): Member isn't part of team " + teamName);
    }


    /**
     * Return information about user in String format.
     *
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + this.firstName + " " + this.lastName + "\n");
        sb.append("Email: " + this.email + "\n");
        sb.append("Phone Number: " + this.phoneNumber + "\n");
        sb.append("Teams:");
        for (Team team : this.teams) {
            sb.append("\n\t" + team.getName());
        }
        return sb.toString();
    }
}
