package com.dabkick.videosdk.livesession.mediadrawer;


import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.SdkApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// contains video categories and corresponding videos
public class MediaDatabase {

    private DabKickSession.DabKickVideoProvider provider;

    private int categoryIndex = 0;
    private List<String> categoryList;

    public MediaDatabase() {
        provider = ((SdkApp) SdkApp.getAppContext()).getDabKickSession().getDabKickVideoProvider();
        categoryList = new ArrayList<>();
        categoryList.addAll(Arrays.asList("1", "2"));
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
                    return list;
                })
                .subscribe(e);

    }


}