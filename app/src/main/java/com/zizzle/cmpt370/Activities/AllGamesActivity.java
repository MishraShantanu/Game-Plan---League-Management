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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class AllGamesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    TeamInfo teamClicked;

    private ArrayAdapter nextGameArrayAdapter;
    private ArrayAdapter pastGameArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.scores_layout);
        NavigationView navigationView = findViewById(R.id.games_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button


        teamClicked = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");

        // ADD STUFF HERE!!! ==========================================================================
        // NEXT GAMES =========================================================================
        final ArrayList<Game> nextGames = new ArrayList<>();
        final ArrayList<Game> pastGames = new ArrayList<>();

        // read in the list of games for this team
        final TeamInfo currentTeamInfo = (TeamInfo)getIntent().getSerializableExtra("TEAM_INFO");
        DatabaseReference currentTeamReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeamInfo.getDatabaseKey());
        currentTeamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nextGames.clear();
                pastGames.clear();
                DataSnapshot nextGamesData = dataSnapshot.child("scheduledGames");
                if(!nextGamesData.exists()){
                    // this team has no scheduled games
                    // TODO add some text indicating there are no upcoming games
                }
                else{
                    // add each scheduled game to our list
                    for(DataSnapshot gameData : nextGamesData.getChildren()){
                        Game currentGame = gameData.getValue(Game.class);
                        nextGames.add(currentGame);
                    }
                }
                nextGameArrayAdapter.notifyDataSetChanged();

                // do the same for the games previously played by this team
                DataSnapshot pastGamesData = dataSnapshot.child("gamesPlayed");
                if(!pastGamesData.exists()){
                    // TODO add text "no previously played games"
                }
                else{
                    for(DataSnapshot gameData : pastGamesData.getChildren()){
                        Game currentGame = gameData.getValue(Game.class);
                        pastGames.add(currentGame);
                    }
                }
                pastGameArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // Display ListView contents.

        nextGameArrayAdapter = new ArrayAdapter<>(this, R.layout.league_listview, nextGames);
        ListView nextGameList = findViewById(R.id.next_scores_list);
        nextGameList.setAdapter(nextGameArrayAdapter);


        // Display ListView contents.
        pastGameArrayAdapter = new ArrayAdapter<>(this, R.layout.league_listview, pastGames);
        ListView pastGameList = findViewById(R.id.past_scores_list);
        pastGameList.setAdapter(pastGameArrayAdapter);

        // clicking on a scheduled game in the ListView is handled with this listener
        AdapterView.OnItemClickListener gameClickListener = new AdapterView.OnItemClickListener() {

            /**
             * performs an action when a ListView item is clicked.
             *
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

                final Game clickedGame = (Game) parent.getAdapter().getItem(listItemPosition);

                Intent gameIntent = new Intent(AllGamesActivity.this, GameActivity.class);
                // pass the current team info and the game clicked to our GameActivity
                gameIntent.putExtra("GAME_CLICKED",clickedGame);
                gameIntent.putExtra("TEAM_INFO",currentTeamInfo);
                startActivity(gameIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };

        nextGameList.setOnItemClickListener(gameClickListener);
        pastGameList.setOnItemClickListener(gameClickListener);

        // add game button =======================================================================
        final FloatingActionButton addGame = findViewById(R.id.add_game_button);

        // only display this button to users on the selected team
        MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
        DatabaseReference currentMemberReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(teamClicked.getDatabaseKey()).child("membersInfoMap").child(currentUserInfo.getDatabaseKey());
        currentMemberReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    // user isn't on the team, don't display the add game button
                    addGame.hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(AllGamesActivity.this, GamePop.class);
                gameIntent.putExtra("TEAM_INFO", teamClicked);
                startActivity(gameIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
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