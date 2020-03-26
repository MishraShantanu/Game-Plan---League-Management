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

import com.google.firebase.auth.FirebaseAuth;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.R;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //main roundedCorners ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar; //Added for overlay effect of menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //add top bar from top_bar as action bar
        mToolBar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(mToolBar); //sets toolbar as action bar

        //MENU (button & drawer)
        mDrawerLayout = (DrawerLayout) findViewById(R.id.about_us_layout);
        NavigationView navigationView = findViewById(R.id.about_us_nav_view); //ADDED FOR CLICK
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_aboutUs); //Highlight respective option in the navigation menu

        //four parameters: the activity (either "this" or getActivity()"), instance of drawer layout, toolbar, open String (see strings.xml in values folder), close String (see strings.xml)
        // ActionBarDrawerToggle sets up the app icon on the left of the top bar to open & close the navigation drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.sidebar_navigation_open, R.string.sidebar_navigation_close); //added "menu button" which automatically animates icon for open/close
        mDrawerLayout.addDrawerListener(mToggle); //Connects ActionBarDrawerToggle to DrawerLayout
        mToggle.syncState(); //takes care of rotating the menu icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button




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
                // already on about us page, do nothing
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