package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zizzle.cmpt370.R;

public class ReauthenticationPop extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.reauthentication_pop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Scale pop up window.
        getWindow().setLayout((int)(dm.widthPixels * 0.8), (int)(dm.heightPixels * 0.4));


        // Gathering Input =========================================================================
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText passwordInput  = findViewById(R.id.reauth_password);
                String password = passwordInput.getText().toString();

                // Password is empty.
                if (password.isEmpty()) {
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                }

                // Password has input.
                else {

                    // TODO Do some reauthorizing.

                    // Reauthorizing was successful.
                    if (/* Successful Condition */ true) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                    }

                    // Reauthorizing was unsuccessful.
                    else
                        Toast.makeText(ReauthenticationPop.this,
                                "Password is incorrect, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Since we are reauthorizing, take to sign in page if tries to leave.
    @Override
    public void onBackPressed() {

        // TODO remove current user so they don't immediately sign in again.

        startActivity(new Intent(ReauthenticationPop.this, SigninActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
