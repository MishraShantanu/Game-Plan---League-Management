package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zizzle.cmpt370.R;

public class ScoreConfirmPop extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.score_confirmation_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.8));


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

    // Close activity if clicked outside of page.
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect dialogBounds = new Rect();
            getWindow().getDecorView().getHitRect(dialogBounds);
            if (!dialogBounds.contains((int) event.getX(), (int) event.getY())) {
                Intent returnResult = new Intent();
                returnResult.putExtra("RESULT", "false");
                setResult(2, returnResult);
                finish();
                return false;
            }
        }
        return false;
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
