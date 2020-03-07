package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.R;

public class TeamsPop extends Activity {

    EditText teamName, typeOfSport;
    Button submitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.teams_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));


        // Gathering Input =========================================================================
        teamName  = findViewById(R.id.teamName);
        typeOfSport = findViewById(R.id.sportInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfTeam = teamName.getText().toString();
                String sportForTeam = typeOfSport.getText().toString();

                if (nameOfTeam.isEmpty() && sportForTeam.isEmpty()) {
                    Toast.makeText(TeamsPop.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else if (nameOfTeam.isEmpty()) {
                    Toast.makeText(TeamsPop.this, "Team name is required", Toast.LENGTH_SHORT).show();
                }

                else if (sportForTeam.isEmpty()) {
                    Toast.makeText(TeamsPop.this, "Type of sport is required", Toast.LENGTH_SHORT).show();
                }

                else {
                    MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo(); // TODO update team constructor to take league info

                    // TODO get league from previous activity
                    // Team newTeam = new Team(nameOfTeam, currentUserInfo, sportForTeam, );
                    // TODO 24/02/2020 - Insert new team into team list, send to database.
                    finish();
                }
            }
        });
    }
}
