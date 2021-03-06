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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MediaDrawerDialogFragment extends DialogFragment {

    @Inject MediaDatabase mediaDatabase;

    public static MediaDrawerDialogFragment newInstance() {
        MediaDrawerDialogFragment mediaDrawerDialogFragment = new MediaDrawerDialogFragment();
        Bundle args = new Bundle();
        mediaDrawerDialogFragment.setArguments(args);
        return mediaDrawerDialogFragment;
    }

    public MediaDrawerDialogFragment() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.layout_content_dialog_fragment, container,false);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = rootLayout.findViewById(R.id.viewpager);
        TabLayout tabLayout = rootLayout.findViewById(R.id.sliding_tabs);
        MediaDrawerPagerAdapter adapter = new MediaDrawerPagerAdapter(
                getChildFragmentManager(), mediaDatabase.getCategoryList());

        /*ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            // hacky solution to make sure we keep loading
            // ViewPager has a bug where it won't call onPageSelected until it has two or more items
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0 || (position == mediaDatabase.getCategoryList().size() - 1)) {
                    // reached end of categories - query more
                    mediaDatabase.loadMoreCategories(new Observer<List<String>>() {
                        @Override public void onSubscribe(Disposable d) {}

                        @Override
                        public void onNext(List<String> strings) {
                            adapter.notifyDataSetChanged();
                        }

                        @Override public void onError(Throwable e) {}
                        @Override public void onComplete() {}
                    });
                }
            }
        };

        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.post(() -> onPageChangeListener.onPageScrolled(viewPager.getCurrentItem(), 0, 0));*/

        mediaDatabase.loadMoreCategories(new Observer<List<String>>() {
            @Override public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(List<String> strings) {
                adapter.notifyDataSetChanged();
                tabLayout.getTabAt(0).select();
            }

            @Override public void onError(Throwable e) {}
            @Override public void onComplete() {}
        });

        viewPager.setAdapter(adapter);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int i =  tab.getPosition();
                if (i == 0 || (i == mediaDatabase.getCategoryList().size() - 1)) {
                    // reached end of categories - query more
                    mediaDatabase.loadMoreCategories(new Observer<List<String>>() {
                        @Override public void onSubscribe(Disposable d) {}

                        @Override
                        public void onNext(List<String> strings) {
                            adapter.notifyDataSetChanged();
                        }

                        @Override public void onError(Throwable e) {}
                        @Override public void onComplete() {}
                    });
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return rootLayout;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaItemClickEvent event) {
        //dismiss();
    };


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

}