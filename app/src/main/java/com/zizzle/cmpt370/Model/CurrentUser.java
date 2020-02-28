package com.zizzle.cmpt370.Model;

/**
 * Uses the singleton pattern to store a Member object with information about the current user of the app
 */
public class CurrentUser {
    /** current user of the app */
    private static Member currentUser = null;

    private CurrentUser(){
        // constructor is private so the class cannot be instantiated
    }

    public static Member getCurrentUser(){
        if(currentUser == null){
            // we don't have any info about the current user, read this from database
            
        }
        // currentUser now stores info about the current user of the app
        return currentUser;
    }



}
