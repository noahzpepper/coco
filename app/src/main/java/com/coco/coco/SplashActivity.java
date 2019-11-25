package com.coco.coco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AUTH";

    private FirebaseAuth auth;
    private FirebaseUser user;

    boolean isCreatingAccount = true;

    private EditText nameText, emailText, passwordText;
    private Button goButton;
    private TextView signInText;
    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        signInText = findViewById(R.id.signInText);
        goButton = findViewById(R.id.goButton);
        splashImage = findViewById(R.id.splashImage);
        signInText.setOnClickListener(this);
        goButton.setOnClickListener(this);

        nameText.requestFocus();
        setSplashImageVisiblity(View.VISIBLE);

        //splash delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    transitionToMain();
                } else {
                    setSplashImageVisiblity(View.INVISIBLE);
                }
            }
        }, 2000);
    }

    private void setSplashImageVisiblity(int visibility) {
        splashImage.setVisibility(visibility);
        int oppositeVisibility = (visibility == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE;
        emailText.setVisibility(oppositeVisibility);
        passwordText.setVisibility(oppositeVisibility);
        signInText.setVisibility(oppositeVisibility);
        goButton.setVisibility(oppositeVisibility);
        nameText.setVisibility(oppositeVisibility);
    }

    private void transitionToMain() {
        Intent i = new Intent (SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
    }

    private void createAccount(final String name, String email, String password) {
        if (name == null || name.isEmpty()) {
            toast("Enter a name");
            return;
        }
        if (email == null || email.isEmpty()) {
            toast("Enter an email");
            return;
        }
        if (password == null || password.isEmpty()) {
            toast("Enter a password");
            return;
        }
        if (password.length() < 6) {
            toast("Enter a 6+ character password");
            return;
        }

        setSplashImageVisiblity(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            storeUserInDatabase(auth.getCurrentUser().getUid(), name);
                            transitionToMain();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            toast("Authentication failed.");
                            setSplashImageVisiblity(View.INVISIBLE);

                        }
                    }
                });
    }

    private void storeUserInDatabase(String uid, String name) {
        if (uid == null) { return; }
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = root.child("users").child(uid);
        userRef.child("name").setValue(name);
        userRef.child("photoUrl").setValue("");
        userRef.child("blockedCompanies").setValue(false);
        userRef.child("blockedIngredients").setValue(false);
    }

    private void signIn(String email, String password) {
        if (email == null || email.isEmpty()) {
            toast("Enter an email");
            return;
        }
        if (password == null || password.isEmpty()) {
            toast("Enter a password");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            transitionToMain();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            toast("Authentication failed.");
                        }
                    }
                });
    }

    private void toast(String s) {
        Toast.makeText(SplashActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signInText:
                if (isCreatingAccount) {
                    nameText.setVisibility(View.INVISIBLE);
                    signInText.setText("I'm new to CoCo!");
                    goButton.setText("Sign In");
                } else {
                    nameText.setVisibility(View.VISIBLE);
                    signInText.setText("I already have an account");
                    goButton.setText("Create Account");
                }
                isCreatingAccount = !isCreatingAccount;
                break;
            case R.id.goButton:
                if (isCreatingAccount) {
                    createAccount(nameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString());
                } else {
                    signIn(emailText.getText().toString(), passwordText.getText().toString());
                }
                break;
        }
    }
}
