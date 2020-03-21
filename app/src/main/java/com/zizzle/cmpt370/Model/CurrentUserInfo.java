package com.zizzle.cmpt370.Model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Uses the singleton pattern to store a Member object with information about the current user of the app
 */
public final class CurrentUserInfo {
    /** info on current user of the app */
    private static MemberInfo currentUserInfo = null;

    private CurrentUserInfo(){
        // constructor is private so the class cannot be instantiated
    }


    /**
     * Returns a MemberInfo object filled with information about the current user of the app
     * @return MemberInfo containing information about the current user of the app
     * @throws IllegalStateException if the user isn't signed onto the app
     */
    public static MemberInfo getCurrentUserInfo() throws IllegalStateException{
        if(currentUserInfo == null){
            // Firebase stores the userID and display name of the user
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            if(fbUser == null){
                // there is currently no user signed onto the app
                throw new IllegalStateException("Cannot get current user, user isn't signed in to the app");
            }
            else{
                // user is signed on, use the information stored by firebase to create an info object
                currentUserInfo = new MemberInfo(fbUser.getDisplayName(),fbUser.getUid());
            }
        }
        // currentUser now stores info about the current user of the app
        return currentUserInfo;
    }

    /**
     * Refreshes the MemberInfo stored by this class, should be used when a new user has signed in to
     * remove stale MemberInfo from a previous user
     */
    public static void refreshMemberInfo(){
        // set the current user of the app to null so we have to reset the MemberInfo to reflect a new user
        currentUserInfo = null;
    }


    /**
     * Initializes the current user's info with the information provided
     * @param userID: String userID of the current user
     * @param displayName: String username of the current user
     * @throws IllegalStateException: if information for the current user is already stored
     */
    public static void initializeMemberInfo(String userID, String displayName) throws IllegalStateException{
        // enforce that we can only set the current user info if not already set
//        if(currentUserInfo != null){
//            throw new IllegalStateException("Cannot initialize current user info, current user info is already set");
//        }
        currentUserInfo = new MemberInfo(displayName,userID);
    }



}
