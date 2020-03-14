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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.R;

public class SignupActivity extends AppCompatActivity {
    EditText emailId, password, displayName, phoneNumber;
    Button buttonSignup;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Set Status Bar Color to Primary Dark Color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.Signup_Email);
        password = findViewById(R.id.Signup_Password);
        buttonSignup = findViewById(R.id.Signup_Button);
        tvSignIn = findViewById(R.id.Signup_HaveAccount);
        phoneNumber = findViewById(R.id.Signup_Phone);

        displayName = findViewById(R.id.Signup_Name);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailId.getText().toString();
                String pass = password.getText().toString();
                String phone = phoneNumber.getText().toString();
                // Remove all non-numeric characters
                phone = phone.replaceAll("\\D", "");


                if (pass.isEmpty() && email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }

                else if (email.isEmpty()) {
                    emailId.setError("Email address required");
                    emailId.requestApplyInsets();
                }

                else if (pass.isEmpty()) {
                    password.setError("Password required");
                    password.requestFocus();
                }

                else if (phone.isEmpty()) {
                    phoneNumber.setError("Phone number required");
                    phoneNumber.requestFocus();
                }

                else if (!(pass.isEmpty() && email.isEmpty())) {
                    System.out.println(email);
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Sign up was unsuccessful, please try again", Toast.LENGTH_SHORT).show();

                            } else {
                                // add the newly created Member to the database
                                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                                Member member = new Member(displayName.getText().toString(), emailId.getText().toString(), phoneNumber.getText().toString(), fbUser.getUid());
                                Storage.writeMember(member);
                                // add the user's display name to firebase authentication
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName.getText().toString()).build();
                                fbUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    // this toast is for testing only
                                                    Toast.makeText(SignupActivity.this, "couldn't add display name to user profile", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // head to main activity
                                                    Intent intoMain = new Intent(SignupActivity.this, HomeActivity.class);
                                                    intoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intoMain);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                }
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });



        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}
