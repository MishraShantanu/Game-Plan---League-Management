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
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                final String nameOfLeague = leagueName.getText().toString();
                final String sportForLeague = typeOfSport.getText().toString();
                final String descriptionOfLeague = description.getText().toString();

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
                    Member tempUser = new Member("Mike","Tyson","BigMike@email.com","13066975541");
                    League newLeague = new League(nameOfLeague,tempUser,sportForLeague,descriptionOfLeague);
                    // add newLeague to the database
                    try{
                        Storage.writeLeague(newLeague);
                    }
                    catch (IllegalStateException e){
                        // this exception is thrown if there is already a league with our new league's name, display error message
                        Toast.makeText(LeaguePop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    catch (DatabaseException e){
                        // this exception is thrown if database operations fail, nothing we can do except try again
                        Toast.makeText(LeaguePop.this, "Failed to create league, please try again", Toast.LENGTH_SHORT).show();
                    }
                    // TODO cannot add league to database if it has teams, probably an infinite nesting issue




                    // TODO 24/02/2020 - Insert new league into league list, send to database.

                    finish();
                }
            }
        });



    }


}
