package com.zizzle.cmpt370;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LeagueActivity extends AppCompatActivity {

    // values inside ListView.
    ArrayList<League> leagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    // ListView
        // TESTING. fills the array with example leagues.
        leagues = new ArrayList<>();
        Member owner = new Member("Tom", "Holland", "e@mail.gov", "1234567890");
        for (int i = 0; i < 20; i++) {
            leagues.add(new League("League " + i, owner, "SQUASH", "description"));
        }

        // TESTING. gathers the league names from the leagues in system.
        ArrayList<String> league_names = new ArrayList<>();
        for (League l :leagues) {
            league_names.add(l.getName());
        }

        // Display ListView contents.
        ArrayAdapter leagueArrayAdapter = new ArrayAdapter<>(
                this, R.layout.league_listview, league_names);
        ListView leagueList = findViewById(R.id.leagues_list);
        leagueList.setAdapter(leagueArrayAdapter);

        // clicking on a league.
        leagueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LeagueActivity.this, "You just clicked " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
