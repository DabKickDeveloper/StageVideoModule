package com.dabkick.videosdk.livesession.mediadrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dabkick.videosdk.R;

import java.util.ArrayList;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class MediaFragment extends Fragment {
    public static final String ARG_CATEGORY = "ARG_CATEGORY";

    private String category;
    private List<String> items;

    public static MediaFragment newInstance(final String category) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString(ARG_CATEGORY);
        items = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.fragment_media, container, false);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);


        return listView;
    }
}
