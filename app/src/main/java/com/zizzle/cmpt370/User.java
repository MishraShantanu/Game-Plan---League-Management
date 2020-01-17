package com.zizzle.cmpt370;

import android.support.annotation.NonNull;

public class User {

    /** Name of the user. */
    private String name;

    /** Email of the user. */
    private String email;

    /** Phone number of the user */
    private int phoneNumber;

    // TODO 15/01/2020 Add Password field.

    // TODO 15/01/2020 Add list of Team objects that member belongs to.



    /**
     * Constructor for User object.
     * @param name: Name of the user.
     * @param email E-mail of the user.
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


    // Getter and Setters.
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }


    /**
     * Return information about user in String format.
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        return "Name: " + this.name + "\nEmail: " + this.email + "\n";
    }
}
