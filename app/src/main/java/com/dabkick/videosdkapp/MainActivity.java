package com.dabkick.videosdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.developerbutton.DabKickVideoButton;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
        categories.add("Apps");
        categories.add("Children");
        categories.add("Dawn");
        categories.add("Typical Workday");
        categories.add("Scenic beauty");

        //Adding values to the holder
        ArrayList<DabKickVideoInfo> list1 = new ArrayList<>();
        DabKickVideoInfo detail1 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail2 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail3 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail4 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail5 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail6 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail7 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail8 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail9 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail10 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list1.add(detail1);
        list1.add(detail2);
        list1.add(detail3);
        list1.add(detail4);
        list1.add(detail5);
        list1.add(detail6);
        list1.add(detail7);
        list1.add(detail8);
        list1.add(detail9);
        list1.add(detail10);
        videosHolder.put(categories.get(0), list1);

        ArrayList<DabKickVideoInfo> list2 = new ArrayList<>();
        DabKickVideoInfo detail11 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail21 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail31= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail41 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail51 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail61 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail71 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail81 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail91 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail101 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list2.add(detail11);
        list2.add(detail21);
        list2.add(detail31);
        list2.add(detail41);
        list2.add(detail51);
        list2.add(detail61);
        list2.add(detail71);
        list2.add(detail81);
        list2.add(detail91);
        list2.add(detail101);
        videosHolder.put(categories.get(1), list2);

        ArrayList<DabKickVideoInfo> list3 = new ArrayList<>();
        DabKickVideoInfo detail12 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail22 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail32= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail42 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail52 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail62 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail72 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail82 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail92 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail102 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list3.add(detail12);
        list3.add(detail22);
        list3.add(detail32);
        list3.add(detail42);
        list3.add(detail52);
        list3.add(detail62);
        list3.add(detail72);
        list3.add(detail82);
        list3.add(detail92);
        list3.add(detail102);
        videosHolder.put(categories.get(2), list3);

        ArrayList<DabKickVideoInfo> list4 = new ArrayList<>();
        DabKickVideoInfo detail13 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail23 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail33= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail43 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail53 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail63 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail73 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail83 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail93 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail103 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list4.add(detail13);
        list4.add(detail23);
        list4.add(detail33);
        list4.add(detail43);
        list4.add(detail53);
        list4.add(detail63);
        list4.add(detail73);
        list4.add(detail83);
        list4.add(detail93);
        list4.add(detail103);
        videosHolder.put(categories.get(3), list4);

        ArrayList<DabKickVideoInfo> list5 = new ArrayList<>();
        DabKickVideoInfo detail14 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail24 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail34= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail44 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail54 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail64 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail74 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail84 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail94 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail104 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list5.add(detail14);
        list5.add(detail24);
        list5.add(detail34);
        list5.add(detail44);
        list5.add(detail54);
        list5.add(detail64);
        list5.add(detail74);
        list5.add(detail84);
        list5.add(detail94);
        list5.add(detail104);
        videosHolder.put(categories.get(4), list5);

        ArrayList<DabKickVideoInfo> list6 = new ArrayList<>();
        DabKickVideoInfo detail15 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail25 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail35= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail45 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail55 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail65 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail75 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail85 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail95 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail105 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list6.add(detail15);
        list6.add(detail25);
        list6.add(detail35);
        list6.add(detail45);
        list6.add(detail55);
        list6.add(detail65);
        list6.add(detail75);
        list6.add(detail85);
        list6.add(detail95);
        list6.add(detail105);
        videosHolder.put(categories.get(5), list6);

        ArrayList<DabKickVideoInfo> list7 = new ArrayList<>();
        DabKickVideoInfo detail16 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail26 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail36= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail46 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail56 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail66 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail76 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail86 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail96 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail106 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list7.add(detail16);
        list7.add(detail26);
        list7.add(detail36);
        list7.add(detail46);
        list7.add(detail56);
        list7.add(detail66);
        list7.add(detail76);
        list7.add(detail86);
        list7.add(detail96);
        list7.add(detail106);
        videosHolder.put(categories.get(6), list7);

        ArrayList<DabKickVideoInfo> list8 = new ArrayList<>();
        DabKickVideoInfo detail17 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail27 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail37= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail47 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail57 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail67 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail77 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail87 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail97 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail107 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list8.add(detail17);
        list8.add(detail27);
        list8.add(detail37);
        list8.add(detail47);
        list8.add(detail57);
        list8.add(detail67);
        list8.add(detail77);
        list8.add(detail87);
        list8.add(detail97);
        list8.add(detail107);
        videosHolder.put(categories.get(7), list8);

        ArrayList<DabKickVideoInfo> list9 = new ArrayList<>();
        DabKickVideoInfo detail18 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail28 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail38= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail48 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail58 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail68 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail78 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail88 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail98 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail108 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list9.add(detail18);
        list9.add(detail28);
        list9.add(detail38);
        list9.add(detail48);
        list9.add(detail58);
        list9.add(detail68);
        list9.add(detail78);
        list9.add(detail88);
        list9.add(detail98);
        list9.add(detail108);
        videosHolder.put(categories.get(8), list9);

        ArrayList<DabKickVideoInfo> list10 = new ArrayList<>();
        DabKickVideoInfo detail19 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail29 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail39= new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail49 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail59 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        DabKickVideoInfo detail69 = new DabKickVideoInfo("Sylvester", "Dabkick", "40000","https://crunchbase-production-res.cloudinary.com/image/upload/c_lpad,h_256,w_256,f_jpg/v1495574401/kmxsxqyvflfz1fzrxixs.png",
                "http://dabkick.com/Assets/Promo%20Video.mp4");
        DabKickVideoInfo detail79 = new DabKickVideoInfo("Peter Griffin", "Wonder Girl", "40000","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX9449089.jpg",
                "http://www.ebookfrenzy.com/android_book/movie.mp4");
        DabKickVideoInfo detail89 = new DabKickVideoInfo("Angelica Pickels", "Gud Morning", "24000","http://www.goodmorning.quotesms.com/images/morning-quote/famous-morning-quotes.jpg",
                "http://s3.bravepeople.co/assets/media/process-loop.mp4");
        DabKickVideoInfo detail99 = new DabKickVideoInfo("Tasmanian", "Morning at its best!!!", "124000","https://wellfinger.com/wellImage/first2016May18-07-24-11.jpg",
                "http://s3.bravepeople.co/assets/media/video.mp4");
        DabKickVideoInfo detail109 = new DabKickVideoInfo("Patrick Gonzales ", "Nature", "41000","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQagAwHm1sT1obnrkqz_4amGl1q6z6TsEwMQOyMYSliro_I4hRB",
                "http://artsandculture.withgoogle.com/gcs/national-parks-service/en-us/9f885369-a52a-4a0b-8b2f-f3e5e41cdd54.mp4");
        list10.add(detail19);
        list10.add(detail29);
        list10.add(detail39);
        list10.add(detail49);
        list10.add(detail59);
        list10.add(detail69);
        list10.add(detail79);
        list10.add(detail89);
        list10.add(detail99);
        list10.add(detail109);
        videosHolder.put(categories.get(9), list10);

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

                int totalSize = videosHolder.get(category).size();
                int endIndex = Math.min(totalSize, offset + 3);
                ArrayList<DabKickVideoInfo> list = new ArrayList<>(videosHolder.get(category).subList(offset, endIndex));
                return list;

            }

            @Override
            public ArrayList<String> provideCategories(int offset) {
                if (offset == categories.size() - 1) {
                    // cannot provide any more categories
                    return new ArrayList<>();
                }
                ArrayList<String> categoryList = new ArrayList<>();
                categoryList.add(categories.get(offset));
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
