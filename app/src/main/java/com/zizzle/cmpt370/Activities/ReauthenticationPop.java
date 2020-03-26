package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
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
                    // get the new and old emails from ProfilePop
                    String oldEmail = getIntent().getStringExtra("OLD_EMAIL");
                    final String newEmail = getIntent().getStringExtra("NEW_EMAIL");
                    // before we can change the user's email, we must reauthenticate them
                    // reauthenticate the user with their old email and password
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(oldEmail,password);
                    currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                // user gave the right password, update the user's email
                                currentUser.updateEmail(newEmail);
                                // update the user's email on the database
                                MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
                                Storage.updateEmail(currentUserInfo,newEmail);

                                finish();
                                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                            }
                            else{
                                // Reauthorizing was unsuccessful.
                                Toast.makeText(ReauthenticationPop.this,
                                        "Password is incorrect, Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
