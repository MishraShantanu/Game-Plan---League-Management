package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.GameTime;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class GamePop extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    TextView txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TeamInfo opponentInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.game_popup);

        //Set Status Bar Color to Primary Dark Color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));


        // Spinner =================================================================================
        final TeamInfo currentTeam = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");
        LeagueInfo leagueInfo = new LeagueInfo(currentTeam.getLeagueName());

        final ArrayList<TeamInfo> teamInfos = new ArrayList<>();

        DatabaseReference leagueReference = FirebaseDatabase.getInstance().getReference().child("Leagues").child(leagueInfo.getDatabaseKey()).child("teamsInfoMap");
        leagueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamInfos.clear();

                // Gather all teams inside the same league, excluding user team.
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    TeamInfo currentTeamInfo = ds.getValue(TeamInfo.class);
                    teamInfos.add(currentTeamInfo);
                }
                // this team cannot play against itself, remove this as an option of teams to play against
                teamInfos.remove(currentTeam);
                // Create a Spinner
                Spinner againstTeam = findViewById(R.id.team_spinner);
                againstTeam.setOnItemSelectedListener(GamePop.this);

                // Creating adapter for spinner
                ArrayAdapter<TeamInfo> dataAdapter = new ArrayAdapter<>(GamePop.this, R.layout.game_spinner, teamInfos);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                againstTeam.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_popup);

        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the user's input data
                EditText locationText = findViewById(R.id.locationInput);
                String gameLocation = locationText.getText().toString();

                String timeString = txtTime.getText().toString();

                String dateString = txtDate.getText().toString();

                if(gameLocation.isEmpty()){
                    Toast.makeText(GamePop.this, "Game location is required", Toast.LENGTH_SHORT).show();
                }
                else if(timeString.isEmpty()){
                    Toast.makeText(GamePop.this, "Time of game is required", Toast.LENGTH_SHORT).show();
                }
                else if(dateString.isEmpty()){
                    Toast.makeText(GamePop.this, "Date of game is required", Toast.LENGTH_SHORT).show();
                }
                else if(opponentInfo == null){
                    Toast.makeText(GamePop.this, "Opponent team is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    // convert our timeString into hours, and minutes, this time string will have the format hour:minutes, hours from 1-23
                    String[] timeArray = timeString.split(":");
                    String hourString = timeArray[0];
                    String minuteString = timeArray[1];
                    int hour = Integer.valueOf(hourString);
                    int minutes = Integer.valueOf(minuteString);

                    // convert our dateString into year, month, day, this date string has the format day-month-year
                    String[] dateArray = dateString.split("-");
                    String dayString = dateArray[0];
                    String monthString = dateArray[1];
                    String yearString = dateArray[2];
                    int day = Integer.valueOf(dayString);
                    int month = Integer.valueOf(monthString);
                    int year = Integer.valueOf(yearString);

                    GameTime gameDate = new GameTime(year,month,day,hour,minutes);
                    Game newGame = new Game(currentTeam, opponentInfo, gameDate, gameLocation);

                    // add this game to the teams playing
                    Storage.addGameToTeams(currentTeam,opponentInfo,newGame);
                    finish();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        opponentInfo = (TeamInfo)parent.getItemAtPosition(position);
    }


    // Date and Time Picker
    @Override
    public void onClick(View v) {
        // Date picker
        if (v == btnDatePicker) {

            // Get Current Date, the date picker will then start on this date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setTextColor(ContextCompat.getColor(GamePop.this, R.color.colorText));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        // Time picker
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            // Add a zero to pad the time if less than ten minutes.
                            if (minute < 10) txtTime.setText(hourOfDay + ":0" + minute);
                            else txtTime.setText(hourOfDay + ":" + minute);

                            txtTime.setTextColor(ContextCompat.getColor(GamePop.this, R.color.colorText));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


    // Unused
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


}
