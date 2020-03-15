package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;

public class GamePop extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.game_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));


        // Spinner =================================================================================
        final TeamInfo currentTeam = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");
        LeagueInfo leagueInfo = new LeagueInfo(currentTeam.getLeagueName());

        DatabaseReference leagueReference = FirebaseDatabase.getInstance().getReference().child("Leagues").child(leagueInfo.getDatabaseKey()).child("teamsInfoMap");
        leagueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TeamInfo> teamInfos = new ArrayList<>();
                ArrayList<String> teamNames = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    TeamInfo currentTeamInfo = ds.getValue(TeamInfo.class);
                    teamInfos.add(currentTeamInfo);
                    teamNames.add(currentTeamInfo.getName());
                }
                teamInfos.remove(currentTeam);
                teamNames.remove(currentTeam.getName());

                System.out.println("TeamNames: " + teamNames);
                System.out.println("TeamInfos: " + teamInfos);

                Spinner againstTeam = findViewById(R.id.team_spinner);

                // Spinner click listener
                againstTeam.setOnItemSelectedListener(GamePop.this);

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(GamePop.this, android.R.layout.simple_spinner_item, teamNames);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                againstTeam.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
