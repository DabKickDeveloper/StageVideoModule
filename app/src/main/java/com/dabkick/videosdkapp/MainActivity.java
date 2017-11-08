package com.dabkick.videosdkapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.developerbutton.DabKickVideoButton;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listview) ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<VideoDetails> videoList = new ArrayList<>();

        VideoDetails detail1 = new VideoDetails("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        VideoDetails detail2 = new VideoDetails("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        VideoDetails detail3 = new VideoDetails("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        VideoDetails detail4 = new VideoDetails("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        VideoDetails detail5 = new VideoDetails("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");

        videoList.add(detail1);
        videoList.add(detail2);
        videoList.add(detail3);
        videoList.add(detail4);
        videoList.add(detail5);

        ListAdapter adapter = new ListAdapter(videoList,MainActivity.this);
        lv.setAdapter(adapter);


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

    public class VideoDetails{

        String author,title,duration, thumbnailUrl, Url;

        VideoDetails(String author, String title, String duration, String thumbnailUrl, String Url){

            this.author = author;
            this.title = title;
            this.duration = duration;
            this.thumbnailUrl = thumbnailUrl;
            this.Url = Url;
        }

    }

}
