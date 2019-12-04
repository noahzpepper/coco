package com.coco.coco;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.coco.coco.model.Price;
import com.coco.coco.model.Product;
import com.google.android.material.tabs.TabLayout;

public class MainViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MAIN";

    public static final String[] CATEGORIES = new String[]{"All", "Face", "Eyes", "Lips", "Brows", "Skincare"};

    ImageView accountButton, filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_view);

        TabLayout tabLayout = findViewById(R.id.tabs);
        for (String category : CATEGORIES) {
            tabLayout.addTab(tabLayout.newTab().setText(category));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        findViewById(R.id.tabs).bringToFront();

        final ViewPager viewPager = findViewById(R.id.view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        accountButton = findViewById(R.id.mainAccountButton);
        filterButton = findViewById(R.id.mainFilterButton);

        accountButton.setOnClickListener(this);
        filterButton.setOnClickListener(this);

        Price.initializeLogos();

        //TEMPORARY - remove this
        new ProductDatabaseUtil().run();
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
