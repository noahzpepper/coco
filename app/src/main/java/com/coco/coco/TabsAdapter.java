package com.coco.coco;

import android.util.Log;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "TABS";

    private int numTabs;

    public TabsAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int i) {
        return RecyclerFragment.newInstance(MainViewActivity.CATEGORIES[i]);
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
