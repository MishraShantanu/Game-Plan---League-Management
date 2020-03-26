package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

import static com.zizzle.cmpt370.Model.CurrentUserInfo.getCurrentUserInfo;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Game currentGame;
    TeamInfo currentTeamInfo;

    String currentTeamScoreString;
    String opponentTeamScoreString;

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.game_layout);
        NavigationView navigationView = findViewById(R.id.game_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button

        currentGame = (Game)getIntent().getSerializableExtra("GAME_CLICKED");
        currentTeamInfo = (TeamInfo)getIntent().getSerializableExtra("TEAM_INFO");

        final TextView currentTeamText = findViewById(R.id.yourTeamNameText);
        TextView opponentTeamText = findViewById(R.id.opponentTeamNameText);
        TextView gameDateText = findViewById(R.id.gameDateText);
        TextView gameTimeText = findViewById(R.id.gameTimeText);
        TextView gameLocationText = findViewById(R.id.locationText);

        final EditText currentTeamScoreText = findViewById(R.id.yourScore);
        final EditText opponentTeamScoreText = findViewById(R.id.opponentScore);

        // set the fields for this page
        currentTeamText.setText(currentTeamInfo.getName());
        gameDateText.append(currentGame.getGameTime().getDateString());
        gameTimeText.append(currentGame.getGameTime().getClockTime());
        gameLocationText.append(currentGame.getLocation());

        // determine if our current team is team1 or 2 of this game
        // current team is team1 in this game
        if (currentGame.getTeam1Info().equals(currentTeamInfo))
            opponentTeamText.setText(currentGame.getTeam2Info().getName());

        // current team is team2 in this game
        else opponentTeamText.setText(currentGame.getTeam1Info().getName());


        Button submitButton = findViewById(R.id.submitScore);

        // only allow the user to change the score fields if the game has started and hasn't already been played
        // this restricts a user so they can only set the scores for a game once after the game has started
        if(currentGame.hasGameStarted() && !currentGame.isPlayed()){
            currentTeamScoreText.setEnabled(true);
            opponentTeamScoreText.setEnabled(true);
            // display the button to submit score changes
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO possibly display some popup asking the user to confirm the final scores for this game
                    // get the input scores for this game
                    currentTeamScoreString = currentTeamScoreText.getText().toString();
                    opponentTeamScoreString = opponentTeamScoreText.getText().toString();
                    // ensure that the user has entered scores for both teams
                    if(currentTeamScoreString.isEmpty()){
                        // prompt the user to enter a score for this team
                        currentTeamScoreText.setError("Score Required");
                        currentTeamScoreText.requestFocus();
                    }
                    else if(opponentTeamScoreString.isEmpty()){
                        // prompt the user to enter the opponent's score
                        opponentTeamScoreText.setError("Score Required");
                        opponentTeamScoreText.requestFocus();
                    }
                    else{

                        Intent confirmIntent = new Intent(GameActivity.this, ScoreConfirmPop.class);
                        confirmIntent.putExtra("TEAM_NAME", currentTeamInfo.getName());
                        confirmIntent.putExtra("OPPONENT_NAME", currentGame.getTeam2Info().getName());
                        confirmIntent.putExtra("TEAM_SCORE", currentTeamScoreString);
                        confirmIntent.putExtra("OPPONENT_SCORE", opponentTeamScoreString);
                        startActivityForResult(confirmIntent, 2);
                    }
                }
            });
        }
        else{
            // the game hasn't started or has already been played, prevent the user from changing the score fields and seeing the submit button
            currentTeamScoreText.setEnabled(false);
            opponentTeamScoreText.setEnabled(false);

            // Display the score for each team, depending which position the teams are.
            // User team is team 1
            if (currentGame.getTeam1Info().equals(currentTeamInfo)) {
                currentTeamScoreText.setText(String.valueOf(currentGame.getTeam1Score()));
                opponentTeamScoreText.setText(String.valueOf(currentGame.getTeam2Score()));
            }
            // User team is team 2
            else {
                currentTeamScoreText.setText(String.valueOf(currentGame.getTeam2Score()));
                opponentTeamScoreText.setText(String.valueOf(currentGame.getTeam1Score()));
            }

            // display the button to submit score changes
            submitButton.setVisibility(View.GONE);
        }


        // remove game button =======================================================================
        final Button removeGame = findViewById(R.id.remove_game_button);

        // Remove game when this button is clicked.
        removeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Storage.removeGameFromTeams(currentGame);

                // Go back to the last activity when deleted.
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // Hide the button if the user is not on the team.
        MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
        DatabaseReference currentMemberReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeamInfo.getDatabaseKey()).child("membersInfoMap").child(currentUserInfo.getDatabaseKey());
        currentMemberReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If game is played don't display
                if (currentGame.isPlayed())
                    removeGame.setVisibility(View.GONE);

                // user isn't on the team, don't display the remove game button
                if(!dataSnapshot.exists())
                    removeGame.setVisibility(View.GONE);
            }

            // Auto Generated.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Request code 2 is for score confirmation
        if (requestCode == 2) {
            String result = data.getStringExtra("RESULT");

            // User confirmed the score.
            if (result.equals("true")) {
                // Value of the scores
                int currentTeamScore = Integer.valueOf(currentTeamScoreString);
                int opponentTeamScore = Integer.valueOf(opponentTeamScoreString);

                // the order we input scores into this game depends on whether the current team is team1 or 2 of this game
                // current team is team1
                if (currentTeamInfo.equals(currentGame.getTeam1Info()))
                    currentGame.setGameAsPlayed(currentTeamScore,opponentTeamScore);

                // current team is team2
                else currentGame.setGameAsPlayed(opponentTeamScore,currentTeamScore);

                // add this played game to the database
                Storage.writePlayedGame(currentGame);

                finish();
            }
        }
    }


    //When item is selected in the menu, open the respective element (fragment or activity)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent toHome = new Intent(this, HomeActivity.class);
                toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toHome);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_leagues:
                startActivity(new Intent(this, LeagueActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_profile:
                startActivityForResult(new Intent(this, ProfileActivity.class), 2);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                // clear the info stored for this user
                CurrentUserInfo.refreshMemberInfo();
                Intent toLogOut = new Intent(this, SigninActivity.class);
                toLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toLogOut);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        //close drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) { //If drawer (sidebar navigation) is open, close it. START is because menu is on left side (for right side menu, use "END")
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    //Button to open menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //allows menu button to show menu on click
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}