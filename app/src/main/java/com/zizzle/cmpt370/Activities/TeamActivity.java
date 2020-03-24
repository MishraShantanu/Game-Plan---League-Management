package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.NonScrollableListView;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

import static com.zizzle.cmpt370.Model.CurrentUserInfo.getCurrentUserInfo;

public class TeamActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Values inside ListView.
     */
    ArrayList<MemberInfo> membersInfo;

    /**
     * Adapter for search bar.
     */
    ArrayAdapter memberArrayAdapter;

    /**
     * TeamInfo object representing the current team whose page the user is on
     */
    TeamInfo currentTeamInfo;

    //main roundedCorners ID of homepageWithMenu.xml
    private DrawerLayout menuDrawer;
    private ActionBarDrawerToggle toggleDrawer;
    private NonScrollableListView teamList;
    private Button removeTeam;

    private MenuItem joinButton;

    /**
     * Bar Chart to display scores
     */
    BarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Suppress soft-keyboard until user actually touches the EditTextView
        setContentView(R.layout.activity_team);

        // add top bar
        Toolbar toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar); //sets toolbar as action bar

        // get the TeamInfo object stored in the intent
        currentTeamInfo = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO_CLICKED");

        // set the title to the name of the clicked team
        getSupportActionBar().setTitle(currentTeamInfo.getName());

        //MENU (button & drawer)
        menuDrawer = findViewById(R.id.team_layout);
        NavigationView navigationView = findViewById(R.id.team_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leagues); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        toggleDrawer = new ActionBarDrawerToggle(this, menuDrawer,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        menuDrawer.addDrawerListener(toggleDrawer); //Connects ActionBarDrawerToggle to DrawerLayout
        toggleDrawer.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button


        // owner button ==========================================================================
        final Button ownerButton = findViewById(R.id.owner_button);


        // list of teams =========================================================================

        membersInfo = new ArrayList<>();
        // read in the current team from the database
        DatabaseReference teamReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeamInfo.getDatabaseKey());
        // this listener will read from the database once initially and whenever the current team is updated
        teamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // called to read in data
                if (dataSnapshot.exists()) {
                    // clear our list of team members so we don't rewrite the same members multiple times if data is altered and read in again
                    membersInfo.clear();
                    final Team currentTeam = dataSnapshot.getValue(Team.class);

                    // Set the record
                    TextView wins = findViewById(R.id.record_wins);
                    TextView losses = findViewById(R.id.record_losses);
                    TextView ties = findViewById(R.id.record_ties);

                    // Display the W/T/L record
                    int numWins = currentTeam.getWins();
                    int numLosses = currentTeam.getLosses();
                    int numTies = currentTeam.getTies();
                    wins.setText(String.valueOf(numWins));
                    losses.setText(String.valueOf(numLosses));
                    ties.setText(String.valueOf(numTies));

                    // Graph to Show the Wins/Ties/Losses ==========================================================================

                    barChart = (BarChart) findViewById(R.id.barGraph);

                    if (numWins == 0 && numTies == 0 && numLosses == 0) { //don't display graph if team hasn't played any games yet
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
                    barChart.animateY(2000);
                    Description d = new Description();
                    d.setText("");
                    barChart.setDescription(d); //remove description
                    barChart.getLegend().setEnabled(false); //remove legend
                    barChart.getAxisLeft().setDrawLabels(false); //remove left axis
                    barChart.getAxisRight().setDrawLabels(false); //remove right axis
                    String[] barLabels = {"Win", "Tie", "Loss"};
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barLabels)); //show X Label (Win, Tie, loss)
                    barChart.getXAxis().setTextColor(Color.WHITE); //set X Axis text color
                    barChart.getXAxis().setGranularityEnabled(true); //removes duplicate first X Axis value
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); //Position X Axis at the bottom

                    // ==========================================================================


                    // Set the next scheduled game
                    TextView nextGameText = findViewById(R.id.next_games_text);

                    if (currentTeam.hasGamesScheduled()) {
                        final Game closestGame = currentTeam.getClosestScheduledGame();

                        // Remove the score since will always be n/a
                        String nextString = closestGame.toString();
                        nextString = nextString.replace("\nFinal Score: n/a", "");

                        // Set the next game text.
                        nextGameText.setText(nextString);
                        // take the user to the page for this game if they clicked this next game text
                        nextGameText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent gameIntent = new Intent(TeamActivity.this, GameActivity.class);
                                // pass the current team info and the game clicked to our GameActivity
                                gameIntent.putExtra("GAME_CLICKED", closestGame);
                                gameIntent.putExtra("TEAM_INFO", currentTeamInfo);
                                startActivity(gameIntent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    }
                    else{
                        // this team has no upcoming games
                        nextGameText.setText("No Upcoming Games");
                    }
                    // otherwise this TextView has the string "No Upcoming Games as a default value"


                    // set the text of the owner button to the owner's name, add 2 spaces to center the name
                    final MemberInfo ownerInfo = currentTeam.getOwnerInfo();
                    ownerButton.setText("  " + ownerInfo.getName());

                    // Owner button
                    // Moved inside here so it can access the database info.
                    ownerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // pass the MemberInfo of the clicked on Member to the TeamMemberActivity
                            Intent teamMemberIntent = new Intent(TeamActivity.this, TeamMemberActivity.class);
                            teamMemberIntent.putExtra("CLICKED_MEMBER", ownerInfo);
                            // add the owner's info of this team to the intent also
                            teamMemberIntent.putExtra("OWNER_INFO", ownerInfo);
                            // add the current teamInfo to the intent
                            teamMemberIntent.putExtra("TEAM_INFO", currentTeamInfo);
                            startActivity(teamMemberIntent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });


                    // Remove team button ===================================
                    removeTeam = findViewById(R.id.delete_team_button);
                    // only display this button if the current user is the owner of the team
                    if (!ownerInfo.equals(CurrentUserInfo.getCurrentUserInfo())) {
                        removeTeam.setVisibility(View.GONE);
                    }

                    removeTeam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // remove the team member from the team
                            // TeamInfo currentTeamInfo = (TeamInfo)getIntent().getSerializableExtra("TEAM_INFO");

                            //System.out.println(currentTeam.getOwnerInfo().getDatabaseKey()+" <<<???>>>"+FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //Check if the current user is same as the Owner of the Team else do not delete the team
                            if (currentTeam.getOwnerInfo().getDatabaseKey().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) == 0) {
                                Toast.makeText(TeamActivity.this, "Team has been removed successfully", Toast.LENGTH_SHORT).show();

                                Intent toHome = new Intent(TeamActivity.this, HomeActivity.class);
                                toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(toHome);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                Storage.removeTeam(currentTeamInfo);
                            } else
                                Toast.makeText(TeamActivity.this, "You are not Authorized to remove this Team", Toast.LENGTH_SHORT).show();


                        }
                    });


                    // display the members of the team
                    for (DataSnapshot ds : dataSnapshot.child("membersInfoMap").getChildren()) {
                        membersInfo.add(ds.getValue(MemberInfo.class));
                    }
                    memberArrayAdapter.notifyDataSetChanged();

                    // Leave the team button.
                    MemberInfo currentUser = getCurrentUserInfo();
                    membersInfo.remove(ownerInfo); //remove owner from the team member list

                    if (membersInfo.contains(currentUser)) {

                        // Make button visible and set click listener.
                        final Button leaveTeamButton = findViewById(R.id.leave_team_button);
                        leaveTeamButton.setVisibility(View.VISIBLE);
                        leaveTeamButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // leave the team
                                MemberInfo currentUser = getCurrentUserInfo();
                                Storage.removeMemberFromTeam(currentUser, currentTeamInfo);
                                leaveTeamButton.setVisibility(View.INVISIBLE);
                                Toast.makeText(TeamActivity.this, "Left the team successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    // clicking on a team in the ListView is handled in here.
                    teamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        /**
                         * performs an action when a ListView item is clicked.
                         *
                         * @param listItemPosition the index of position for the item in the ListView
                         */
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {
                            // MemberInfo object that was clicked.
                            MemberInfo clickedMemberInfo = (MemberInfo) parent.getAdapter().getItem(listItemPosition);

                            // Take user to their profile if they clicked on themselves.
                            if (getCurrentUserInfo().equals(clickedMemberInfo)) {
                                Intent profileFromTeam = new Intent(TeamActivity.this, ProfileActivity.class);
                                startActivityForResult(profileFromTeam, 2);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                            // Take user to member profile page if not them
                            else {
                                // pass the MemberInfo of the clicked on Member to the TeamMemberActivity
                                Intent teamMemberIntent = new Intent(TeamActivity.this, TeamMemberActivity.class);
                                teamMemberIntent.putExtra("CLICKED_MEMBER", clickedMemberInfo);
                                // add the owner's info of this team to the intent also
                                teamMemberIntent.putExtra("OWNER_INFO", ownerInfo);
                                // add the current teamInfo to the intent
                                teamMemberIntent.putExtra("TEAM_INFO", currentTeamInfo);
                                startActivity(teamMemberIntent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // called when database operation fails
                // TODO display some error message, telling the user they couldn't connect to database, or asking them to try again
            }
        });


        // All games button
        Button allGamesButton = findViewById(R.id.all_games_button);
        allGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take the user to the game activity page
                Intent gameIntent = new Intent(TeamActivity.this, AllGamesActivity.class);
                gameIntent.putExtra("TEAM_INFO", currentTeamInfo);
                startActivity(gameIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        // Display ListView contents.
        memberArrayAdapter = new ArrayAdapter<>(this, R.layout.team_listview, membersInfo);
        teamList = findViewById(R.id.members_list);
        teamList.setAdapter(memberArrayAdapter);

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
        int id = item.getItemId();
        if (id == R.id.join_team_button) { //Join Team Text/Button was clicked
            // add the current user to the current team
            MemberInfo currentUserInfo = getCurrentUserInfo();
            // add this current user to the current team, and the current team to the current user
            // we don't need to check if the user is already part of this team, we just rewrite values that are already there in that case
            Storage.addTeamToMember(currentUserInfo, currentTeamInfo);
            return true;
        }

        //Sidebar navigation menu
        if (toggleDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.team_button_menu, menu);
        this.joinButton = menu.findItem(R.id.join_team_button);

        // only display the join button if the current user is on this team
        TeamInfo currentTeamInfo = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO_CLICKED");
        MemberInfo currentMemberInfo = CurrentUserInfo.getCurrentUserInfo();
        DatabaseReference userOnTeamReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeamInfo.getDatabaseKey()).child("membersInfoMap").child(currentMemberInfo.getDatabaseKey());
        userOnTeamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // the current user is on this team, don't display the join button
                    joinButton.setVisible(false);
                }
                else{
                    // current user isn't on the team, display the join button
                    joinButton.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }
}