package com.zizzle.cmpt370.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
     */
    public static void writeLeague(League newLeague) {
        LeagueInfo newLeagueInfo = new LeagueInfo(newLeague);
        // assume this league has a unique name, write it to the database
        database.child("Leagues").child(newLeagueInfo.getDatabaseKey()).setValue(newLeague);
        // write this league to the owner of the league on the database
        MemberInfo leagueOwner = newLeague.getOwnerInfo();
        database.child("users").child(leagueOwner.getDatabaseKey()).child("ownedLeaguesInfoMap").child(newLeagueInfo.getDatabaseKey()).setValue(newLeagueInfo);
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
     * Updates the input Member object to now have the input display name, all parts of the database involving the
     * input member are updated, including MemberInfos stored by teams and for team owners
     *
     * @param member:  Member object member to be updated
     * @param newName: String new name for this member
     */
    public static void updateDisplayName(Member member, final String newName) {
        final MemberInfo currentMemberInfo = new MemberInfo(member);
        // add this new name for the user
        database.child("users").child(currentMemberInfo.getDatabaseKey()).child("displayName").setValue(newName);
        // update the display name stored in each team the user is a part of
        for (final TeamInfo memberTeamInfo : member.getTeamsInfo()) {
            database.child("Teams").child(memberTeamInfo.getDatabaseKey()).child("membersInfoMap").child(currentMemberInfo.getDatabaseKey()).child("name").setValue(newName);
            // if the user is the owner of this team, we must also update the owner info stored for this team to reflect the new name
            database.child("Teams").child(memberTeamInfo.getDatabaseKey()).child("ownerInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MemberInfo ownerInfo = dataSnapshot.getValue(MemberInfo.class);
                    if (currentMemberInfo.equals(ownerInfo)) {
                        // update the display name of this owner entry
                        database.child("Teams").child(memberTeamInfo.getDatabaseKey()).child("ownerInfo").child("name").setValue(newName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        // update the display name stored for the owner of each league this user owns
        for (final LeagueInfo ownedLeagueInfo : member.getOwnedLeaguesList()) {
            database.child("Leagues").child(ownedLeagueInfo.getDatabaseKey()).child("ownerInfo").child("name").setValue(newName);
        }
    }

    /**
     * Stores the new phone number for the input member on the database
     *
     * @param memberInfo:     MemberInfo object represented the Member to be updated
     * @param newPhoneNumber: String new phone number for this member, this phone number is assumed to have correct format
     */
    public static void updatePhoneNumber(MemberInfo memberInfo, String newPhoneNumber) {
        // store this new phone number for the user on the database
        database.child("users").child(memberInfo.getDatabaseKey()).child("phoneNumber").setValue(newPhoneNumber);
    }

    /**
     * Stores the new email for the input member on the database
     *
     * @param memberInfo: MemberInfo object represented the Member to be updated
     * @param newEmail:   String new email for this member
     */
    public static void updateEmail(MemberInfo memberInfo, String newEmail) {
        // store this email for the user on the database
        database.child("users").child(memberInfo.getDatabaseKey()).child("email").setValue(newEmail);
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
     * Removes the input Game from the teams playing in the game on the database
     *
     * @param game: Game object to be removed from the teams playing in this game
     */
    public static void removeGameFromTeams(Game game) {
        TeamInfo team1Info = game.getTeam1Info();
        TeamInfo team2Info = game.getTeam2Info();
        // remove this game from the first team playing
        database.child("Teams").child(team1Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).removeValue();
        // remove this game from the second team
        database.child("Teams").child(team2Info.getDatabaseKey()).child("scheduledGames").child(game.getDatabaseKey()).removeValue();
    }


    /**
     * Stores the input Game object under the input teams on the database
     *
     * @param game: Game object denoting game to store
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
     *
     * @param game: Game object that has been played
     * @throws IllegalArgumentException if the input game hasn't been played
     */
    public static void writePlayedGame(Game game) throws IllegalArgumentException {
        if (!game.isPlayed()) {
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
        final Transaction.Handler incrementHandler = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    // if an object has none of a stat recorded set its value to 1
                    mutableData.setValue(1);
                } else {
                    // otherwise increment the attribute for this object
                    mutableData.setValue((Long) mutableData.getValue() + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (!committed) { //error occurred
                    //keep method (may prevent crash)
                }
            }
        };

        // ValueEventListener that reads all the members in for a particular team, and increments the wins of these members
        ValueEventListener teamMemberWinIncrementListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Team winningTeam = dataSnapshot.getValue(Team.class);
                // increment the wins for each member on this team
                for (MemberInfo teamMember : winningTeam.getTeamMembersInfo()) {
                    database.child("users").child(teamMember.getDatabaseKey()).child("careerWins").runTransaction(incrementHandler);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // ValueEventListener that reads all the members in for a particular team, and increments the losses of these members
        ValueEventListener teamMemberLossIncrementListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Team winningTeam = dataSnapshot.getValue(Team.class);
                // increment the losses for each member on this team
                for (MemberInfo teamMember : winningTeam.getTeamMembersInfo()) {
                    database.child("users").child(teamMember.getDatabaseKey()).child("careerLosses").runTransaction(incrementHandler);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // ValueEventListener that reads all the members in for a particular team, and increments the ties of these members
        ValueEventListener teamMemberTieIncrementListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Team winningTeam = dataSnapshot.getValue(Team.class);
                // increment the ties for each member on this team
                for (MemberInfo teamMember : winningTeam.getTeamMembersInfo()) {
                    database.child("users").child(teamMember.getDatabaseKey()).child("careerTies").runTransaction(incrementHandler);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        LeagueInfo currentLeagueInfo = new LeagueInfo(game.getTeam1Info().getLeagueName());
        if (game.getTeam1Score() > game.getTeam2Score()) {
            // team1 has won increment team1's wins and team2's losses
            database.child("Teams").child(team1Info.getDatabaseKey()).child("wins").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("losses").runTransaction(incrementHandler);
            // update the TeamInfo objects stored for this league to reflect the new win by team1
            database.child("Leagues").child(currentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(team1Info.getName()).child("wins").runTransaction(incrementHandler);
            // increment the wins for each member of team1
            database.child("Teams").child(team1Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberWinIncrementListener);
            // increment the losses for each member of team2
            database.child("Teams").child(team2Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberLossIncrementListener);
        } else if (game.getTeam1Score() < game.getTeam2Score()) {
            // team2 has won, increment team2's wins and team1's losses
            database.child("Teams").child(team1Info.getDatabaseKey()).child("losses").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("wins").runTransaction(incrementHandler);
            // update the TeamInfo objects stored for this league to reflect the new win by team2
            database.child("Leagues").child(currentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(team2Info.getName()).child("wins").runTransaction(incrementHandler);
            // increment the wins for each member of team2
            database.child("Teams").child(team2Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberWinIncrementListener);
            // increment the losses for each member of team1
            database.child("Teams").child(team1Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberLossIncrementListener);
        } else {
            // the teams tied, increment the ties for each team
            database.child("Teams").child(team1Info.getDatabaseKey()).child("ties").runTransaction(incrementHandler);
            database.child("Teams").child(team2Info.getDatabaseKey()).child("ties").runTransaction(incrementHandler);
            // increment the wins for each member of team1 and team2
            database.child("Teams").child(team1Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberTieIncrementListener);
            database.child("Teams").child(team2Info.getDatabaseKey()).addListenerForSingleValueEvent(teamMemberTieIncrementListener);
        }
    }


    /**
     * Removes the Team from the database corresponding to the input TeamInfo, any Members on this team are
     * removed, and any teams with games scheduled against this team have these games removed
     *
     * @param teamToDeleteInfo: TeamInfo object representing the Team to be removed
     */
    public static void removeTeam(final TeamInfo teamToDeleteInfo) {
        final String leagueDatabaseKey = teamToDeleteInfo.getLeagueName();

        // read in the specified team
        database.child("Teams").child(teamToDeleteInfo.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Team teamToDelete = dataSnapshot.getValue(Team.class);
                // remove the members from this team
                for (MemberInfo currentMemberInfo : teamToDelete.getTeamMembersInfo()) {
                    removeMemberFromTeam(currentMemberInfo, teamToDeleteInfo);
                }
                // if this team has any scheduled games against other teams, remove these games for the other teams
                for (Game scheduledGame : teamToDelete.getScheduledGameList()) {
                    removeGameFromTeams(scheduledGame);
                }
                // remove this team from the league its a part of
                database.child("Leagues").child(leagueDatabaseKey).child("teamsInfoMap").child(teamToDeleteInfo.getName()).removeValue();
                // remove this team from the database
                database.child("Teams").child(teamToDeleteInfo.getDatabaseKey()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //keep method (may prevent crash)
            }
        });
    }

    /**
     * Removes the league corresponding to the input LeagueInfo and all teams that are a part of this league
     * from the database
     *
     * @param leagueInfo: LeagueInfo object describing the league to be removed from the database
     */
    public static void removeLeague(final LeagueInfo leagueInfo) {
        // read in the league to remove
        database.child("Leagues").child(leagueInfo.getDatabaseKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                League leagueToRemove = dataSnapshot.getValue(League.class);
                // remove the teams from this league
                for (TeamInfo currentTeamInfo : leagueToRemove.getTeamInfos()) {
                    removeTeam(currentTeamInfo);
                }

                // remove this league from its owner
                MemberInfo ownerInfo = leagueToRemove.getOwnerInfo();
                database.child("users").child(ownerInfo.getDatabaseKey()).child("ownedLeaguesInfoMap").child(leagueInfo.getDatabaseKey()).removeValue();

                //remove league
                database.child("Leagues").child(leagueInfo.getDatabaseKey()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //keep method (may prevent crash)
            }
        });


    }


}
