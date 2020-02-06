package com.zizzle.cmpt370;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LeagueActivity extends AppCompatActivity {

    String[] leagues = {"TEST1", "TEST2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter leagueArrayAdapter = new ArrayAdapter<String>(this, R.layout.league_listview, leagues);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mobileArray);

        ListView leagueList = findViewById(R.id.leagues_list);
//        ListView listView = (ListView) findViewById(R.id.mobile_list);

        leagueList.setAdapter(leagueArrayAdapter);
//        listView.setAdapter(adapter);
    }
}
