package com.zizzle.cmpt370;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

public class LeagueActivity extends AppCompatActivity {

    // values inside ListView.
    ArrayList<League> leagues;

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
//        setSupportActionBar(toolbar);

        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.league_layout);
        NavigationView navigationView = findViewById(R.id.league_nav_view); //ADDED FOR CLICK
//        navigationView.setNavigationItemSelectedListener(this);

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button



        // ListView
        // TESTING. fills the array with example leagues.
        leagues = new ArrayList<>();
        Member owner = new Member("Tom", "Holland", "e@mail.gov", "1234567890");
        for (int i = 0; i < 20; i++) {
            leagues.add(new League("League " + i, owner, "SQUASH", "description"));
        }

        // TESTING. gathers the league names from the leagues in system.
        ArrayList<String> league_names = new ArrayList<>();
        for (League l :leagues) {
            league_names.add(l.getName());
        }

        // Display ListView contents.
        ArrayAdapter leagueArrayAdapter = new ArrayAdapter<>(
                this, R.layout.league_listview, league_names);
        ListView leagueList = findViewById(R.id.leagues_list);
        leagueList.setAdapter(leagueArrayAdapter);

        // clicking on a league.
        leagueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LeagueActivity.this, "You just clicked " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }


//    //Button to open menu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) { //allows menu button to show menu on click
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
