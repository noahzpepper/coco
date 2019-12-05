package com.coco.coco;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "TABS";

    private int numTabs;
    private RecyclerFragment[] tabs;
    private ProductFilter filter;

    public TabsAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
        this.tabs = new RecyclerFragment[numTabs];
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = RecyclerFragment.newInstance(MainViewActivity.CATEGORIES[i]);
        tabs[i] = (RecyclerFragment) f;
        if (filter != null) {
            tabs[i].setFilter(filter);
        }
        return f;
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public void setFilter(ProductFilter filter) {
        this.filter = filter;
        for (RecyclerFragment tab : tabs) {
            if (tab != null) {
                tab.setFilter(filter);
            }
        }
    }

    public void refreshTabs() {
        for (RecyclerFragment tab : tabs) {
            if (tab != null) {
                tab.getDataFromDatabase();
            }
        }
    }
}
