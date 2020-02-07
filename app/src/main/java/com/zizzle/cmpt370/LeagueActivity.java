package com.zizzle.cmpt370;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class LeagueActivity extends AppCompatActivity {

    // values inside ListView.
    ArrayList<String> leagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ListView
        leagues = new ArrayList<>();
        leagues.add("League 1");
        leagues.add("League 2");
        leagues.add("League 3");
        leagues.add("League 4");
        leagues.add("League 5");
        leagues.add("League 6");
        leagues.add("League 1");
        leagues.add("League 2");
        leagues.add("League 3");
        leagues.add("League 4");
        leagues.add("League 5");
        leagues.add("League 6");

        // Display ListView contents.
        ArrayAdapter leagueArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.league_listview, leagues);
        ListView leagueList = findViewById(R.id.leagues_list);
        leagueList.setAdapter(leagueArrayAdapter);
    }
}
