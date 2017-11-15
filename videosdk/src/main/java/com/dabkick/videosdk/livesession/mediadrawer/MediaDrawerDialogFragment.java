package com.dabkick.videosdk.livesession.mediadrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;

import java.util.ArrayList;


public class MediaDrawerDialogFragment extends DialogFragment {

    public static MediaDrawerDialogFragment newInstance() {
        MediaDrawerDialogFragment mediaDrawerDialogFragment = new MediaDrawerDialogFragment();
        Bundle args = new Bundle();
        mediaDrawerDialogFragment.setArguments(args);
        return mediaDrawerDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.layout_content_dialog_fragment, container);

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("1");
        categoryList.add("2");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = parent.findViewById(R.id.viewpager);
        MediaDrawerPagerAdapter adapter = new MediaDrawerPagerAdapter(
                getChildFragmentManager(), getContext(), categoryList);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            // onPageSelected will not be called with only 1 or 0 item in initial list
            @Override public void onPageSelected(int position) {
                if (position == categoryList.size() - 1) {
                    ArrayList<String> newCategories = ((SdkApp) SdkApp.getAppContext()).getDabKickSession().
                            getDabKickVideoProvider().provideCategories(position);
                    if (newCategories == null) {
                        // partner app did not provide any new categories
                        return;
                    }
                    categoryList.addAll(newCategories);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override public void onPageScrollStateChanged(int state) {}
        });

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = parent.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return parent;
    }

}
