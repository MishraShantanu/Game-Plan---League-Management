package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.CustomArrayAdapter;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Values inside ListView.
     */
    ArrayList<TeamInfo> teamsInfo;
    ArrayList<String> leaguesName;

    /**
     * Adapter for displaying teams
     */
    CustomArrayAdapter teamArrayAdapter;

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    /**
     * Bar Chart to display scores
     */
    BarChart barChart;


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
        // Initialize arrays
        teamsInfo = new ArrayList<>();
        leaguesName = new ArrayList<>();

        // read in the current user's information from the database
        final MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
        DatabaseReference currentUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserInfo.getDatabaseKey());
        currentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Remove the progress bar once the current user has been fetched
                ProgressBar memberLoading = findViewById(R.id.progressbar_loading);
                memberLoading.setVisibility(View.GONE);
                Member currentMember = dataSnapshot.getValue(Member.class);

                // display the teams the user is a part of, along with the league each team belongs to
                // first clear out list of teams for this user to prevent teams from showing up twice if new teams are added or removed
                teamsInfo.clear();
                leaguesName.clear();
                for(TeamInfo ti : currentMember.getTeamsInfo()){
                    teamsInfo.add(ti);
                    leaguesName.add(ti.getLeagueName());
                }

                // If user is on teams, show their teams.
                TextView myTeamsText = findViewById(R.id.my_teams_text);
                myTeamsText.setVisibility(View.VISIBLE);
                View myTeamsDivider = findViewById(R.id.my_teams_div);
                myTeamsDivider.setVisibility(View.VISIBLE);

                // If user not on any teams, show sad text.
                if (teamsInfo.isEmpty()) {
                    TextView noTeamText = findViewById(R.id.no_team_text);
                    noTeamText.setVisibility(View.VISIBLE);
                }

                // Show the teams found.
                teamArrayAdapter.notifyDataSetChanged();


                // Show Personal Wins : Ties: Losses ==========================================================================

                // make personal record numbers and graph visible
                findViewById(R.id.personalRecord_TitleText).setVisibility(View.VISIBLE);
                findViewById(R.id.personalRecord_Divider).setVisibility(View.VISIBLE);
                findViewById(R.id.personalRecord).setVisibility(View.VISIBLE);
                findViewById(R.id.personalBarGraph).setVisibility(View.VISIBLE);


                // Set the record
                TextView wins = findViewById(R.id.personalRecord_wins);
                TextView losses = findViewById(R.id.personalRecord_losses);
                TextView ties = findViewById(R.id.personalRecord_ties);

                // Display the W/T/L record
                int numWins = currentMember.getCareerWins();;
                int numLosses = currentMember.getCareerLosses();;
                int numTies = currentMember.getCareerTies();;
                wins.setText(String.valueOf(numWins));
                losses.setText(String.valueOf(numLosses));
                ties.setText(String.valueOf(numTies));

                // Graph to Show the Wins/Ties/Losses ==========================================================================

                barChart = (BarChart) findViewById(R.id.personalBarGraph);

                if (numWins == 0 && numTies == 0 && numLosses == 0) { //don't display graph if user hasn't played any games yet
                    barChart.setVisibility(View.GONE);
                }

                ArrayList<BarEntry> barEntries = new ArrayList<>();
                // x and y coordinate
                barEntries.add(new BarEntry(0f, numWins)); //entries must be floats
                barEntries.add(new BarEntry(1f, numTies));
                barEntries.add(new BarEntry(2f, numLosses));
                BarDataSet barDataSet = new BarDataSet(barEntries, "Games");

                barDataSet.setDrawValues(false); //hide values of the bar heights (i.e. number of games)

                //bar colors (same shades as numbers above the graph)
                int green = Color.argb(255, 153, 204, 0);
                int yellow = Color.argb(255, 235, 200, 0);
                int red = Color.argb(255, 255, 68, 68);
                int[] barColors = {green, yellow, red};
                barDataSet.setColors(barColors);

                BarData data = new BarData(barDataSet);
                barChart.setData(data);

                barChart.setTouchEnabled(true); //true = enable all gestures and touches on the chart
                barChart.setScaleEnabled(false); //disable all zooming
                barChart.animateY(2000);
                barChart.getDescription().setEnabled(false); //remove description
                barChart.getLegend().setEnabled(false); //remove legend
                //set y axis to start at '0'
                barChart.getAxisLeft().setAxisMinimum(0f);
                barChart.getAxisRight().setAxisMinimum(0f);
                barChart.getAxisLeft().setDrawLabels(false); //remove left axis
                barChart.getAxisRight().setDrawLabels(false); //remove right axis
                String[] barLabels = {"Win", "Tie", "Loss"};
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barLabels)); //show X Label (Win, Tie, loss)
                barChart.getXAxis().setTextColor(Color.WHITE); //set X Axis text color
                barChart.getXAxis().setGranularityEnabled(true); //removes duplicate first X Axis value
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); //Position X Axis at the bottom

                // Privacy Policy Link ==========================================================================

                TextView privacyPolicyButton = findViewById(R.id.Home_PrivacyPolicy);
                privacyPolicyButton.setVisibility(View.VISIBLE);
                privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://sites.google.com/view/zizzlestudioscanada/privacy-policy"));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Display ListView contents.
        teamArrayAdapter = new CustomArrayAdapter(HomeActivity.this, leaguesName, teamsInfo);
        ListView teamList = findViewById(R.id.user_individual_teams_list);
        teamList.setAdapter(teamArrayAdapter);
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
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                // indicate that we want to view the profile of the current user
                profileIntent.putExtra("SELECTED_MEMBER",CurrentUserInfo.getCurrentUserInfo());
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        } else super.onBackPressed();
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