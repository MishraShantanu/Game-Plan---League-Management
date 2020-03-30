package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

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
                        hideKeyboard(ProfilePop.this);

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
                        else if(phoneNumberString.length()!=10){
                            phoneNumber.setError("Phone Number Must be 10 Digits");
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
                                // pass the old and new emails for this user to the reauthentication popup
                                Intent reauthenticationIntent = new Intent(ProfilePop.this, ReauthenticationPop.class);
                                reauthenticationIntent.putExtra("OLD_EMAIL",user.getEmail());
                                reauthenticationIntent.putExtra("NEW_EMAIL",emailString);

                                // send the user to the reauthentication popup as the user must reauthenticate when changing their login information (in this case email)
                                startActivity(reauthenticationIntent);
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                            }
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Close activity if clicked outside of page.
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect dialogBounds = new Rect();
            getWindow().getDecorView().getHitRect(dialogBounds);
            if (!dialogBounds.contains((int) event.getX(), (int) event.getY())) {
                finish();
                return false;
            }
        }
        return false;
    }

    // Used to hide keyboard.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
