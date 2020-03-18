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
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

import static com.zizzle.cmpt370.Model.CurrentUserInfo.getCurrentUserInfo;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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



        final EditText currentTeamScoreText = findViewById(R.id.yourScore);
        final EditText opponentTeamScoreText = findViewById(R.id.opponentScore);
        TextView currentTeamText = findViewById(R.id.yourTeamNameText);
        TextView opponentTeamText = findViewById(R.id.opponentTeamNameText);
        TextView gameDateText = findViewById(R.id.gameDateText);
        TextView gameTimeText = findViewById(R.id.gameTimeText);
        TextView gameLocationText = findViewById(R.id.locationText);


        final Game currentGame = (Game)getIntent().getSerializableExtra("GAME_CLICKED");
        final TeamInfo currentTeamInfo = (TeamInfo)getIntent().getSerializableExtra("TEAM_INFO");
        // set the fields for this page
        currentTeamText.append(currentTeamInfo.getName());
        gameDateText.append(currentGame.getGameTime().getDateString());
        gameTimeText.append(currentGame.getGameTime().getClockTime());
        gameLocationText.append(currentGame.getLocation());

        // determine if our current team is team1 or 2 of this game
        if(currentGame.getTeam1Info().equals(currentTeamInfo)){
            // current team is team1 in this game
            currentTeamScoreText.setText(String.valueOf(currentGame.getTeam1Score()));
            opponentTeamText.append(currentGame.getTeam2Info().getName());
            opponentTeamScoreText.setText(String.valueOf(currentGame.getTeam2Score()));
        }
        else{
            // current team is team2 in this game
            currentTeamScoreText.setText(String.valueOf(currentGame.getTeam2Score()));
            opponentTeamText.append(currentGame.getTeam1Info().getName());
            opponentTeamScoreText.setText(String.valueOf(currentGame.getTeam1Score()));
        }

        // only allow the user to change the score fields if the game has started, this prevents users from resolving a game that hasn't been played yet
        if(currentGame.hasGameStarted()){
            currentTeamScoreText.setEnabled(true);
            opponentTeamScoreText.setEnabled(true);
            // display the button to submit score changes
            Button submitButton = findViewById(R.id.submitScore);
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO possibly display some popup asking the user to confirm the final scores for this game
                    // get the input scores for this game
                    int currentTeamScore = Integer.valueOf(currentTeamScoreText.getText().toString());
                    int opponentTeamScore = Integer.valueOf(opponentTeamScoreText.getText().toString());
                    // the order we input scores into this game depends on whether the current team is team1 or 2 of this game
                    if(currentTeamInfo.equals(currentGame.getTeam1Info())){
                        // current team is team1
                        currentGame.setGameAsPlayed(currentTeamScore,opponentTeamScore);
                    }
                    else{
                        // current team is team2
                        currentGame.setGameAsPlayed(opponentTeamScore,currentTeamScore);
                    }
                    // add this played game to the database
                    
                }
            });
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
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
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