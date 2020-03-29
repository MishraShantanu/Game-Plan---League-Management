package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.zizzle.cmpt370.R;


public class RemoveLeaguePop extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.remove_teams_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels * 0.8), (int)(dm.heightPixels * 0.4));


        // Buttons =================================================================================
        // Yes button.
        Button yesButton = findViewById(R.id.confirmButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnResult = new Intent();
                returnResult.putExtra("RESULT", "true");
                setResult(3, returnResult);
                finish();
            }
        });


        // No button.
        Button noButton = findViewById(R.id.denyButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnResult = new Intent();
                returnResult.putExtra("RESULT", "false");
                setResult(3, returnResult);
                finish();
            }
        });
    }


    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() {
        Intent returnResult = new Intent();
        returnResult.putExtra("RESULT", "false");
        setResult(3, returnResult);
        finish();
    }
}
