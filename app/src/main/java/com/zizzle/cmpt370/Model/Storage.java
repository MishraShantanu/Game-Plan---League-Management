package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


/**
 * Static class used to read/write from database
 */
public class Storage {
    /**
     * Reference to the database
     */
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    /**
     * Adds the input league to the database
     *
     * @param newLeague: League object to be added to the database, the name of this league must be
     *                   unique
     * @throws IllegalStateException if the name of the league isn't unique
     * @throws DatabaseException     if accessing the database fails
     */
    public static void writeLeague(League newLeague) {
        LeagueInfo newLeagueInfo = new LeagueInfo(newLeague);
        // assume this league has a unique name, write it to the database
        database.child("Leagues").child(newLeagueInfo.getDatabaseKey()).setValue(newLeague);
    }


    /**
     * Adds the input team to the database
     *
     * @param newTeam: Team to be added to the database
     */
    public static void writeTeam(Team newTeam) {
        // assume newTeam already has a unique name for the league it's in
        TeamInfo newTeamInfo = new TeamInfo(newTeam);
        // write the team to the database
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).setValue(newTeam);

    }


    /**
     * Adds the input member to the database,
     *
     * @param member: Member object to be added to the database
     */
    public static void writeMember(Member member) {
        // new member is added at the path /Users/memberEmail/
        // assume the member being added has a unique email address
        database.child("users").child(member.getUserID()).setValue(member);
    }


    /**
     * Adds the input team to the input league on the database, this assumes that the input team
     * can be validly added to the input league, and that both league and team are already on the database
     *
     * @param parentLeagueInfo: LeagueInfo object the team is being added to
     * @param newTeamInfo:      TeamInfo object to be added to the league
     */
    public static void addTeamToLeague(LeagueInfo parentLeagueInfo, TeamInfo newTeamInfo) {
        // assume the input team is valid to add to this league
        // add the team to the league, and the league to the team
        database.child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(newTeamInfo.getName()).setValue(newTeamInfo);
        // we don't have to add the league to the team, as the team already keeps track of its parent league
    }

    /**
     * Adds the input member to the input team and vice versa on the database, this assumes that the input
     * member can and should be added to the input team
     *
     * @param teamMemberInfo: MemberInfo object describing the member to be added to the input team
     * @param newTeamInfo:    TeamInfo object describing the team to add the input member to
     */
    public static void addTeamToMember(MemberInfo teamMemberInfo, TeamInfo newTeamInfo) {
        // add the team to the member
        database.child("users").child(teamMemberInfo.getDatabaseKey()).child("teamInfoMap").child(newTeamInfo.getDatabaseKey()).setValue(newTeamInfo);
        // add the member to the team
        database.child("Teams").child(newTeamInfo.getDatabaseKey()).child("membersInfoMap").child(teamMemberInfo.getDatabaseKey()).setValue(teamMemberInfo);
    }

    /**
     * Removes the input member from the input team on the database, this is a no-op if the member isn't on the input team
     *
     * @param memberInfo: MemberInfo object representing the member to remove from the team
     * @param teamInfo:   TeamInfo object representing the team to remove from the member
     */
    public static void removeMemberFromTeam(MemberInfo memberInfo, TeamInfo teamInfo) {
        // remove the team from the member
        database.child("users").child(memberInfo.getDatabaseKey()).child("teamInfoMap").child(teamInfo.getDatabaseKey()).removeValue();
        // remove the member from the team
        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfoMap").child(memberInfo.getDatabaseKey()).removeValue();
    }


    /**
     * Stores the input Game object under the input teams on the database
     * @param game:      Game object denoting game to store
     */
    public static void writeGame(Game game) {
        TeamInfo team1Info = game.getTeam1Info();
        TeamInfo team2Info = game.getTeam2Info();
        // add this game to team1
        database.child("Teams").child(team1Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).setValue(game);
        // add this game to team2
        database.child("Teams").child(team2Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).setValue(game);
    }

    /**
     * Writes the input played game to the database, updates the records of the teams playing this game to reflect
     * a win, loss or tie of this game
     * @param game: Game object that has been played
     * @throws IllegalArgumentException if the input game hasn't been played
     */
    public static void writePlayedGame(Game game) throws IllegalArgumentException{
        if(!game.hasBeenPlayed()){
            throw new IllegalArgumentException("game " + game + " hasn't been played");
        }
        TeamInfo team1Info = game.getTeam1Info();
        TeamInfo team2Info = game.getTeam2Info();
        // the input game should already be on the database as a scheduled game for team1 and team2
        // remove this game as a scheduled game from these teams
        database.child("Teams").child(team1Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).removeValue();
        database.child("Teams").child(team2Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).removeValue();

        // add this game as a played game to both team1 and team2
        database.child("Teams").child(team1Info.getDatabaseKey()).child("gamesPlayed").child(game.getDatabaseKey()).setValue(game);
        database.child("Teams").child(team2Info.getDatabaseKey()).child("gamesPlayed").child(game.getDatabaseKey()).setValue(game);

        // use a transaction to increment the wins/losses/ties of the teams in this game to avoid race conditions
        Transaction.Handler incrementHandler = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if(mutableData.getValue() == null){
                    // if the team has no wins/losses/ties recorded, set the teams's wins/losses/ties to 1
                    mutableData.setValue(1);
                }
                else{
                    // otherwise increment the wins/losses/ties of this team
                    mutableData.setValue((Integer)mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if(!committed){
                    // TODO an error occured
                }
            }
        };

        if(game.getTeam1Score()>game.getTeam2Score()){
            // team1 has won increment team1's wins and team2's losses
            database.child("Teams").child(team1Info.getDatabaseKey()).child("wins").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("losses").runTransaction(incrementHandler);
        }
        else if(game.getTeam1Score()<game.getTeam2Score()){
            // team2 has won, increment team2's wins and team1's losses
            database.child("Teams").child(team1Info.getDatabaseKey()).child("losses").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("wins").runTransaction(incrementHandler);
        }
        else{
            // the teams tied, increment the ties for each team
            database.child("Teams").child(team1Info.getDatabaseKey()).child("ties").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("ties").runTransaction(incrementHandler);
        }
    }


    /**
     * Removes the input member from the input team on the database, this is a no-op if the member isn't on the input team
     *
     * @param teamInfo: TeamInfo object representing the team to remove from the member
     */
    public static void removeTeam(TeamInfo teamInfo) {
        // remove team from members teams
        final String teamDatabaseKey = teamInfo.getDatabaseKey();

        String leagueDatabaseKey = teamInfo.getLeagueName();

        database.child("Teams").child(teamInfo.getDatabaseKey()).child("membersInfoMap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    database.child("users").child(ds.getKey()).child("teamInfoMap").child(teamDatabaseKey).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // remove the member from the team
        database.child("Teams").child(teamInfo.getDatabaseKey()).removeValue();
        database.child("Leagues").child(leagueDatabaseKey).child("teamsInfoMap").child(teamInfo.getName()).removeValue();
    }


    public static void removeLeague(final String LeagueName) {
        // remove team from members teams


        database.child("Leagues").child(LeagueName).child("teamsInfoMap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    final String teamKey = LeagueName+"-"+ds.getKey();


                     database.child("Teams").child(teamKey).child("ownerInfo").child("databaseKey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                               //remove the ownership of teams from player under league deleted
                                database.child("users").child(dataSnapshot.getKey()).child("teamInfoMap").child(teamKey).removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //remove Teams of a league
                    database.child("Teams").child(teamKey).removeValue();
                }

                //remove league
                database.child("Leagues").child(LeagueName).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



}
