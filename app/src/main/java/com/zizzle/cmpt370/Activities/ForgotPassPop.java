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
import com.google.firebase.auth.FirebaseAuth;
import com.zizzle.cmpt370.R;

public class ForgotPassPop extends Activity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.setDimAmount((float) 0.4);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.forgot_pass_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Scale of the pop up window
        getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.5));


        // Send email ==============================================================================
        final EditText email = findViewById(R.id.forgot_email);

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(ForgotPassPop.this);

                // Get the email address.
                String emailString = email.getText().toString();

                // Email address is empty.
                if (emailString.isEmpty()) {
                    email.setError("Email address is required");
                    email.requestFocus();
                }

                // Email address is not empty.
                else {
                    // send the user a password reset email through firebase
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // email successfully sent, inform the user they must reset their password via email
                                Toast.makeText(ForgotPassPop.this, "Password reset email sent", Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(ForgotPassPop.this, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
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

    // Go back to previous activity
    @Override
    public void onBackPressed() {
        finish();
    }
}