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
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.R;

public class TeamMemberActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

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


        // Temporary User created ==========================================================================
        //TODO Mar. 4 2020: change this to get member from the database

        Member owner = new Member("Will Gates", "BigBill@microsoft.com", "78945612311", "F");
        MemberInfo ownerInfo = new MemberInfo(owner);

        League fakeLeague = new League("FUN", ownerInfo, "HOCKEY", "LOLOLOL");
        Team fakeTeam = new Team("Charger", owner,"HOCKEY", fakeLeague);

        Member member = new Member("Bill Gates", "BigBill@microsoft.com", "78945612311", "F");

        // Set the title of the page to user name.
        getSupportActionBar().setTitle(member.getDisplayName() + " Information");

        // DisplayName Text ==========================================================================
        TextView userName = (TextView) findViewById(R.id.TeamMember_DisplayName);
        userName.setText(member.getDisplayName());

        // Email Text ==========================================================================
        TextView email = (TextView) findViewById(R.id.TeamMember_Email);
        email.setText(member.getEmail());

        // Phone Number Text ==========================================================================
        TextView phoneNumber = (TextView) findViewById(R.id.TeamMember_PhoneNumber);
        phoneNumber.setText(member.getPhoneNumber());

        // Remove Player Button ==========================================================================
        Button removePlayer = findViewById(R.id.remove_player);
        View removeDivider = findViewById(R.id.remove_player_divider);

        if (fakeTeam.getOwnerInfo().equals(CurrentUserInfo.getCurrentUserInfo())) {
            removePlayer.setVisibility(View.VISIBLE);
            removeDivider.setVisibility(View.VISIBLE);
        }

        removePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO 07/03/2020 - Remove the member from the team.

            }
        });


        //THIS WAS ON THE PROFILE PAGE, DON'T NEED NOW. BUT KEEP THIS SO THAT WE CAN EASILY ADD A BUTTON TO DO STUFF IF NEEDED
//        // Update Info button ==========================================================================
//        Button updateInfoButton = findViewById(R.id.updateInfoButton);
//        updateInfoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity( new Intent(TeamMemberActivity.this, ProfilePop.class));
//                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//            }
//        });
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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);        }
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