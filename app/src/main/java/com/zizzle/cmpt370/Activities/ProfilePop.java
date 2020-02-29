package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseException;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.R;

public class ProfilePop extends Activity{

    EditText memberName, phoneNumber, email, password;
    Button submitButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.profile_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));


        // Gathering Input =========================================================================
        memberName  = findViewById(R.id.displayName);
        phoneNumber = findViewById(R.id.phoneNumberInput);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String memberNameString = memberName.getText().toString();
                final String phoneNumberString = phoneNumber.getText().toString();
                final String emailString = email.getText().toString();
                final String passwordString = password.getText().toString();

                if (!memberNameString.isEmpty()) {
                    Toast.makeText(ProfilePop.this, "Updating display name", Toast.LENGTH_SHORT).show();
                }

                if (!phoneNumberString.isEmpty()) {
                    Toast.makeText(ProfilePop.this, "Updating phone number", Toast.LENGTH_SHORT).show();
                }

                if (!emailString.isEmpty()) {
                    Toast.makeText(ProfilePop.this, "Updating email", Toast.LENGTH_SHORT).show();
                }

                if (!passwordString.isEmpty()) {
                    Toast.makeText(ProfilePop.this, "Updating password", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });



    }
}
