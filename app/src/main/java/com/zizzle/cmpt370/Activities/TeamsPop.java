package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

public class TeamsPop extends Activity {

    EditText teamName;
    Button submitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.teams_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.4));


        // Gathering Input =========================================================================
        teamName  = findViewById(R.id.teamName);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameOfTeam = teamName.getText().toString();
                final String sportForTeam = "TEMP SPORT"; /////TODO ADD SPORT FOR TEAM

                if (nameOfTeam.isEmpty()) {
                    Toast.makeText(TeamsPop.this, "Team name is required", Toast.LENGTH_SHORT).show();
                }

                else {
                    final MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
                    Bundle extras = getIntent().getExtras();
                    if(extras != null){
                        // get the current league name
                        final String currentLeagueName = extras.getString("CURRENT_LEAGUE_NAME");
                        final LeagueInfo parentLeagueInfo = new LeagueInfo(currentLeagueName);
                        int initialWins = 0;
                        final TeamInfo newTeamInfo = new TeamInfo(nameOfTeam,currentLeagueName,initialWins);
                        // check if the input team name is unique for this league
                        DatabaseReference newTeamReference = FirebaseDatabase.getInstance().getReference().child("Leagues").child(parentLeagueInfo.getDatabaseKey()).child("teamsInfoMap").child(newTeamInfo.getName());
                        newTeamReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // called to read data, here we should retrieve null from the database if team name is unique
                                boolean teamAlreadyExists = dataSnapshot.exists();
                                if(teamAlreadyExists){
                                    // team doesn't have a unique name
                                    Toast.makeText(TeamsPop.this, "Team creation failed, team with name '" + nameOfTeam + "' already exists in this league", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    // team name is unique, create and add this team to the database
                                    Team newTeam = new Team(nameOfTeam, currentUserInfo, sportForTeam, parentLeagueInfo);
                                    Storage.writeTeam(newTeam);
                                    // add this new team to its parent league and vice versa on the database
                                    Storage.addTeamToLeague(parentLeagueInfo,newTeamInfo);
                                    // add the current user to this team, and this team to the current user
                                    Storage.addTeamToMember(currentUserInfo,newTeamInfo);

                                    // close this pop-up activity
                                    finish();

                                    // intent to the new league page.
                                    Intent teamsIntent = new Intent(TeamsPop.this, TeamActivity.class);
                                    // pass the name of the league clicked on to this intent, so it can be accessed from the TeamsActivity

                                    teamsIntent.putExtra("TEAM_INFO_CLICKED", newTeamInfo);
                                    startActivity(teamsIntent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // called when database operations fail
                                Toast.makeText(TeamsPop.this, "Team creation failed, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
