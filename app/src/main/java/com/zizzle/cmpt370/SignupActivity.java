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

public class SignupActivity extends AppCompatActivity {
     EditText emailId,password;
     Button buttonSignup;
     TextView tvSignIn;
     FirebaseAuth mFirebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId  = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        buttonSignup = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView2);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignupActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }else if(!(pass.isEmpty()&&email.isEmpty())){
                    System.out.println(email);
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this,"SignUp Unsucessful,plz try again",Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignupActivity.this,"Error Occurred..!!,try again..!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SignupActivity.this,SigninActivity.class);
                    startActivity(i);
                }
            });


    }
}
