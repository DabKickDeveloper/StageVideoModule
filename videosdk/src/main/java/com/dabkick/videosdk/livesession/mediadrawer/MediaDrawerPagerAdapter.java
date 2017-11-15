package com.dabkick.videosdk.livesession.mediadrawer;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class MediaDrawerPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tabTitles;
    private Context context;

    MediaDrawerPagerAdapter(FragmentManager fm, Context context, ArrayList<String> categoryList) {
        super(fm);
        this.context = context;
        tabTitles = categoryList;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public Fragment getItem(int position) {
        return MediaFragment.newInstance(tabTitles.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}

