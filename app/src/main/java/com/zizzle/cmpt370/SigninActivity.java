package com.zizzle.cmpt370;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    EditText emailId,password;
    Button buttonSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth ;

    private FirebaseAuth.AuthStateListener mAuthstatelistner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId  = findViewById(R.id.editText3);
        password = findViewById(R.id.editText4);
        buttonSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView3);

        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mfirebaseUser = mFirebaseAuth.getCurrentUser();

                if(mfirebaseUser != null){
                    Toast.makeText(SigninActivity.this," You are  logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SigninActivity.this,MainActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(SigninActivity.this,"Please Login",Toast.LENGTH_SHORT).show();

                }
            }
        };

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if(email.isEmpty()){
                    emailId.setError("Plz enter  email id");
                    emailId.requestApplyInsets();
                }
                else if(pass.isEmpty()){
                    password.setError("Enter a password");
                    password.requestFocus();

                }else if(pass.isEmpty()&&email.isEmpty()){
                    Toast.makeText(SigninActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }else if(!(pass.isEmpty()&&email.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SigninActivity.this,"Login Error, Please try again..",Toast.LENGTH_SHORT).show();

                        }else{
                                Intent intoMain = new Intent(SigninActivity.this,MainActivity.class);
                                startActivity(intoMain);
                            }

                    };
                });

            }
                else{
                    Toast.makeText(SigninActivity.this,"Error Occurred..!!,try again..!!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inSignUp = new Intent(SigninActivity.this,loginActivity. class);
                startActivity(inSignUp);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthstatelistner);
    }
}
