package com.dabkick.videosdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.developerbutton.DabKickVideoButton;

import java.util.ArrayList;

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


        DabKickSession.DabKickVideoProvider dabKickVideoProvider = new DabKickSession.DabKickVideoProvider() {
            @Override
            public ArrayList<DabKickVideoInfo> provideVideos(String category, int offset) {
                return null; // Shwetha TODO
            }

            @Override
            public ArrayList<String> provideCategories(int offset) {
                return null; // Shwetha TODO
            }

            @Override
            public ArrayList<DabKickVideoInfo> startDabKickWithVideos() {
                return null; // Shwetha TODO
            }
        };

        DabKickSession.DabKickBuilder dabKickBuilder = new DabKickSession.DabKickBuilder(
                "4b3403665fea6",
                dabKickVideoProvider
        );

        DabKickSession dabKickSession = dabKickBuilder.build();
        DabKickVideoButton dabKickVideoButton = findViewById(R.id.dabkick_video_button);
        dabKickVideoButton.setDabKickSession(dabKickSession);



    }
}
