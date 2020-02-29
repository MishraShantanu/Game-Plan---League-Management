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
    /** Stores the member read in, we require this to overcome scoping issues */
    private static Member retrievedMember;
    /** Stores the team read in, we require this to overcome scoping issues */
    private static Team retrievedTeam;


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

    /**
     * Retrieves the Team object from the database that has info corresponding to that input
     * @param info: TeamInfo object containing info of the Team to read in
     * @return Team read from the database, may be null if there is no team on the database with the info input
     * @throws DatabaseException if database access fails
     */
    public static Team readTeam(TeamInfo info) throws DatabaseException{
        // ListenerForSingleValueEvent will read from the database exactly once
        database.child("Teams").child(info.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // called when data is read from database
                retrievedTeam = dataSnapshot.getValue(Team.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // database read has failed for some reason, display error message
                throw new DatabaseException("Failed to read team information from database: " + databaseError.getMessage());
            }
        });
        // return the team read from the database, may be null if there is no team associated with the input info
        return retrievedTeam;
    }


    public static void writeTeam(Team newTeam){
        // assume newTeam already has a unique name for the league it's in
        // use a TeamInfo object to get the key to store the Team with
        TeamInfo newTeamInfo = new TeamInfo(newTeam);
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeam);

        LeagueInfo parentLeagueInfo = newTeam.getLeagueInfo();
        // add this new team to the parent league in the database
        database.child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child("teamInfo").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);

        // add this new team to each member of the team on the database
        for(MemberInfo currentMemberInfo : newTeam.getTeamMembersInfo()){
            database.child("Users").child("teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
        }
    }


    /**
     * Adds the input member to the database,
     * @param member: Member object to be added to the database
     */
    public static void writeMember(Member member){
        // new member is added at the path /Users/memberEmail/
        // assume the member being added has a unique email address
        database.child("Users").child(member.getEmail()).setValue(member);
        // TODO if member is part of any teams, add these to the new member on database
    }


    /**
     * Retrieves the Member object from the database that corresponds to the MemberInfo input
     * @param info: MemberInfo object containing the information of the member to read in
     * @return Member read from the database that corresponds to the MemberInfo input, may be null if
     * no Member corresponds to the input MemberInfo
     * @throws DatabaseException if accessing the database fails
     */
    public static Member readMember(MemberInfo info) throws DatabaseException{
        // the singleValueEvent listener will read from the database exactly once
        database.child("Users").child(info.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // called when data is read from database
                retrievedMember = dataSnapshot.getValue(Member.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // database read has failed for some reason, display error message
                throw new DatabaseException("Failed to read member information from database: " + databaseError.getMessage());
            }
        });
        // return the member read from the database, may be null if there is no member corresponding to the input info
        return retrievedMember;
    }

    // TODO method to add a member to a team and league on the database, if a user joins a team, this team must also be added to the member on the database

}
