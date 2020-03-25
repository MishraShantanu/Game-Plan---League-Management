package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;
import java.util.Collections;

import static com.zizzle.cmpt370.Model.CurrentUserInfo.getCurrentUserInfo;

public class AllGamesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    TeamInfo teamClicked;

    private ArrayAdapter nextGameArrayAdapter;
    private ArrayAdapter pastGameArrayAdapter;


    /**
     * Line Chart to display scores
     */
    LineChart lineChart;


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

        // NEXT GAMES =========================================================================
        final ArrayList<Game> nextGames = new ArrayList<>();
        final ArrayList<Game> pastGames = new ArrayList<>();

        // read in the list of games for this team
        final TeamInfo currentTeamInfo = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");
        DatabaseReference currentTeamReference = FirebaseDatabase.getInstance().getReference().child("Teams").child(currentTeamInfo.getDatabaseKey());
        currentTeamReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Team currentTeam = dataSnapshot.getValue(Team.class);
                // add the games scheduled and played for this team to lists to be displayed
                // first clear these lists so games aren't displayed twice if new games are added
                nextGames.clear();
                pastGames.clear();
                nextGames.addAll(currentTeam.getSortedScheduledGames());
                ArrayList<Game> sortedPlayedGames = currentTeam.getSortedPlayedGames();
                // sortedPlayedGames is sorted so that the oldest played game appears first, but we want to
                // display the most recent played game first, so reverse this list
                Collections.reverse(sortedPlayedGames);
                pastGames.addAll(sortedPlayedGames);

                // team has no games scheduled, display this to the user
                TextView noComingText = findViewById(R.id.no_upcoming_games_text);
                if (nextGames.isEmpty()) {
                    noComingText.setVisibility(View.VISIBLE);
                }
                else{
                    // the user has upcoming games, don't show this text
                    noComingText.setVisibility(View.GONE);
                }

                TextView noPastText = findViewById(R.id.no_past_games_text);

                if (pastGames.isEmpty()) {
                    // team hasn't played any games, display this
                    noPastText.setVisibility(View.VISIBLE);
                }
                else{
                    // user has previous games don't display this message
                    noPastText.setVisibility(View.GONE);
                }

                // display the games in these lists
                nextGameArrayAdapter.notifyDataSetChanged();
                pastGameArrayAdapter.notifyDataSetChanged();

                // determine the win loss ratios for this team over all of their games played
                ArrayList<Float> winLossRatios = currentTeam.getWinLossRatioOverTime();

                // Graph to Show the Win:Loss Ratio ==========================================================================

                lineChart = (LineChart) findViewById(R.id.WInLossRatioBarGraph);

                int count = 0;
                ArrayList<Entry> lineEntries = new ArrayList<>();
                for (int i=0; i<winLossRatios.size(); i++) {
                    //x and y coordinate
                    lineEntries.add(new Entry(i, winLossRatios.get(i)));
                    count++; //used later to animate graph to show the latest 5 ratios
                }
                LineDataSet lineDataSet = new LineDataSet(lineEntries, "Win:Loss Ratio");

                int lightGreen = Color.argb(255, 204, 229, 255);
                lineDataSet.setColor(lightGreen); //set Line Color
                lineDataSet.setLineWidth(2f); //adjust line thickness
                // Adjust data point circle shape and color
                lineDataSet.setCircleColor(Color.WHITE);
                lineDataSet.setCircleHoleColor(Color.DKGRAY);
                lineDataSet.setCircleHoleRadius(2f);
                lineDataSet.setCircleRadius(4f);
                lineDataSet.setValueTextSize(10f);
                lineDataSet.setValueTextColor(Color.WHITE);

                ArrayList<ILineDataSet> dataSet = new ArrayList<>();
                dataSet.add(lineDataSet);

                LineData data = new LineData(dataSet);
                lineChart.setData(data); //add data to the chart
                lineChart.invalidate(); //refresh the chart

                lineChart.setTouchEnabled(true); //true = enable all gestures and touches on the chart
                lineChart.setScaleEnabled(false); //disable all zooming
                lineChart.animateX(2000);
                lineChart.animateY(1000);
                Description d = new Description();
                d.setText("");
                lineChart.setDescription(d); //remove description
                lineChart.getLegend().setEnabled(false); //remove legend
                lineChart.getAxisLeft().setDrawLabels(false); //remove left axis
                lineChart.getAxisRight().setTextColor(Color.WHITE); //set left axis color
                lineChart.getXAxis().setDrawLabels(false); //remove X axis label
                //Show Y axis from 0 to 1 at all times
                lineChart.getAxisLeft().setAxisMinimum(0f);
                lineChart.getAxisLeft().setAxisMaximum(1f);
                lineChart.getAxisRight().setAxisMinimum(0f);
                lineChart.getAxisRight().setAxisMaximum(1f);
                //forces 6 Y axis values to display (0.0, 0.2, 0.4, 0.6, 0.8, 1.0); this was needed to display 1.0
                lineChart.getAxisRight().setLabelCount(6, true);
                lineChart.setVisibleXRangeMaximum(5f); //only show 5 values at a time
                lineChart.moveViewTo((float) count, 0.5f, YAxis.AxisDependency.RIGHT); //moves visible points to show latest 5 ratios


                // ==========================================================================

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
             * @param listItemPosition the index of position for the item in the ListView
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int listItemPosition, long id) {

                final Game clickedGame = (Game) parent.getAdapter().getItem(listItemPosition);

                Intent gameIntent = new Intent(AllGamesActivity.this, GameActivity.class);
                // pass the current team info and the game clicked to our GameActivity
                gameIntent.putExtra("GAME_CLICKED", clickedGame);
                gameIntent.putExtra("TEAM_INFO", currentTeamInfo);
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
                if (!dataSnapshot.exists()) {
                    // user isn't on the team, don't display the add game button
                    addGame.hide();
                }
            }

            // Auto Generated.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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