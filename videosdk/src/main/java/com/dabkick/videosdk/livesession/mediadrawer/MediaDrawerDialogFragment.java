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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;


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
        View parent = inflater.inflate(R.layout.layout_content_dialog_fragment, container);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = parent.findViewById(R.id.viewpager);
        MediaDrawerPagerAdapter adapter = new MediaDrawerPagerAdapter(
                getChildFragmentManager(), getContext(), mediaDatabase.getCategoryList());


        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            // hacky solution to make sure we keep loading
            // ViewPager has a bug where it won't call onPageSelected until it has two or more items
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Timber.i("pos: %s", position);
                if (position == 0 || (position == mediaDatabase.getCategoryList().size() - 1)) {
                    Timber.i("inside");
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
        viewPager.post(() -> onPageChangeListener.onPageScrolled(viewPager.getCurrentItem(), 0, 0));

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = parent.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return parent;
    }

}
