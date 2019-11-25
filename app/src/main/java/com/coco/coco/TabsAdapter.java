package com.coco.coco;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "TABS";

    int mNumOfTabs;

    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Log.d(TAG, "case 0");
                RecyclerFragment all = new RecyclerFragment();
                return all;
            case 1:
                Log.d(TAG, "case 1");
                RecyclerFragment face = new RecyclerFragment();
                return face;
            case 2:
                Log.d(TAG, "case 2");
                RecyclerFragment eyes = new RecyclerFragment();
                return eyes;
            case 3:
                Log.d(TAG, "case 3");
                RecyclerFragment lips = new RecyclerFragment();
                return lips;
            case 4:
                Log.d(TAG, "case 4");
                RecyclerFragment brows = new RecyclerFragment();
                return brows;
            case 5:
                Log.d(TAG, "case 5");
                RecyclerFragment skincare = new RecyclerFragment();
                return skincare;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
