package com.coco.coco;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MAIN";

    ImageView accountButton, filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_view);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Face"));
        tabLayout.addTab(tabLayout.newTab().setText("Eyes"));
        tabLayout.addTab(tabLayout.newTab().setText("Lips"));
        tabLayout.addTab(tabLayout.newTab().setText("Brows"));
        tabLayout.addTab(tabLayout.newTab().setText("Skincare"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        findViewById(R.id.tabs).bringToFront();

        final ViewPager viewPager = findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        accountButton = findViewById(R.id.mainAccountButton);
        filterButton = findViewById(R.id.mainFilterButton);

        //mainIncluded.setOnClickListener(this);
        accountButton.setOnClickListener(this);
        filterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "" + String.format("onClick: %d", view.getId()));
        switch (view.getId()) {
            case R.id.mainFilterButton:
                startActivity(new Intent(MainViewActivity.this, UserFilterActivity.class));
                break;
            case R.id.mainAccountButton:
                startActivity(new Intent(MainViewActivity.this, UserAccountActivity.class));
                break;
        }
    }
}
