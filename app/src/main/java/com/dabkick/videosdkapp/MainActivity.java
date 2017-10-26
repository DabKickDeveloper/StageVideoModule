package com.dabkick.videosdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.dabkick.videosdk.DabKick;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listview) ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String[] urls = {
                "https://www.youtube.com/watch?v=njSyHmcEdkw",
                "https://www.youtube.com/watch?v=FavUpD_IjVY",
                "https://www.youtube.com/watch?v=n-5F_7DwPpo"};

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, urls);
        lv.setAdapter(listAdapter);

        DabKick.initSdk("4b3403665fea6");



    }
}
