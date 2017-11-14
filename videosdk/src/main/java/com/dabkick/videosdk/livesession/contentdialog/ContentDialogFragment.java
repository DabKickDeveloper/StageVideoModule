package com.dabkick.videosdk.livesession.contentdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;


public class ContentDialogFragment extends DialogFragment {

    public static ContentDialogFragment newInstance() {
        ContentDialogFragment contentDialogFragment = new ContentDialogFragment();
        Bundle args = new Bundle();
        contentDialogFragment.setArguments(args);
        return contentDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.layout_content_dialog_fragment, container);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = parent.findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager(), getContext()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = parent.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return parent;
    }

}
