package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zizzle.cmpt370.R;

public class ForgotPassPop extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.forgot_pass_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Scale of the pop up window
        getWindow().setLayout((int)(dm.widthPixels * 0.8), (int)(dm.heightPixels * 0.5));


        // Send email ==============================================================================
        final EditText email = findViewById(R.id.forgot_email);

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the email address.
                String emailString = email.getText().toString();

                // Email address is empty.
                if (emailString.isEmpty()) {
                    email.setError("Email address is required");
                    email.requestFocus();
                }

                // Email address is not empty.
                else {

                    // TODO Firebase stuff

                    finish();
                }
            }
        });
    }



    // Go back to previous activity
    @Override
    public void onBackPressed() {
        finish();
    }
}