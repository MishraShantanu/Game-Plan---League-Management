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

public class loginActivity extends AppCompatActivity {
     EditText emailId,password;
     Button buttonSignup;
     TextView tvSignIn;
     FirebaseAuth mFirebaseAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    Toast.makeText(loginActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }else if(!(pass.isEmpty()&&email.isEmpty())){
                    System.out.println(email);
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(loginActivity.this,"SignUp Unsucessful,plz try again",Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(loginActivity.this,MainActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(loginActivity.this,"Error Occurred..!!,try again..!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(loginActivity.this,SigninActivity.class);
                    startActivity(i);
                }
            });


    }
}
