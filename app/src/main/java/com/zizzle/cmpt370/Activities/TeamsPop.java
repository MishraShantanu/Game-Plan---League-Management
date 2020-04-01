package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

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
                hideKeyboard(TeamsPop.this);

                final String nameOfTeam = teamName.getText().toString();

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
                        int initialWins = 0; // new team initially has no wins
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
                                    teamName.setError("Team name must be unique");
                                    teamName.requestFocus();
                                }
                                else{
                                    // team name is unique, create and add this team to the database
                                    Team newTeam = new Team(nameOfTeam, currentUserInfo, parentLeagueInfo);
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

    // Close activity if clicked outside of page.
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect dialogBounds = new Rect();
            getWindow().getDecorView().getHitRect(dialogBounds);
            if (!dialogBounds.contains((int) event.getX(), (int) event.getY())) {
                finish();
                return false;
            }
        }
        return false;
    }

    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() {
        finish();
    }

    // Used to hide keyboard.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
