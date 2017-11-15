package com.dabkick.videosdk.livesession.mediadrawer;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

class MediaDrawerPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles;

    MediaDrawerPagerAdapter(FragmentManager fm, List<String> categoryList) {
        super(fm);
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

