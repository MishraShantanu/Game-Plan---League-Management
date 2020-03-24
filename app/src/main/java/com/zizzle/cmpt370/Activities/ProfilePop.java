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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.Member;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.R;

import static android.support.constraint.Constraints.TAG;

public class ProfilePop extends Activity{

    EditText memberName, phoneNumber, email;
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

        firebaseAuth = FirebaseAuth.getInstance();

        MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserInfo.getDatabaseKey());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Member user = dataSnapshot.getValue(Member.class);

                // DisplayName Text ==========================================================================
                final TextView userName = (TextView) findViewById(R.id.displayName);
                userName.setText(user.getDisplayName());

                // Email Text ==========================================================================
                final TextView email = (TextView) findViewById(R.id.emailInput);
                email.setText(user.getEmail());

                // Phone Number Text ==========================================================================
                final TextView phoneNumber = (TextView) findViewById(R.id.phoneNumberInput);
                phoneNumber.setText(user.getPhoneNumber().replace("-",""));

                submitButton = findViewById(R.id.submitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String memberNameString = memberName.getText().toString();
                        final String phoneNumberString = phoneNumber.getText().toString();
                        final String emailString = email.getText().toString();

                        if(memberNameString.isEmpty()){
                            memberName.setError("Display Name Required");
                            memberName.requestFocus();
                        }
                        else if(phoneNumberString.isEmpty()){
                            phoneNumber.setError("Phone Number Required");
                            phoneNumber.requestFocus();
                        }
                        else if(phoneNumberString.length()!=11){
                            phoneNumber.setError("Phone Number Must be 11 Digits");
                            phoneNumber.requestFocus();
                        }
                        else if(emailString.isEmpty()){
                            email.setError("Email Required");
                            email.requestFocus();
                        }
                        else{
                            // determine which fields the user has changed, only update what is necessary
                            boolean displayNameChanged = !user.getDisplayName().equals(memberNameString);
                            boolean phoneNumberChanged = !user.getPhoneNumber().equals(phoneNumberString);
                            boolean emailChanged = !user.getEmail().equals(emailString);
                            MemberInfo currentUserInfo = new MemberInfo(user);
                            if(displayNameChanged){
                                // write the user's new name to the database
                                Storage.updateDisplayName(user,memberNameString);
                                // update the current stored MemberInfo for this user to use the new name
                                CurrentUserInfo.initializeMemberInfo(user.getUserID(),memberNameString);
                                // update the user's profile so firebase authentication keeps track of the updated name
                                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(memberNameString).build();
                                fbUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {}
                                });
                            }
                            if(phoneNumberChanged){
                                Storage.updatePhoneNumber(currentUserInfo,phoneNumberString);
                            }
                            if(emailChanged){
                                Storage.updateEmail(currentUserInfo,emailString);
                                // make sure firebase authentication keeps track of the updated email
                                FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
                                fbUser.updateEmail(emailString);

                                startActivity(new Intent(ProfilePop.this, ReauthenticationPop.class));
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);

                            }
                            finish();
                            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
