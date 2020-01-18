package com.zizzle.cmpt370;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class homepageWithMenu extends AppCompatActivity {

    private DrawerLayout mDrawerLayout; //main layout ID of homepageWithMenu.xml
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepagewithmenu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //four parameters: the activity, drawer layout, open String (see strings.xml in values folder), close String (see strings.xml)
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //displays menu button
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //allows menu button to show menu on click
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
