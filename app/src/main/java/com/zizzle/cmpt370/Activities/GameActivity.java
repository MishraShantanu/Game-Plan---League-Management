package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu


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


        // NEXT GAMES =========================================================================
        ArrayList<String> nextGames = new ArrayList<>();
        ArrayList<String> pastGames = new ArrayList<>();

        // read in the current team object


        // Display ListView contents.

        ArrayAdapter nextGameArrayAdapter = new ArrayAdapter<>(this, R.layout.league_listview, nextGames);
        ListView nextGameList = findViewById(R.id.next_scores_list);
        nextGameList.setAdapter(nextGameArrayAdapter);


        // clicking on a league in the ListView is handled in here.
        nextGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * performs an action when a ListView item is clicked.
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

//                 name of the league that was clicked.
//                final String clickedLeagueName = (String) parent.getAdapter().getItem(listItemPosition);
//
//                Intent teamsIntent = new Intent(LeagueActivity.this, TeamsActivity.class);
//                // pass the name of the league clicked on to this intent, so it can be accessed from the TeamsActivity
//                teamsIntent.putExtra("LEAGUE_CLICKED",clickedLeagueName);
//                startActivity(teamsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // PAST GAMES =========================================================================


        // Display ListView contents.
        ArrayAdapter pastGameArrayAdapter = new ArrayAdapter<>(this, R.layout.league_listview, pastGames);
        ListView pastGameList = findViewById(R.id.past_scores_list);
        nextGameList.setAdapter(pastGameArrayAdapter);


        // clicking on a league in the ListView is handled in here.
        nextGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * performs an action when a ListView item is clicked.
             *
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

//                 name of the league that was clicked.
//                final String clickedLeagueName = (String) parent.getAdapter().getItem(listItemPosition);
//
//                Intent teamsIntent = new Intent(LeagueActivity.this, TeamsActivity.class);
//                // pass the name of the league clicked on to this intent, so it can be accessed from the TeamsActivity
//                teamsIntent.putExtra("LEAGUE_CLICKED",clickedLeagueName);
//                startActivity(teamsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    //When item is selected in the menu, open the respective element (fragment or activity)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent toHome = new Intent(GameActivity.this, HomeActivity.class);
                toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toHome);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_leagues:
                startActivity(new Intent(GameActivity.this, LeagueActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(GameActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(GameActivity.this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                Intent toLogOut = new Intent(GameActivity.this, SigninActivity.class);
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