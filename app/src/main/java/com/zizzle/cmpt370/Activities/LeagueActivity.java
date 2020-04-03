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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class LeagueActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Values inside ListView. */
    ArrayList<String> leagueNames;

    /** Adapter for search bar. */
    ArrayAdapter leagueArrayAdapter;


    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;

    /**
     * User verified or not
     */
    boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Suppress soft-keyboard until user actually touches the EditTextView
        setContentView(R.layout.activity_league);

        // add top bar with title 'Leagues'
        Toolbar mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar
        getSupportActionBar().setTitle("All Leagues");

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.league_layout);
        NavigationView navigationView = findViewById(R.id.league_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leagues); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button

        // Check that user is verified =========================================================================
        verified = FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
        if (!verified) { //user is NOT verified
            findViewById(R.id.progressbar_loading).setVisibility(View.GONE);
            findViewById(R.id.Leagues_NotVerified).setVisibility(View.VISIBLE); //show "need to verify account" message
            findViewById(R.id.league_search_bar).setVisibility(View.INVISIBLE); //hide search bar
            return;
        }

        // add league button =======================================================================

        // launches a pop-up for adding a new class.
        FloatingActionButton addLeague = findViewById(R.id.add_league_button);
        addLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(LeagueActivity.this, LeaguePop.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        // list of leagues =========================================================================

        leagueNames = new ArrayList<>();

        // get a reference to the leagues on the database
        // NOTE: this will give all leagues created by anybody, NOT leagues the current user is a part of
        DatabaseReference leagueDBReference = FirebaseDatabase.getInstance().getReference().child("Leagues");

        

        // attaching this listener will read from the database once initially and whenever leagues on the database are changed.
        leagueDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // first clear our list of league names in case a league is added, we don't want the same leagues to be displayed twice
                leagueNames.clear();
                // remove the loading animation if there are no leagues to read in
                if(!dataSnapshot.exists()){
                    ProgressBar leagueLoading = findViewById(R.id.progressbar_loading);
                    leagueLoading.setVisibility(View.GONE);
                }
                // method called when data is read from the database, get all league names
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                                        // the key of each league is the league's name
                    String leagueName = ds.getKey();

                    leagueNames.add(leagueName);

                    // If there are no leagues, display special text
                    if (leagueNames.isEmpty()) {
                        TextView noTeamText = findViewById(R.id.no_leagues_text);
                        noTeamText.setVisibility(View.VISIBLE);
                    }

                    leagueArrayAdapter.notifyDataSetChanged();
                }

                // Remove the progress bar once leagues have been fetched
                ProgressBar leagueLoading = findViewById(R.id.progressbar_loading);
                leagueLoading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Firebase failed to fetch the data requested
                // TODO display some sort of error message to the user
            }
        });


        // Display ListView contents.
        leagueArrayAdapter = new ArrayAdapter<>(this, R.layout.league_listview, leagueNames);
        ListView leagueList = findViewById(R.id.leagues_list);
        leagueList.setAdapter(leagueArrayAdapter);


        // clicking on a league in the ListView is handled in here.
        leagueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * performs an action when a ListView item is clicked.
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

                // name of the league that was clicked.
                final String clickedLeagueName = (String) parent.getAdapter().getItem(listItemPosition);

                Intent teamsIntent = new Intent(LeagueActivity.this, TeamsActivity.class);
                // pass the name of the league clicked on to this intent, so it can be accessed from the TeamsActivity
                teamsIntent.putExtra("LEAGUE_CLICKED",clickedLeagueName);
                startActivity(teamsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        // functionality for search bar.
        EditText leagueSearchBar = findViewById(R.id.league_search_bar);
        leagueSearchBar.addTextChangedListener(new TextWatcher() {

            // changes the shown list items based on characters in search bar.
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (LeagueActivity.this).leagueArrayAdapter.getFilter().filter(charSequence);
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
                // already on league page do nothing
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
            Intent toHome = new Intent(this, HomeActivity.class);
            toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toHome);
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