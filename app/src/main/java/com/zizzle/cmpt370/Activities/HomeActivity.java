package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Values inside ListView. */
    ArrayList<TeamInfo> teamsInfo;

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

        teamsInfo = new ArrayList<>();
        final MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
        // read in the current user's teams from the database
        DatabaseReference userTeamsReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserInfo.getDatabaseKey()).child("teamInfoMap");

        userTeamsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called when the user is added to a team, when a user's join request for some team has been accepted for example
                // add this new team to the top of the list of teams the user is a part of
                teamsInfo.add(0,dataSnapshot.getValue(TeamInfo.class));
                teamArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called when one of the teams the user is a part of is changed, for example if its name or members changed
                // do nothing, may want to update a team if its name has changed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // called when the user has been removed from a team, remove this team from the display
                teamsInfo.remove(dataSnapshot.getValue(TeamInfo.class));
                teamArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // called when a team is moved on the database, do nothing
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // called if database operations fail, display some error message
                Toast.makeText(HomeActivity.this, "Couldn't connect to the database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });


        // Display ListView contents.
        teamArrayAdapter = new ArrayAdapter<>(this, R.layout.home_listview, teamsInfo);
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
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

                // TeamInfo object that was clicked.
                TeamInfo clickedTeam = (TeamInfo) parent.getAdapter().getItem(listItemPosition);

                // listItemPosition is the array index for the teams array. can be used such as:
                // teams.get(listItemPosition)
                // TODO Feb. 26, 2020 - Give ListView items functionality

                startActivity(new Intent(HomeActivity.this, TeamActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                Toast.makeText(HomeActivity.this, "You clicked on " + clickedTeam.getName(), Toast.LENGTH_SHORT).show();
            }
        });


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
        }

        else super.onBackPressed();
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