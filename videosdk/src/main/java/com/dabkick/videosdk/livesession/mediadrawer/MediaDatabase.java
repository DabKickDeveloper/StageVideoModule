package com.dabkick.videosdk.livesession.mediadrawer;


import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.SdkApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// contains video categories and corresponding videos
public class MediaDatabase {

    private DabKickSession.DabKickVideoProvider provider;

    // category fields
    private int categoryIndex = 0;
    private List<String> categoryList;
    // category-specific video-list fields
    private Map<String, List<DabKickVideoInfo>> videoMap;
    private Map<String, Integer> videoIndexMap;

    public MediaDatabase() {
        provider = ((SdkApp) SdkApp.getAppContext()).getDabKickSession().getDabKickVideoProvider();

        categoryList = new ArrayList<>();
        categoryList.addAll(Arrays.asList("1", "2"));

        videoMap = new HashMap<>();
        videoIndexMap = new HashMap<>();

    }

    List<String> getCategoryList() { return categoryList; }

    void loadMoreCategories(Observer<List<String>> e) {

        Observable
                .just(provider.provideCategories(categoryIndex))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(strings -> strings != null)
                .map(list -> {
                    categoryList.addAll(list);
                    categoryIndex += list.size();

                    // add categories to map
                    for (String category : categoryList) {
                        if (videoMap.get(category) == null) {
                            videoMap.put(category, new ArrayList<>());
                        }
                    }

                    return list;
                })
                .subscribe(e);

    }


    List<String> getVideoList(String category) {
        List<DabKickVideoInfo> videoList = videoMap.get(category);
        if (videoList == null) videoList = new ArrayList<>();
        return getDabKickVideoUrlsFromList(videoList);
    }

    ArrayList<String> loadMoreVideos(String category) {

        if (videoIndexMap.get(category) == null) {
            videoIndexMap.put(category, 0);
        }

        // TODO convert to Observable
        int index = videoIndexMap.get(category);
        List<DabKickVideoInfo> videos = provider.provideVideos(category, index);

        videoMap.get(category).addAll(videos);
        videoIndexMap.put(category, index + videos.size());

        return (ArrayList<String>) getDabKickVideoUrlsFromList(videoMap.get(category));
    }

    private List<String> getDabKickVideoUrlsFromList(List<DabKickVideoInfo> videoList) {
        List<String> result = new ArrayList<>();
        for (DabKickVideoInfo video : videoList) {
            result.add(video.getVideoUrl());
        }
        return result;
    }
}