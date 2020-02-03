package com.zizzle.cmpt370;

import android.support.annotation.NonNull;

import java.util.ArrayList;


/**
 * Member class for holding information about a user.
 */
public class Member {

    /** Name of the user. */
    private String name;

    /** Email of the user. */
    private String email;

    /** Password of the user. */
    // TODO 15/01/2020 Add Password field.

    /** Phone number of the user. */
    private long phoneNumber;

    /** Teams the user belongs to. */
    private ArrayList<Team> teams = new ArrayList<>();



    /**
     * Constructor for Member object.
     * @param name: Name of the user.
     * @param email E-mail of the user.
     */
    public Member(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        setPhoneNumber(phoneNumber);
    }


    /**
     * Retrieves the name of the user.
     * @return name of the user.
     */
    public String getName() { return name; }


    /**
     * Retrieves the email of the user.
     * @return email of the user.
     */
    public String getEmail() { return email; }


    /**
     * Retrieves the phone number of the user.
     * @return phone number of the user.
     */
    public long getPhoneNumber() { return phoneNumber; }


    /**
     * Retrieves the teams the user belongs to.
     * @return ArrayList of teams user belongs to.
     */
    public ArrayList<Team> getTeams() { return teams; }


    /**
     * Sets the name of the user to the given name.
     * @param name: new name for the user.
     */
    public void setName(String name) { this.name = name; }


    /**
     * Set the email of the user to the given email.
     * @param email: new email for the user.
     */
    public void setEmail(String email) { this.email = email; }


    /**
     * Set the phone number of a user to new phone number.
     * @param phoneNumber: String value of the phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        // Strip non-numeric characters from phone number.
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        // Parse String of numeric characters to long (int too small for phone numbers).
        try {
            this.phoneNumber = Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("Error in setPhoneNumber(): Invalid phone number: " + phoneNumber);
        }
    }


    /**
     * Adds a team to the list of teams user belongs to.
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
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + this.name + "\n");
        sb.append("Email: " + this.email + "\n");
        sb.append("Phone Number: " + this.phoneNumber + "\n");
        sb.append("Teams:");
        for (Team team : this.teams) {
            sb.append("\n\t" + team.getName());
        }
        return sb.toString();
    }
}
