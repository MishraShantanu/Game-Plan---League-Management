package com.zizzle.cmpt370.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
     * @throws IllegalStateException if the user isn't signed onto the app
     * @throws IllegalArgumentException if the user isn't on the database
     */
    public static Member getCurrentUser() throws IllegalStateException{
        if(currentUser == null){
            // Firebase stores some information about the current user, like name and email, we must still
            // read the rest of the user's data from the database
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            if(fbUser == null){
                // there is currently no user signed onto the app
                throw new IllegalStateException("Cannot get current user, user isn't signed in to the app");
            }
            else{
                // user is signed on, use the information stored by firebase to read from the database
                MemberInfo currentUserInfo = new MemberInfo(fbUser.getDisplayName(),fbUser.getUid());
                // read the currentMember from the database
                currentUser = Storage.readMember(currentUserInfo);
                if(currentUser==null){
                    // the current user isn't on the database
                    throw new IllegalArgumentException("Cannot get the current user, user isn't present on the database");
                }
                // otherwise currentUser is now a Member object with information about the user of the app
            }
        }
        // currentUser now stores info about the current user of the app
        return currentUser;
    }



}
