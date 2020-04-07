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
import com.zizzle.cmpt370.PieChartFormatter;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    /**
     * Pie Chart to display W:T:L
     */
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.profile_layout);
        NavigationView navigationView = findViewById(R.id.profile_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button


        // Temporary User created ==========================================================================
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member user = dataSnapshot.getValue(Member.class);

                // DisplayName Text ==========================================================================
                TextView userName = (TextView) findViewById(R.id.newProfile_DisplayName);
                userName.setText(user.getDisplayName());

                // Email Text ==========================================================================
                TextView email = (TextView) findViewById(R.id.newProfile_Email);
                email.setText(user.getEmail());

                // Phone Number Text ==========================================================================
                TextView phoneNumber = (TextView) findViewById(R.id.newProfile_PhoneNumber);
                phoneNumber.setText(user.getPhoneNumber());


                // Set the title of the page to user name.
                getSupportActionBar().setTitle(user.getDisplayName() + "'s Information");


                // Graph to Show the Wins/Ties/Losses ==========================================================================
                int numWins = user.getCareerWins();
                int numLosses = user.getCareerLosses();
                int numTies = user.getCareerTies();

                pieChart = (PieChart) findViewById(R.id.ProfilePieChart);

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

            }
        });


        // Update Info button ==========================================================================
        Button updateInfoButton = findViewById(R.id.updateInfoButton);
        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ProfilePop.class));
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
                // already on profile activity, do nothing
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
        }

        // Go back to team page if this activity was called from there.
        if (getCallingActivity().getClassName().equals(TeamActivity.class.getName())) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        // Return to home for other activities.
        else {
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