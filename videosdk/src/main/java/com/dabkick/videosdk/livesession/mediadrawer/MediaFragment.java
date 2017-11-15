package com.dabkick.videosdk.livesession.mediadrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

// In this case, the fragment displays simple text based on the page
public class MediaFragment extends Fragment {

    public static final String ARG_CATEGORY = "ARG_CATEGORY";
    private String category;

    @Inject MediaDatabase mediaDatabase;

    public static MediaFragment newInstance(final String category) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MediaFragment() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString(ARG_CATEGORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ListView listView = (ListView) inflater.inflate(R.layout.fragment_media, container, false);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mediaDatabase.getVideoList(category));
        listView.setAdapter(listAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount == totalItemCount) {
                    listAdapter.addAll(mediaDatabase.loadMoreVideos(category));
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = mediaDatabase.getVideoList(category).get(position);
            MediaItemClickEvent event = new MediaItemClickEvent(url);
            EventBus.getDefault().post(event);
        });


        return listView;
    }
}
