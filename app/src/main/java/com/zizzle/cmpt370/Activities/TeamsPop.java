package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                    // TODO 24/02/2020 - Replace tempUser with the current user of the app.
                    Member currentUser = new Member("Mike Tyson", "bigmike@punchface.gov", "12312312312","UID67755");
                    // TODO get league from previous activity
                    //Team newTeam = new Team(nameOfTeam, currentUser, sportForTeam);

                    // TODO 24/02/2020 - Insert new team into team list, send to database.

                    finish();
                }
            }
        });
    }
}
