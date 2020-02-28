package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Static class used to read/write from database
 */
public class Storage {
    /** Reference to the database */
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    /** Stores the league read in, we require this to overcome scoping issues */
    private static League retrievedLeague;

    /**
     * Reads the League with the input name from the database
     * @param leagueName: String, name of the database to read from the database
     * @return League object with the name input or null if no such league exists
     * @throws DatabaseException if the database read fails
     */
    public static League readLeague(String leagueName) throws DatabaseException{
        // addListenerForSingleValueEvent reads from the database exactly once

        database.child("Leagues").child(leagueName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // called when data is read from database
                retrievedLeague = dataSnapshot.getValue(League.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // database read has failed for some reason, display error message
                throw new DatabaseException("Failed to read league information from database: " + databaseError.getMessage());
            }
        });
        // return the league read from the database, may be null if there is no league with name: leagueName
        return retrievedLeague;
    }

    /**
     * Adds the input league to the database
     * @param newLeague: League object to be added to the database, the name of this league must be
     *                 unique
     * @throws IllegalStateException if the name of the league isn't unique
     * @throws DatabaseException if accessing the database fails
     */
    public static void writeLeague(final League newLeague) throws IllegalStateException, DatabaseException{
        // determine if league name is unique in the database, try to read in a league with this name
        if(readLeague(newLeague.getName())!=null){
            // there is already a league with this league's name, cannot create a league with a duplicate name
            throw new IllegalStateException("League creation failed, league with name: " + newLeague.getName() + " already exists");
            // TODO: could also only enforce that a league name must be unique for a sport only, not globally
        }
        else{
            // this league has a unique name, write it to the database
            database.child("Leagues").child(newLeague.getName()).setValue(newLeague);
        }
    }


    public static void addTeam(Team newTeam){
        // assume newTeam already has a unique name for the league it's in
        // add team to database, this is added to Leagues/leaguename/teams/teamname/
        database.child("Leagues").child(newTeam.getLeague().getName()).child("teams").child(newTeam.getName()).setValue(newTeam);
    }

}
