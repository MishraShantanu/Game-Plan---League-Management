package com.zizzle.cmpt370.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zizzle.cmpt370.R;

public class SigninActivity extends AppCompatActivity {
    EditText emailId, password;
    Button buttonSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthstatelistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Set Status Bar Color to Primary Dark Color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.Signin_Email);
        password = findViewById(R.id.Signin_Password);
        buttonSignIn = findViewById(R.id.Signin_Button);
        tvSignUp = findViewById(R.id.Signin_HaveAccount);

        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mfirebaseUser = mFirebaseAuth.getCurrentUser();

                if (mfirebaseUser != null) {
                    Intent intoMain = new Intent(SigninActivity.this, HomeActivity.class);
                    intoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intoMain);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                else {
                    Toast.makeText(SigninActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if (pass.isEmpty() && email.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else if (email.isEmpty()) {
                    emailId.setError("Email address is required");
                    emailId.requestApplyInsets();
                }

                else if (pass.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                }

                else if (!(pass.isEmpty() && email.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SigninActivity.this, "Combination of email and password is invalid", Toast.LENGTH_SHORT).show();

                            }

                            else {
                                Intent intoMain = new Intent(SigninActivity.this, HomeActivity.class);
                                intoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intoMain);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }

                        ;
                    });
                }

                else {
                    Toast.makeText(SigninActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSignUp = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(inSignUp);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() { }


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthstatelistner);
    }
}
