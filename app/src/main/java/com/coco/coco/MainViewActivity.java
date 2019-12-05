package com.coco.coco;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.coco.coco.model.Price;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainViewActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "MAIN";

    public static final String[] CATEGORIES = new String[]{"All", "Face", "Eyes", "Lips", "Brows", "Skincare"};
    private static final int FILTER_CODE = 1;

    private ProductFilter filter;

    TabsAdapter tabsAdapter;

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
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

        //listeners
        findViewById(R.id.mainAccountButton).setOnClickListener(this);
        findViewById(R.id.mainFilterButton).setOnClickListener(this);
        ((AutoCompleteTextView) findViewById(R.id.input_search)).addTextChangedListener(this);

        Price.initializeLogos();
        filter = ProductFilter.FILTER_DEFAULT;

        //temporary for adding products to db
        try {
            new ProductFactory().run();
        } catch (AssertionError e) {
            Toast.makeText(this, "Product not added to database: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "" + String.format("onClick: %d", view.getId()));
        switch (view.getId()) {
            case R.id.mainFilterButton:
                Intent intent = new Intent(MainViewActivity.this, UserFilterActivity.class);
                intent.putExtra("filter", filter);
                startActivityForResult(intent, FILTER_CODE);
                break;
            case R.id.mainAccountButton:
                startActivity(new Intent(MainViewActivity.this, UserAccountActivity.class));
                break;
        }
    }

    /* Receiving from filtering activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_CODE) {
            if (resultCode == RESULT_OK) {
                ProductFilter filter = (ProductFilter) data.getSerializableExtra("filter");
                this.filter = filter;
                tabsAdapter.setFilter(filter);
                tabsAdapter.refreshTabs();
            }
        }
    }

    //search bar
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        filter.setSearchText(charSequence.toString());
        tabsAdapter.refreshTabs();
    }

    @Override
    public void afterTextChanged(Editable editable) {}
}
