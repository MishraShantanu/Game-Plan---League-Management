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
    public User(String name, String email, String phoneNumber) {
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
    public int getPhoneNumber() { return phoneNumber; }


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
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        try {
            this.phoneNumber = Integer.parseInt(phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid phone number: " + phoneNumber + "\nFrom setPhoneNumber()");
        }
    }


    /**
     * Return information about user in String format.
     * @return String of user information.
     */
    @NonNull
    public String toString() {
        return "Name: " + this.name + "\nEmail: " + this.email + "\n";
    }
}
