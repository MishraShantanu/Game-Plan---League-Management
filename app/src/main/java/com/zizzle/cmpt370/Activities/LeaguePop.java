package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.league_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));


        // Gathering Input =========================================================================
        leagueName = findViewById(R.id.leagueName);
        typeOfSport = findViewById(R.id.sportInput);
        description = findViewById(R.id.descriptionInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(LeaguePop.this);

                final String nameOfLeague = leagueName.getText().toString();
                final String sportForLeague = typeOfSport.getText().toString();
                final String descriptionOfLeague = description.getText().toString();

                if (nameOfLeague.isEmpty()) {
                    leagueName.setError("League name required");
                    leagueName.requestFocus();
                } else if (sportForLeague.isEmpty()) {
                    typeOfSport.setError("Sport required");
                    typeOfSport.requestFocus();
                }else if (descriptionOfLeague.isEmpty()){
                    description.setError("Description required");
                    description.requestFocus();
                }
                else {
                    // determine if the league being created has a unique name
                    DatabaseReference sameNameReference = FirebaseDatabase.getInstance().getReference().child("Leagues").child(nameOfLeague);
                    sameNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                // there is league with nameOfLeague already, cannot create this new league
                                leagueName.setError("League name must be unique");
                                leagueName.requestFocus();
                            }
                            else{
                                // the chosen league name is unique, we can create this league
                                League newLeague = new League(nameOfLeague, CurrentUserInfo.getCurrentUserInfo(), sportForLeague, descriptionOfLeague);
                                // add this new league to the database
                                Storage.writeLeague(newLeague);

                                // close this pop-up activity
                                finish();

                                // redirect to the new league's page
                                Intent teamsIntent = new Intent(LeaguePop.this, TeamsActivity.class);
                                // pass the name of the league clicked on to this intent, so it can be accessed from the TeamsActivity
                                teamsIntent.putExtra("LEAGUE_CLICKED", newLeague.getName());
                                startActivity(teamsIntent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    // Close activity if clicked outside of page.
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect dialogBounds = new Rect();
            getWindow().getDecorView().getHitRect(dialogBounds);
            if (!dialogBounds.contains((int) event.getX(), (int) event.getY())) {
                finish();
                return false;
            }
        }
        return false;
    }

    // Used to hide keyboard.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
