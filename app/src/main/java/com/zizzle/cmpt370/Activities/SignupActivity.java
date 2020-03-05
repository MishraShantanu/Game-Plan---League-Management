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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.R;
import com.zizzle.cmpt370.Useless.homepageWithMenu;

public class SignupActivity extends AppCompatActivity {
    EditText emailId, password, displayName;
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

        displayName = findViewById(R.id.Signup_Name);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter an email address");
                    emailId.requestApplyInsets();
                } else if (pass.isEmpty()) {
                    password.setError("Enter a password");
                    password.requestFocus();

                } else if (pass.isEmpty() && email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(pass.isEmpty() && email.isEmpty())) {
                    System.out.println(email);
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Sign up was unsuccessful, please try again", Toast.LENGTH_SHORT).show();

                            } else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference root = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                String newUserId = root.getKey();
                                Member member = new Member(displayName.getText().toString(), emailId.getText().toString(), "987654321",newUserId);
                                root.setValue(member);
                                System.out.println(member.toString());
                                root.push();
                                Intent intoMain = new Intent(SignupActivity.this, homepageWithMenu.class);
                                intoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intoMain);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "ERROR occurred, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    //When back button is pressed, we want to just close the menu, not close the activity
    @Override
    public void onBackPressed() { }

}
