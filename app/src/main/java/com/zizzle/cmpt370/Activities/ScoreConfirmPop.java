package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.Game;
import com.zizzle.cmpt370.Model.GameTime;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class ScoreConfirmPop extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.score_confirmation_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));


        // Displaying scores =======================================================================
        String currentTeamName = (String) getIntent().getSerializableExtra("TEAM_NAME");
        String opponentTeamName = (String) getIntent().getSerializableExtra("OPPONENT_NAME");
        String currentTeamScore = (String) getIntent().getSerializableExtra("TEAM_SCORE");
        String opponentTeamScore = (String) getIntent().getSerializableExtra("OPPONENT_SCORE");

        TextView teamName = findViewById(R.id.yourTeamNameText);
        TextView opponentName = findViewById(R.id.opponentTeamNameText);
        TextView teamScore = findViewById(R.id.yourScore);
        TextView opponentScore = findViewById(R.id.opponentScore);

        teamName.setText(currentTeamName);
        opponentName.setText(opponentTeamName);
        teamScore.setText(currentTeamScore);
        opponentScore.setText(opponentTeamScore);


        // Confirm button ==========================================================================
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnResult = new Intent();
                returnResult.putExtra("RESULT", "true");
                setResult(2, returnResult);
                finish();
            }
        });


        // Deny button =============================================================================
        Button denyButton = findViewById(R.id.denyButton);
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnResult = new Intent();
                returnResult.putExtra("RESULT", "false");
                setResult(2, returnResult);
                finish();
            }
        });
    }



    // Go back to previous activity
    @Override
    public void onBackPressed() {
        Intent returnResult = new Intent();
        returnResult.putExtra("RESULT", "false");
        setResult(2, returnResult);
        finish();
    }
}
