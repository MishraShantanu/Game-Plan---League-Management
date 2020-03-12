package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.R;

import static android.support.constraint.Constraints.TAG;

public class ProfilePop extends Activity{

    EditText memberName, phoneNumber, email, password;
    Button submitButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
       // password = findViewById(R.id.passwordInput);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String memberNameString = memberName.getText().toString();
                final String phoneNumberString = phoneNumber.getText().toString();
                final String emailString = email.getText().toString();
              //  final String passwordString = password.getText().toString();

                Member user;


                firebaseAuth = FirebaseAuth.getInstance();

                firebaseDatabase = FirebaseDatabase.getInstance();

                databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());



                if (!memberNameString.isEmpty()) {
                    Toast.makeText(ProfilePop.this, "Updating display name", Toast.LENGTH_SHORT).show();

                    if (!phoneNumberString.isEmpty()) {
                        Toast.makeText(ProfilePop.this, "Updating phone number", Toast.LENGTH_SHORT).show();

                        if (!emailString.isEmpty()) {

                               user = new Member (memberNameString,emailString,phoneNumberString,firebaseAuth.getCurrentUser().getUid());

                                FirebaseUser userpass  = FirebaseAuth.getInstance().getCurrentUser();

                                userpass.updateEmail(emailString);
                                Toast.makeText(ProfilePop.this, "Updating email", Toast.LENGTH_SHORT).show();

                                databaseReference.setValue(user);



                                databaseReference.push();
                                FirebaseAuth.getInstance().signOut();
                                Intent toLogOut = new Intent(ProfilePop.this, SigninActivity.class);
                                toLogOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(toLogOut);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                        }
                    }

                }









                finish();
            }
        });



    }
}
