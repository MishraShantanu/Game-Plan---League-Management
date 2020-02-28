package com.zizzle.cmpt370.Model;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Static class used to read/write from database
 */
public class Storage {
    /** Reference to the database */
    private DatabaseReference database;

    /**
     * Adds the input league to the database
     * @param newLeague: League object to be added to the database, the name of this league must be
     *                 unique
     * @throws IllegalStateException if the name of the league isn't unique
     * @throws DatabaseException if accessing the database fails
     */
    public void addLeague(final League newLeague) throws IllegalStateException, DatabaseException{
        // determine if league name is unique in the database

        // addListenerForSingleValueEvent will read from the database exactly once when the listener is created
        // here we try to read a league with the new league's name from the database,
        // if there is such a league, the new league doesn't have a unique name
        database.child("Leagues").child(newLeague.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // a league with the new league's name already exists, cannot create a new league with this name
                    throw new IllegalStateException("League creation failed; league with name: " + newLeague.getName() + " already exists");
                }
                else{
                    // add new league to the database
                    database.child("Leagues").child(newLeague.getName()).setValue(newLeague);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // database read has failed for some reason, display error message
                throw new DatabaseException("Failed to read league information from database: " + databaseError.getMessage());
            }
        });
    }


    
}
