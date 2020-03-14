package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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

    // constants to be used when updating fields of the database

    // constants that should be used to update Leagues
    /** constant that should be used if the description of a league is being updated */
    public static final String LEAGUE_DESCRIPTION = "description";

    /** constant that should be used if the sport of a league is being updated */
    public static final String LEAGUE_SPORT = "sport";

    /** constant that should be used if the name field of a league is being updated */
    public static String LEAGUE_NAME = "name";

    /** constant that should be used if the owner field of a league is being updated */
    public static final String LEAGUE_OWNER = "ownerInfo";

    /** constant that should be used if the teams of a league are being updated */
    public static final String LEAGUE_TEAMS = "teamsInfo";

    // constants that should be used to update Teams
    /** constant that should be used if the name of a team is being updated */
    public static  String TEAM_NAME = "name";

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
    public static void writeLeague(League newLeague){
        LeagueInfo newLeagueInfo = new LeagueInfo(newLeague);
        // assume this league has a unique name, write it to the database
        database.child("Leagues").child(newLeagueInfo.getDatabaseKey()).setValue(newLeague);
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
     * Adds the input team to the input league on the database, this assumes that the input team
     * can be validly added to the input league, and that both league and team are already on the database
     * @param parentLeagueInfo: LeagueInfo object the team is being added to
     * @param newTeamInfo: TeamInfo object to be added to the league
     */
    public static void addTeamToLeague(LeagueInfo parentLeagueInfo, TeamInfo newTeamInfo){
        // assume the input team is valid to add to this league
        // add the team to the league, and the league to the team
        database.child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(newTeamInfo.getName()).setValue(newTeamInfo);
        // we don't have to add the league to the team, as the team already keeps track of its parent league
    }

    /**
     * Adds the input member to the input team and vice versa on the database, this assumes that the input
     * member can and should be added to the input team
     * @param teamMemberInfo: MemberInfo object describing the member to be added to the input team
     * @param newTeamInfo: TeamInfo object describing the team to add the input member to
     */
    public static void addTeamToMember(MemberInfo teamMemberInfo, TeamInfo newTeamInfo){
        // add the team to the member
        database.child("users").child(teamMemberInfo.getDatabaseKey()).child("teamInfoMap").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
        // add the member to the team
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).child("membersInfoMap").child(teamMemberInfo.getDatabaseKey()).setValue(teamMemberInfo);
    }

    /**
     * Removes the input member from the input team on the database, this is a no-op if the member isn't on the input team
     * @param memberInfo: MemberInfo object representing the member to remove from the team
     * @param teamInfo: TeamInfo object representing the team to remove from the member
     */
    public static void removeMemberFromTeam(MemberInfo memberInfo, TeamInfo teamInfo){
        // remove the team from the member
        database.child("users").child(memberInfo.getDatabaseKey()).child("teamInfoMap").child(teamInfo.getDatabaseKey()).removeValue();
        // remove the member from the team
        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfoMap").child(memberInfo.getDatabaseKey()).removeValue();
    }




    /**
     * Stores the input Game object under the input teams on the database
     * @param team1Info: TeamInfo object representing the first team playing in this game
     * @param team2Info: TeamInfo object representing the second team playing in this game
     * @param game: Game object denoting game to store
     */
    public static void addGameToTeams(TeamInfo team1Info, TeamInfo team2Info, Game game){
        // TODO check that these input teams are actually playing in this game, check that this game hasn't been played yet etc
        String gameDatabaseKey = game.getDatabaseKey();
        // add this game to team1
        database.child("Teams").child(team1Info.getDatabaseKey()).child("scheduledGames").child(gameDatabaseKey).setValue(game);
        // add this game to team2
        database.child("Teams").child(team2Info.getDatabaseKey()).child("scheduledGames").child(gameDatabaseKey).setValue(game);
    }


    /**
     * Removes the input member from the input team on the database, this is a no-op if the member isn't on the input team
     *
     * @param teamInfo: TeamInfo object representing the team to remove from the member
     */
    public static void removeTeam(TeamInfo teamInfo){
        // remove team from members teams

        TEAM_NAME = teamInfo.getDatabaseKey();

        LEAGUE_NAME = teamInfo.getLeagueName();

        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfoMap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()
                ) {

                    database.child("users").child(ds.getKey()).child("teamInfoMap").child(TEAM_NAME).removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // remove the member from the team

        database.child("Teams").child(teamInfo.getDatabaseKey()).removeValue();

        database.child("Leagues").child(LEAGUE_NAME).child("teamsInfoMap").child(teamInfo.getName()).removeValue();

    }


}
