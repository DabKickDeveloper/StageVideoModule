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

        //Holder to bind categories and videos
        final Map<String,ArrayList<DabKickVideoInfo>> videosHolder = new LinkedHashMap<>();

        //ArrayList to hold categories
        final ArrayList<String> categories = new ArrayList<>();
        categories.add("Comminucation");
        categories.add("Kid");
        categories.add("Mornings");
        categories.add("Morning Routine");
        categories.add("Nature");

        //Adding values to the holder
        ArrayList<DabKickVideoInfo> list1 = new ArrayList<>();
        DabKickVideoInfo detail1 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        list1.add(detail1);
        videosHolder.put(categories.get(0), list1);

        ArrayList<DabKickVideoInfo> list2 = new ArrayList<>();
        DabKickVideoInfo detail2 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        list2.add(detail2);
        videosHolder.put(categories.get(1), list2);

        ArrayList<DabKickVideoInfo> list3 = new ArrayList<>();
        DabKickVideoInfo detail3 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        list3.add(detail3);
        videosHolder.put(categories.get(2), list3);

        ArrayList<DabKickVideoInfo> list4 = new ArrayList<>();
        DabKickVideoInfo detail4 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        list4.add(detail4);
        videosHolder.put(categories.get(3), list4);

        ArrayList<DabKickVideoInfo> list5 = new ArrayList<>();
        DabKickVideoInfo detail5 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list5.add(detail5);
        videosHolder.put(categories.get(4), list5);

        //All the video objects in a single list for display
        final ArrayList<DabKickVideoInfo> videoList = new ArrayList<>();

        for(int count = 0; count < videosHolder.size(); count++)
            videoList.addAll(videosHolder.get(categories.get(count)));

        //Set adapter for the list
        ListAdapter adapter = new ListAdapter(videoList,MainActivity.this);
        lv.setAdapter(adapter);


        DabKickSession.DabKickVideoProvider dabKickVideoProvider = new DabKickSession.DabKickVideoProvider() {
            @Override
            public ArrayList<DabKickVideoInfo> provideVideos(String category, int offset) {

                //Get the video list from the holder
                ArrayList<DabKickVideoInfo> list = (videosHolder.get(category));

                //Create a new list to return required value
                ArrayList<DabKickVideoInfo> categoryVideoList = new ArrayList<>();

                //If offset is equal to list size
                //return the entire list
                if(offset == list.size() - 1)
                    return list;

                //If the size of the list from the offset is > 5
                //Return only 5 videos
                if((list.size() - 1 - offset) > 5){

                    for(int i = offset; i < 5; i++)
                        categoryVideoList.add(list.get(i));
                }else{ //If the size of the list from the offset <=5, then copy the entire list from the offset

                    for(int i = offset; i < list.size() ; i++)
                        categoryVideoList.add(list.get(i));

                }

                return categoryVideoList;
            }

            @Override
            public ArrayList<String> provideCategories(int offset) {

                //Create new list to return required value
                ArrayList<String> categoryList = new ArrayList<>();

                //If offset is equal to list size
                //return the entire list
                if(offset == categories.size() - 1)
                    return categories;


                //If the size of the list from the offset is > 5
                //Return only 5 categories
                if((categories.size() - 1 - offset) > 5){

                    for(int i = offset; i < 5; i++)
                        categoryList.add(categories.get(i));
                }else{//If the size of the list from the offset <=5, then opy the entire list from the ofset

                    for(int i = offset; i < categories.size() ; i++)
                        categoryList.add(categories.get(i));

                }

                return categoryList;
            }

            @Override
            public ArrayList<DabKickVideoInfo> startDabKickWithVideos() {
                //Return the list containing all the videos
                return videoList;
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
