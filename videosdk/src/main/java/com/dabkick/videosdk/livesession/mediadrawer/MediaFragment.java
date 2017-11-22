package com.dabkick.videosdk.livesession.mediadrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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

        GridView listView = (GridView) inflater.inflate(R.layout.fragment_media, container, false);

       /* ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, mediaDatabase.getVideoList(category));*/

       List<DabKickVideoInfo> videoInfoList = mediaDatabase.getVideoList(category);
       MediaFragmentThumbnailsAdapter listAdapter = new MediaFragmentThumbnailsAdapter(videoInfoList, getContext());
        listView.setAdapter(listAdapter);

        videoInfoList.addAll(mediaDatabase.loadMoreVideos(category));
        listAdapter.notifyDataSetChanged();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(isListviewReachBottom(listView)){

                    //listAdapter.addAll(mediaDatabase.loadMoreVideos(category));
                    videoInfoList.addAll(mediaDatabase.loadMoreVideos(category));
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*if (visibleItemCount == totalItemCount) {
                    listAdapter.addAll(mediaDatabase.loadMoreVideos(category));
                    listAdapter.notifyDataSetChanged();
                }*/
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = mediaDatabase.getVideoList(category).get(position).getVideoUrl();
            MediaItemClickEvent event = new MediaItemClickEvent(url);
            EventBus.getDefault().post(event);
        });


        return listView;
    }

    private boolean isListviewReachBottom(AbsListView listview) {
        return listview.getLastVisiblePosition() == listview.getAdapter().getCount() - 1 && listview.getChildCount() != 0 &&
                listview.getChildAt(listview.getChildCount() - 1).getBottom() <= listview.getHeight();
    }
}
