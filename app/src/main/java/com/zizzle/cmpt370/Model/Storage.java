package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.InputMismatchException;


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

    // constants to be used when updating fields of the database

    // constants that should be used to update Leagues
    /** constant that should be used if the description of a league is being updated */
    public static final String LEAGUE_DESCRIPTION = "description";

    /** constant that should be used if the sport of a league is being updated */
    public static final String LEAGUE_SPORT = "sport";

    /** constant that should be used if the name field of a league is being updated */
    public static final String LEAGUE_NAME = "name";

    /** constant that should be used if the owner field of a league is being updated */
    public static final String LEAGUE_OWNER = "ownerInfo";

    /** constant that should be used if the teams of a league are being updated */
    public static final String LEAGUE_TEAMS = "teamsInfo";

    // constants that should be used to update Teams
    /** constant that should be used if the name of a team is being updated */
    public static final String TEAM_NAME = "name";

    /** constant that should be used if the sport of a team is being updated */
    public static final String TEAM_SPORT = "sport";

    /** constant that should be used if the members of a team are being updated */
    public static final String TEAM_MEMBER = "membersInfo";

    /** constant that should be used if the owner of a team is being updated */
    public static final String TEAM_OWNER = "ownerInfo";

    /** constant that should be used if the wins of a team are being updated */
    public static final String TEAM_WINS = "wins";

    /** constant that should be used if the losses of a team are being updated */
    public static final String TEAM_LOSSES = "losses";

    /** constant that should be used if the ties of a team are being updated */
    public static final String TEAM_TIES = "ties";

    // TODO add fields that allow you to change games

    // constants that should be used to update Members
    /** constant that should be updated if the name of a member is being updated */
    public static final String MEMBER_NAME = "displayName";

    /** constant that should be updated if the email of a member is being updated */
    public static final String MEMBER_EMAIL = "email";

    /** constant that should be updated if the phone number of a member is being updated */
    public static final String MEMBER_PHONE_NUMER = "phoneNumber";

    /** constant that should be updated if the teams of a member are being updated */
    public static final String MEMBER_TEAMS = "teamsInfo";

    /** constant that should be updated if the leagues of a member are being updated */
    public static final String MEMBER_LEAGUES = "leaguesInfo";










    /**
     * Reads the League with the input name from the database
     * @param leagueInfo: String, name of the database to read from the database
     * @return League object with the name input or null if no such league exists
     * @throws DatabaseException if the database read fails
     */
    public static League readLeague(LeagueInfo leagueInfo) throws DatabaseException{
        // addListenerForSingleValueEvent reads from the database exactly once
        Log.d("league database key",":"+leagueInfo.getDatabaseKey()+":");
        database.child("Leagues").child(leagueInfo.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        LeagueInfo newLeagueInfo = new LeagueInfo(newLeague);
        // determine if league name is unique in the database, try to read in a league with this name
        if(readLeague(newLeagueInfo)!=null){
            // there is already a league with this league's name, cannot create a league with a duplicate name
            throw new IllegalStateException("League creation failed, league with name: " + newLeague.getName() + " already exists");
            // TODO: could also only enforce that a league name must be unique for a sport only, not globally
        }
        else{
            // this league has a unique name, write it to the database
            database.child("Leagues").child(newLeagueInfo.getDatabaseKey()).setValue(newLeague);
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


    /**
     * Adds the input team to the database
     * @param newTeam: Team to be added to the database
     */
    public static void writeTeam(Team newTeam){
        // assume newTeam already has a unique name for the league it's in
        TeamInfo newTeamInfo = new TeamInfo(newTeam);
        // write the team to the database
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeam);

    }


    /**
     * Adds the input member to the database,
     * @param member: Member object to be added to the database
     */
    public static void writeMember(Member member){
        // new member is added at the path /Users/memberEmail/
        // assume the member being added has a unique email address
        database.child("users").child(member.getUserID()).setValue(member);
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
        database.child("users").child(info.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener(){
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

    /**
     * Adds the input team to the input league on the database and vice versa, this assumes that the input team
     * can be validly added to the input league, and that both league and team are already on the database
     * @param parentLeagueInfo: LeagueInfo object to be added to the team
     * @param newTeamInfo: TeamInfo object to be added to the league
     */
    public static void addTeamToLeague(LeagueInfo parentLeagueInfo, TeamInfo newTeamInfo){
        // assume the input team is valid to add to this league
        // add the team to the league, and the league to the team
        database.child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child(newTeamInfo.getName()).setValue(newTeamInfo);
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).child(parentLeagueInfo.getName()).setValue(parentLeagueInfo);
    }

    // TODO method to add a member to a team and league on the database, if a user joins a team, this team must also be added to the member on the database

    /**
     * Removes the input Member from the input team, the Member cannot be owner of this team, the input team
     * is removed from the input member and the member is deleted from the team
     * @param memberToRemove: Member object, member to be removed from the team
     * @param team: Team object, team to remove member from
     * @throws IllegalArgumentException if memberToRemove is the owner of the team, use removeTeamFromLeague()
     * when this team has only an owner to remove the team
     */
    public static void removeMemberFromTeam(Member memberToRemove, Team team){
        // memberToRemove cannot be removed if this member is the owner of the team
        if(team.getOwner().equals(memberToRemove)){
            throw new IllegalArgumentException("Cannot remove owner from team: " + team);
        }
        MemberInfo memberToRemoveInfo = new MemberInfo(memberToRemove);
        TeamInfo teamInfo = new TeamInfo(team);
        // remove memberToRemove from the input team
        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfo").child(memberToRemoveInfo.getDatabaseKey()).removeValue();
        // remove team from memberToRemove
        database.child("users").child(memberToRemoveInfo.getDatabaseKey()).child("teamsInfo").child(teamInfo.getDatabaseKey()).removeValue();
        // TODO what happens if member isn't on the input team
    }


}
