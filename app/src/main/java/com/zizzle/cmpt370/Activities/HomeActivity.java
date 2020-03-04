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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Values inside ListView. */
    ArrayList<Team> teams;

    /** Adapter for displaying teams */
    ArrayAdapter teamArrayAdapter;

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar
        getSupportActionBar().setTitle("Home");


        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);
        NavigationView navigationView = findViewById(R.id.home_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button


        // list of teams =========================================================================

        // TESTING - generates a list of teams for testing the displaying functionality.
        // TODO Feb. 26, 2020 - remove this and replace with teams that a user is in from the database.

        teams = new ArrayList<>();
        Member user = new Member("Elon Musk", "ironman@xyz.com", "12312312341","uid12334");
        Member owner = new Member("Pope Francis", "rome@popemobile.com", "15935774125","uid543456");
        League league = new League("Tennis Club", owner, "Tennis", "The tennis club for future World Number 1s");
        for (int i = 0; i < 20; i++) {
            teams.add(new Team("Team-Name " + i, user, "Tennis", league));
        }


        // creates a ArrayList<String> from ArrayList<Team> in order to display the names to user.
        // TODO Feb. 26, 2020 - replace teams with the the ones from the database.

        ArrayList<String> team_names = new ArrayList<>();
        for (Team team : teams) {
            team_names.add(team.getName());
        }


        // Display ListView contents.
        teamArrayAdapter = new ArrayAdapter<>(this, R.layout.home_listview, team_names);
        ListView teamList = findViewById(R.id.user_individual_teams_list);
        teamList.setAdapter(teamArrayAdapter);


        // clicking on a team in the ListView is handled in here.
        teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * performs an action when a ListView item is clicked.
             *
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int listItemPosition, long id) {

                // listItemPosition is the array index for the teams array. can be used such as:
                // teams.get(listItemPosition)
                // TODO Feb. 26, 2020 - Give ListView items functionality

                startActivity(new Intent(HomeActivity.this, TeamActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                // this was used for testing. can be removed later.
                Toast.makeText(HomeActivity.this, "You just clicked " + listItemPosition, Toast.LENGTH_SHORT).show();
            }
        });
        Member testOwner = new Member("jonny test","jhon@mail","13066999999","UID7865432456789");
        League testWrite = new League("testWrite",testOwner,"testing","testing");
        Storage.writeLeague(testWrite);
        League testRead = Storage.readLeague(new LeagueInfo(testWrite));


    }

    //When item is selected in the menu, open the respective element (fragment or activity)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                // already on home page, do nothing
                break;
            case R.id.nav_leagues:
                startActivity(new Intent(this, LeagueActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SigninActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        } else{
            super.onBackPressed();
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