package com.zizzle.cmpt370.Model;

/**
 * Uses the singleton pattern to store a Member object with information about the current user of the app
 */
public final class CurrentUser {
    /** current user of the app */
    private static Member currentUser = null;

    private CurrentUser(){
        // constructor is private so the class cannot be instantiated
    }

    /**
     * Returns a Member object filled with information about the current user of the app
     * @return Member, current user of the app
     */
    public static Member getCurrentUser(){
        if(currentUser == null){
            // we don't have any info about the current user, get info about current user
            //TODO either read user data from database, or read data from local storage

        }
        // currentUser now stores info about the current user of the app
        return currentUser;
    }



}
