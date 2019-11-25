package com.coco.coco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((TextView) findViewById(R.id.textView)).setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        findViewById(R.id.userAccountButton).setOnClickListener(this);
        findViewById(R.id.userFilterButton).setOnClickListener(this);
        findViewById(R.id.detailedViewButton).setOnClickListener(this);
        findViewById(R.id.mainViewButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userAccountButton:
                startActivity(new Intent(MainActivity.this, UserAccountActivity.class));
                break;
            case R.id.userFilterButton:
                startActivity(new Intent(MainActivity.this, UserFilterActivity.class));
                break;
            case R.id.detailedViewButton:
                startActivity(new Intent(MainActivity.this, DetailedViewActivity.class));
                break;
            case R.id.mainViewButton:
                startActivity(new Intent(MainActivity.this, MainViewActivity.class));
                break;
        }
    }
}
