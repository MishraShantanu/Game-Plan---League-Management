package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.PieChartFormatter;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class TeamMemberActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu
    private Button removePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member);
        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.team_member_layout);
        NavigationView navigationView = findViewById(R.id.team_member_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leagues); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button

        // the clicked on member is passed via intent ==========================================================================
        final MemberInfo clickedMemberInfo = (MemberInfo) getIntent().getSerializableExtra("CLICKED_MEMBER");

        // read the clicked member in from the database
        DatabaseReference clickedMemberReference = FirebaseDatabase.getInstance().getReference().child("users").child(clickedMemberInfo.getDatabaseKey());
        clickedMemberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // called when data is read from the database
                Member clickedMember = dataSnapshot.getValue(Member.class);

                // Set the title of the page to user name.
                getSupportActionBar().setTitle(clickedMember.getDisplayName() + "'s Information");

                // DisplayName Text ==========================================================================
                TextView userName = (TextView) findViewById(R.id.TeamMember_DisplayName);
                userName.setText(clickedMember.getDisplayName());

                // Email Text ==========================================================================
                TextView email = (TextView) findViewById(R.id.TeamMember_Email);
                email.setText(clickedMember.getEmail());

                // Phone Number Text ==========================================================================
                TextView phoneNumber = (TextView) findViewById(R.id.TeamMember_PhoneNumber);
                phoneNumber.setText(clickedMember.getPhoneNumber());

                // if the current user of the app is the owner of the current team, display the remove member button
                // get the owner info again from our intent
                MemberInfo ownerInfo = (MemberInfo) getIntent().getSerializableExtra("OWNER_INFO");

                // Remove Player Button ==========================================================================
                // Show the remove player button if you are the owner and the player to remove is not you.
                if (ownerInfo.equals(CurrentUserInfo.getCurrentUserInfo()) &&
                        !ownerInfo.equals(clickedMemberInfo)) {
                    removePlayer.setVisibility(View.VISIBLE);
                }

                // Graph to Show the Wins/Ties/Losses of this member ==========================================================================
                int numWins = clickedMember.getCareerWins();
                int numLosses = clickedMember.getCareerLosses();
                int numTies = clickedMember.getCareerTies();

                // PieChart to display this team member's wins/ties/losses
                PieChart pieChart = (PieChart) findViewById(R.id.TeamMemberPieChart);

                if (numWins == 0 && numTies == 0 && numLosses == 0) { //don't display graph if user hasn't played any games yet
                    pieChart.setVisibility(View.GONE);
                }

                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                // data values
                pieEntries.add(new PieEntry((float) numWins, "Wins")); //entries must be floats
                pieEntries.add(new PieEntry((float) numTies, "Ties"));
                pieEntries.add(new PieEntry((float) numLosses, "Losses"));
                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

                //bar colors (same shades as numbers above the graph)
                int green = Color.argb(255, 153, 204, 0);
                int yellow = Color.argb(255, 235, 200, 0);
                int red = Color.argb(255, 255, 68, 68);
                int[] barColors = {green, yellow, red};
                pieDataSet.setColors(barColors);
                //data text size and color
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(20f);

                pieDataSet.setValueFormatter(new PieChartFormatter());

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);


                pieChart.setTouchEnabled(true); //true = enable all gestures and touches on the chart
                pieChart.setUsePercentValues(true); //use percentages
                pieChart.animateXY(1000, 1000);
                pieChart.getDescription().setEnabled(false); //remove description
                pieChart.getLegend().setTextColor(Color.WHITE); //set legend text color to white
                pieChart.getLegend().setTextSize(15f); //adjust legend text size
                pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE); //shape of color points beside legend entries
                pieChart.getLegend().setXEntrySpace(20f); //space out the legend values
                pieChart.setHoleRadius(10f); //set white hole radius
                pieChart.setTransparentCircleRadius(15f); //set transparent hold radius
                pieChart.setDrawEntryLabels(false); //removes inside pie labels of "Win, Tie, Loss"
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // called when database operations fail, display an error message
                Toast.makeText(TeamMemberActivity.this, "Cannot connect to database, please try again later", Toast.LENGTH_SHORT).show();
            }
        });


        // Remove player button ===================================
        removePlayer = findViewById(R.id.remove_player);
        removePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove the team member from the team
                TeamInfo currentTeamInfo = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");
                Storage.removeMemberFromTeam(clickedMemberInfo, currentTeamInfo);
                Toast.makeText(TeamMemberActivity.this, "Team member removed successfully", Toast.LENGTH_SHORT).show();

                // Move player back to the team page.
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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