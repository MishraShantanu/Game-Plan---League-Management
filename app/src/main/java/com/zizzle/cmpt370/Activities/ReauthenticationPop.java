package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.reauthentication_pop);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Scale pop up window.
        getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.4));


        // Gathering Input =========================================================================
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(ReauthenticationPop.this);

                EditText passwordInput = findViewById(R.id.reauth_password);
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
                    AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, password);
                    currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // user gave the right password, update the user's email
                                currentUser.updateEmail(newEmail);
                                // update the user's email on the database
                                MemberInfo currentUserInfo = CurrentUserInfo.getCurrentUserInfo();
                                Storage.updateEmail(currentUserInfo, newEmail);

                                finish();
                                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
                            } else {
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

    // Close activity if clicked outside of page.
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
