package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zizzle.cmpt370.Model.*;
import com.zizzle.cmpt370.R;

public class LeaguePop extends Activity {

    EditText leagueName, typeOfSport, description;
    Button submitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.league_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));


        // Gathering Input =========================================================================
        leagueName  = findViewById(R.id.leagueName);
        typeOfSport = findViewById(R.id.sportInput);
        description = findViewById(R.id.descriptionInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfLeague = leagueName.getText().toString();
                String sportForLeague = typeOfSport.getText().toString();
                String descriptionOfLeague = description.getText().toString();

                if (nameOfLeague.isEmpty() && sportForLeague.isEmpty()) {
                    Toast.makeText(LeaguePop.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else if (nameOfLeague.isEmpty()) {
                    Toast.makeText(LeaguePop.this, "League name is required", Toast.LENGTH_SHORT).show();
                }

                else if (sportForLeague.isEmpty()) {
                    Toast.makeText(LeaguePop.this, "Type of sport is required", Toast.LENGTH_SHORT).show();
                }

                else {
                    // TODO 24/02/2020 - Replace tempUser with the current user of the app.
                    Member currentUser = new Member("Mike", "Tyson", "bigmike@punchface.gov", "12312312312");
                    League newLeague = new League(nameOfLeague, currentUser, sportForLeague, descriptionOfLeague);

                    // TODO 24/02/2020 - Insert new league into league list, send to database.

                    finish();
                }
            }
        });



    }


}
