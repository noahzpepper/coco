package com.coco.coco;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainViewActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";
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

    }
}
