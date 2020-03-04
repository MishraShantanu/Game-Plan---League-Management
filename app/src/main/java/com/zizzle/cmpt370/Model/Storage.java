package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
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
     * Adds the input team to the database, this team is also added to its parent league, and any members
     * present on this team have their database entries updated to reflect this new team
     * @param newTeam: Team to be added to the database
     */
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
            database.child("users").child("teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
        }
    }


    /**
     * Adds the input member to the database,
     * @param member: Member object to be added to the database
     */
    public static void writeMember(Member member){
        // new member is added at the path /Users/memberEmail/
        // assume the member being added has a unique email address
        database.child("users").child(member.getUserID()).setValue(member);
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

    // TODO method to add a member to a team and league on the database, if a user joins a team, this team must also be added to the member on the database

    /**
     * Updates a League's data on the database
     * @param league: league to update
     * @param field: field or attribute of the league to update, these should be one of the constants defined above, LEAGUE_OWNER for example
     * @param newValue: new value for this field or attribute to take, must be an appropriate type for the field being updated
     * @throws InputMismatchException if newValue is an invalid type, for example if updating the owner field, a Member object is expected
     * @throws IllegalArgumentException if field input isn't one of the defined constants
     */
    public static void updateLeagueField(League league, String field, Object newValue) throws InputMismatchException, IllegalArgumentException{
        LeagueInfo leagueInfo = new LeagueInfo(league);
        // ensure the input field is valid
        if(field.equals(LEAGUE_NAME) || field.equals(LEAGUE_DESCRIPTION) || field.equals(LEAGUE_SPORT)){
            // we want to update a String field, make sure newValue is a String
            if(! (newValue instanceof String)){
                // invalid input for these fields
                throw new InputMismatchException("Selected field requires String input");
            }
            // add this new field to the database
            database.child("Leagues").child(leagueInfo.getDatabaseKey()).child(field).setValue(newValue);
        }
        else if(field.equals(LEAGUE_OWNER)){
            // expected input is a Member object
            if(!(newValue instanceof Member)){
                // invalid type of input for this field
                throw new InputMismatchException("Selected field requires Member object input");
            }
            // update database to have this new member, add MemberInfo where required
            MemberInfo newOwnerInfo = new MemberInfo((Member)newValue);
            database.child("Leagues").child(leagueInfo.getDatabaseKey()).child(field).setValue(newOwnerInfo);
            // also add this league to the new owner
            database.child("Users").child(newOwnerInfo.getDatabaseKey()).child("leagues").child(leagueInfo.getDatabaseKey()).setValue(leagueInfo);
        }
        else if(field.equals(LEAGUE_TEAMS)){
            // expected input is a Team object
            if(!(newValue instanceof Team)){
                throw new InputMismatchException("Selected field requires a Team object input");
            }
            // update league on database to have this new team
            TeamInfo newTeamInfo =  new TeamInfo((Team)newValue);
            database.child("Leagues").child(leagueInfo.getDatabaseKey()).child(field).child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
            // also add this league to the new team
            database.child("Teams").child(newTeamInfo.getDatabaseKey()).child("leagueInfo").setValue(leagueInfo);
        }
        else{
            throw new IllegalArgumentException("Input field: " + field + " isn't recognized");
        }
        // TODO uncouple leagues from teams, and leagues from users, update league fields only, call a separate method to update team and user fields
    }

    /**
     * Updates the specified field of the input team on the database, note that team members should only be added
     * with the field TEAM_MEMBER, to remove team members use the removeTeamValue() function
     * @param team: Team object, team to be updated on the database
     * @param field: String field of the team to be changed, should be one of the constants defined in this class, TEAM_NAME for example
     * @param newValue: Object new value for the field being changed, this must have the proper type, for example if the members of a team are being updated
     *                newValue should be a MemberInfo object
     * @throws InputMismatchException if newValue has an invalid type for the field being changed
     * @throws IllegalArgumentException if the input field isn't one of the specified constants
     */
    public static void updateTeamField(Team team, String field, Object newValue) throws InputMismatchException, IllegalArgumentException{
        TeamInfo teamInfo = new TeamInfo(team);
        if(field.equals(TEAM_NAME) || field.equals(TEAM_SPORT)){
            // these fields require string values
            if(!(newValue instanceof String)){
                throw new InputMismatchException("Field: " + field + " requires String values");
            }
        }
        else if(field.equals(TEAM_LOSSES) || field.equals(TEAM_TIES) || field.equals(TEAM_WINS)){
            // these fields require Integer values
            if(!(newValue instanceof Integer)){
                throw new InputMismatchException("Field: " + field + " requires Integer values");
            }
        }
        else if(field.equals(TEAM_MEMBER)){
            // we expect a HashSet<MemberInfo> for this field
            // attempt to cast newValue to the correct type, if this fails, newValue is invalid type
            try{
                HashSet<MemberInfo> newHashSet = (HashSet<MemberInfo>) newValue;
            }catch (ClassCastException e){
                throw new InputMismatchException("Field: " + field + " requires HashSet<MemberInfo>");
            }
        }
        else if(field.equals(TEAM_OWNER)){
            // we expect a MemberInfo object for this field
            if(!(newValue instanceof MemberInfo)){
                throw new InputMismatchException("Field: " + field + " requires MemberInfo values");
            }
        }
        else{
            // invalid field
            throw new IllegalArgumentException("Input field: " + field + " is invalid");
        }
        // write new value to database
        database.child("Teams").child(teamInfo.getDatabaseKey()).child(field).setValue(newValue);
    }


}
