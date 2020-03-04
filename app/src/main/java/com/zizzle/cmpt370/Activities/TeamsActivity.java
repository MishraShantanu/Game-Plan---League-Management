package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class TeamsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Values inside ListView. */
    ArrayList<Team> teams;

    /** Adapter for search bar. */
    ArrayAdapter teamArrayAdapter;

    //main roundedCorners ID of homepageWithMenu.xml
    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggleDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Suppress soft-keyboard until user actually touches the EditTextView
        setContentView(R.layout.activity_teams);

        // add top bar with title 'Teams'
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar); //sets toolbar as action bar

        // TODO 24/02/2020 - Set title to the current team.
        getSupportActionBar().setTitle("Cool League");

        //MENU (button & drawer)
        menuDrawer = findViewById(R.id.teams_layout);
        NavigationView navigationView = findViewById(R.id.teams_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leagues); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        toggleDrawer = new ActionBarDrawerToggle(this, menuDrawer,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        menuDrawer.addDrawerListener(toggleDrawer); //Connects ActionBarDrawerToggle to DrawerLayout
        toggleDrawer.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button


        // add team button =======================================================================

        // launches a pop-up for adding a new class.
        FloatingActionButton addTeam = findViewById(R.id.add_team_button);
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(TeamsActivity.this, TeamsPop.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        // list of teams =========================================================================

        // TESTING - generates a list of teams for testing the displaying functionality.
        // TODO 18/02/2020 - remove this and replace with teams from database.

        teams = new ArrayList<>();
        Member owner = new Member("Tom Holland", "e@mail.gov", "12345678901");
        League league = new League("league",owner,"SQUASH","fun league"); // stub league
        for (int i = 0; i < 20; i++) {
            teams.add(new Team("Team " + i, owner, "SQUASH",league));
        }


        // Display ListView contents.
        teamArrayAdapter = new ArrayAdapter<>(this, R.layout.teams_listview, teams);
        ListView teamList = findViewById(R.id.teams_list);
        teamList.setAdapter(teamArrayAdapter);


        // clicking on a team in the ListView is handled in here.
        teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * performs an action when a ListView item is clicked.
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

                // Team object that was clicked.
                Team clickedTeam = (Team) parent.getAdapter().getItem(listItemPosition);

                // listItemPosition is the array index for the teams array. can be used such as:
                // teams.get(listItemPosition)
                // TODO 18/02/2020 - Give ListView items functionality
                startActivity(new Intent(TeamsActivity.this, TeamActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                Toast.makeText(TeamsActivity.this, "You clicked on " + clickedTeam.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        // functionality for search bar.
        EditText teamsSearchBar = findViewById(R.id.teams_search_bar);
        teamsSearchBar.addTextChangedListener(new TextWatcher() {

            // changes the shown list items based on characters in search bar.
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (TeamsActivity.this).teamArrayAdapter.getFilter().filter(charSequence);
            }

            // these two are not needed for search but must be override.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
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
                Intent toLogOut = new Intent(this, SigninActivity.class);
                toLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toLogOut);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        //close drawer
        menuDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() {
        if (menuDrawer.isDrawerOpen(GravityCompat.START)) { //If drawer (sidebar navigation) is open, close it. START is because menu is on left side (for right side menu, use "END")
            menuDrawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    //Button to open menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //allows menu button to show menu on click
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}