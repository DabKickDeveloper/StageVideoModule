package com.dabkick.videosdk.livesession;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabkick.videosdk.livesession.chat.EmojiLayout;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDrawerDialogFragment;
import com.dabkick.videosdk.R;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.chat.ChatAdapter;
import com.dabkick.videosdk.livesession.chat.ChatModel;
import com.dabkick.videosdk.livesession.chat.ChatPresenter;
import com.dabkick.videosdk.livesession.chat.ChatView;
import com.dabkick.videosdk.livesession.chat.ClearFocusBackPressedEditText;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenter;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenterImpl;
import com.dabkick.videosdk.livesession.livestream.LivestreamView;
import com.dabkick.videosdk.livesession.livestream.SessionParticipantsAdapter;
import com.dabkick.videosdk.livesession.stage.StagePresenter;
import com.dabkick.videosdk.livesession.stage.StagePresenterImpl;
import com.dabkick.videosdk.livesession.stage.StageRecyclerViewAdapter;
import com.dabkick.videosdk.livesession.usersetup.GetUserDetailsFragment;
import com.like.CircleView;
import com.like.DotsView;
import com.twilio.video.VideoView;

import java.util.ArrayList;
import java.util.Random;

import timber.log.Timber;

public class LiveSessionActivity extends AppCompatActivity implements ChatView, LivestreamView {

    // Chat MVP
    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;

    // Session Participant MVP
    private SessionParticipantsAdapter sessionParticipantsAdapter;
    private LivestreamPresenter livestreamPresenter;
    private StagePresenter stagePresenter;

    // constants
    private final int PERMISSION_REQUEST_CODE = 3928;
    private final int DEFAULT_CHAT_MSG_LENGTH_LIMIT = 256;

    // Views
    private ImageView chatToggleButton;
    private ListView chatListView;
    private ClearFocusBackPressedEditText chatEditText;

    // Stage
    private StageRecyclerViewAdapter stageRecyclerViewAdapter;

    //Emoji Layout
    private View emojiLayout;
    RelativeLayout innerContainer;
    ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        if (savedInstanceState == null) {
            Util.register();
        }

        chatListView = findViewById(R.id.listview_livesession_chat);
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        chatListView.setAdapter(chatAdapter);

        chatPresenter = new ChatPresenter(this);

        chatToggleButton = findViewById(R.id.chat_toggle);
        chatToggleButton.setOnClickListener(v -> toggleChatUi());

        chatEditText = findViewById(R.id.message_edit_text);
        chatEditText.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // do not handle empty strings
                if (chatEditText.getText().toString().length() == 0) {
                    return true;
                }
                handled = true;
                clickSendButton(chatEditText.getText().toString());
                chatEditText.setText("");
            }

            return handled;
        });

        chatEditText.setOnFocusChangeListener((v, hasFocus) -> {
            // show keyboard if widget has focus
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                chatEditText.setWidth(0);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                param.setMargins(12,4,4,4);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.GONE);

                toggleChatUi();
            } else {

                int px = Math.round(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 180,getResources().getDisplayMetrics()));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        px,LinearLayout.LayoutParams.MATCH_PARENT);
                param.setMargins(12,4,4,4);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.VISIBLE);

                toggleChatUi();
            }
        });

        chatEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_CHAT_MSG_LENGTH_LIMIT)});

        // back button
        ImageView backBtn = findViewById(R.id.iv_leave_session_btn);
        backBtn.setOnClickListener(view -> finish());


        // setup livestream
        RecyclerView livestreamRecyclerView = findViewById(R.id.recyclerview_livestream);
        RecyclerView.LayoutManager livestreamLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        livestreamRecyclerView.setLayoutManager(livestreamLayoutManager);
        livestreamPresenter = new LivestreamPresenterImpl(this);
        sessionParticipantsAdapter = new SessionParticipantsAdapter(this, this,
                livestreamPresenter.getLivestreamParticipants());
        livestreamRecyclerView.setAdapter(sessionParticipantsAdapter);


        // setup stage
        RecyclerView stageRecyclerView = findViewById(R.id.recyclerview_stage);
        RecyclerView.LayoutManager stageLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        stageRecyclerView.setLayoutManager(stageLayoutManager);

        SnapHelper stageSnapHelper = new PagerSnapHelper();
        stageSnapHelper.attachToRecyclerView(stageRecyclerView);

        stageRecyclerViewAdapter = new StageRecyclerViewAdapter(this);
        stagePresenter = new StagePresenterImpl(stageRecyclerViewAdapter);
        stageRecyclerViewAdapter.setVideoControlListener(stagePresenter.getVideoControlsListener());
        stageRecyclerViewAdapter.setItems(stagePresenter.getStageItems());

        stageRecyclerView.setAdapter(stageRecyclerViewAdapter);


        innerContainer = findViewById(R.id.container_layout);
        container = findViewById(R.id.container);

        emojiLayout = findViewById(R.id.layout_emoji);

        findViewById(R.id.emoji_icon1).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.reactions_default));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.SMILE);
        });

        findViewById(R.id.emoji_icon2).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.cool));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.COOL);
        });

        findViewById(R.id.emoji_icon3).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.winky));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.WINK);
        });

        findViewById(R.id.emoji_icon4).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.love));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.LOVE);
        });

        findViewById(R.id.emoji_icon5).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.tongue));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.TONGUE);
        });

        findViewById(R.id.emoji_icon6).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.rofl));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.ROFL);
        });

        findViewById(R.id.emoji_icon7).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.crying));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.CRY);
        });

        findViewById(R.id.emoji_icon8).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.angry));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.ANGRY);
        });

        findViewById(R.id.emoji_icon9).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.x_eyes));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.XEYES);
        });

        findViewById(R.id.emoji_icon10).setOnClickListener(view -> {
            SlideToAbove(getResources().getDrawable(R.drawable.shocked));
            if(EmojiLayout.getEmojiClickCallbackListener() != null)
                EmojiLayout.getEmojiClickCallbackListener().emojiClicked(EmojiLayout.Emoji.SHOCKED);
        });

    }

    public void showContentDialog(View view) {
        MediaDrawerDialogFragment mediaDrawerDialogFragment = MediaDrawerDialogFragment.newInstance();
        mediaDrawerDialogFragment.show(getSupportFragmentManager(), "contentdialogfragment");
    }

    // toggle visibility of chat UI and swap button drawable
    private void toggleChatUi() {
        // toggle chat list and icon
        int visibility = chatListView.getVisibility();
        if (visibility == View.INVISIBLE) {
            chatListView.setVisibility(View.VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_accessible_white_36dp);
            chatToggleButton.setImageDrawable(drawable);
        } else {
            chatListView.setVisibility(View.INVISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_directions_boat_white_36dp);
            chatToggleButton.setImageDrawable(drawable);
        }
    }

    @Override
    public void clickSendButton(String message) {
        chatPresenter.sendMessage(message);
    }

    @Override
    public void addChatMessage(ChatModel chatModel) {
        chatAdapter.add(chatModel);
    }

    @Override
    public void myStreamClicked() {
        if (checkPermissionForCameraAndMicrophone()) {
            livestreamPresenter.toggleMyStream();
        } else {
            requestPermissionForCameraAndMicrophone();
        }
    }

    private boolean checkPermissionForCameraAndMicrophone(){
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    getString(R.string.permissions_needed),
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String PERMISSIONS[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {

                boolean cameraAndMicPermissionGranted = true;

                for (int grantResult : grantResults) {
                    cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
                }

                if (cameraAndMicPermissionGranted) {
                    // TODO enable my stream livestreamPresenter.toggleMyStream();
                } else {
                    Toast.makeText(this,
                            R.string.permissions_needed,
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            livestreamPresenter.onFinishing();
        }
    }

    @Override
    public void myVideoViewCreated(VideoView videoView) {
        livestreamPresenter.bindMyVideoView(videoView);
    }

    @Override
    public void otherUserStreamClicked(int index) {

    }

    @Override
    public void otherUserVideoViewCreated(VideoView videoView, int index) {

    }

    @Override
    public void addFriendClicked() {
        Timber.d("addFriendClicked");
        // waiting on Ronnie to finish server auth work before this can work fully
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appName = Util.getAppName(this);
        String text = "Let's watch videos together on " + appName + " by clicking link <link-from-server>";
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Room With");
        startActivity(chooserIntent);
    }

    @Override
    public void notifyDataSetChanged() {
        sessionParticipantsAdapter.notifyDataSetChanged();
    }

    private void showGetUserDetailsFragment() {

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        // remove any currently shown dialog
        android.app.Fragment prev = getFragmentManager().findFragmentByTag(GetUserDetailsFragment.class.getName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // show Fragment
        GetUserDetailsFragment newGetUserDetailsFragment = new GetUserDetailsFragment();
        newGetUserDetailsFragment.show(ft, GetUserDetailsFragment.class.getName());

    }

    public void SlideToAbove(Drawable emojiIcons) {

        Random r = new Random();
        int Low = 10;
        int High = 200;
        int rightValue = r.nextInt(High - Low) + Low;

        ImageView bounceimage = new ImageView(this);
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams((int) convertDpToPixel(this, 34),
                (int) convertDpToPixel(this, 34));

        vp.addRule(RelativeLayout.ABOVE, R.id.layout_chat);
        vp.bottomMargin = 250;
        vp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        vp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        vp.rightMargin = rightValue;
        bounceimage.setImageDrawable(emojiIcons);

        FrameLayout frameLayout = new FrameLayout(this);
        CircularImageView friendImageIcon = new CircularImageView(this);
        DotsView dotsView = new DotsView(this);
        CircleView circleView = new CircleView(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams((int) convertDpToPixel(this, 150),
                height);
        relativeLayoutParams.addRule(RelativeLayout.ABOVE, R.id.layout_chat);
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayoutParams.rightMargin = rightValue - 72;
        frameLayout.requestLayout();
        frameLayout.setLayoutParams(relativeLayoutParams);

        FrameLayout.LayoutParams dotsParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dotsParams.gravity = Gravity.CENTER;
        dotsParams.bottomMargin = -(height / 2 - 238 - 420);
        FrameLayout.LayoutParams circleParams = new FrameLayout.LayoutParams(90, 90);
        circleParams.gravity = Gravity.CENTER;
        circleParams.bottomMargin = -(height / 2 - 238 - 420);
        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(90, 90);
        iconParams.gravity = Gravity.CENTER;
        iconParams.bottomMargin = -(height / 2 - 238);
        FrameLayout.LayoutParams bounceEmojiParams = new FrameLayout.LayoutParams(90, 2500);
        bounceEmojiParams.gravity = Gravity.CENTER;
        bounceEmojiParams.bottomMargin = -(height / 2 - 238 -420);

        dotsView.requestLayout();
        dotsView.setLayoutParams(dotsParams);
        circleView.requestLayout();
        circleView.setLayoutParams(circleParams);
        friendImageIcon.requestLayout();
        friendImageIcon.setLayoutParams(iconParams);
        bounceimage.requestFocus();
        bounceimage.setLayoutParams(bounceEmojiParams);
        frameLayout.addView(bounceimage);
        frameLayout.addView(friendImageIcon);
        frameLayout.addView(circleView);
        frameLayout.addView(dotsView);
        innerContainer.requestLayout();
        innerContainer.bringToFront();
        innerContainer.addView(frameLayout);
        circleView.setStartColor(getResources().getColor(R.color.pink));
        circleView.setEndColor(getResources().getColor(R.color.white));
        dotsView.setColors(getResources().getColor(R.color.white), getResources().getColor(R.color.pink));
        //End


        friendImageIcon.setImageDrawable(getResources().getDrawable(R.drawable.generic_avatar_cat_v70));
        friendImageIcon.setVisibility(View.VISIBLE);
        bounceimage.setVisibility(View.GONE);

        ObjectAnimator objectanimator = ObjectAnimator.ofFloat(friendImageIcon, "translationY", -420);
        objectanimator.setDuration(1500);
        objectanimator.start();
        objectanimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                friendImageIcon.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                perFormCustomAnimation(bounceimage, dotsView, circleView, friendImageIcon);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void perFormCustomAnimation(ImageView icon, DotsView dotsView, CircleView circleView, CircularImageView friendImage) {
        int indexForDots = container.indexOfChild(icon);
        for (int i = 0; i < indexForDots; i++) {
            container.bringChildToFront(container.getChildAt(i));
        }
        int indexForCircle = container.indexOfChild(icon);
        for (int i = 0; i < indexForCircle; i++) {
            container.bringChildToFront(container.getChildAt(i));
        }
        int indexForIcon = container.indexOfChild(icon);
        for (int i = 0; i < indexForIcon; i++) {
            container.bringChildToFront(container.getChildAt(i));
        }
        DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
        AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
        OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4.0F);
        icon.setScaleX(0.0F);
        icon.setScaleY(0.0F);
        circleView.setInnerCircleRadiusProgress(0.0F);
        circleView.setOuterCircleRadiusProgress(0.0F);
        dotsView.setCurrentProgress(0.0F);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(circleView, circleView.OUTER_CIRCLE_RADIUS_PROGRESS, new float[]{0.1F, 1.0F});
        outerCircleAnimator.setDuration(250L);
        outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(circleView, circleView.INNER_CIRCLE_RADIUS_PROGRESS, new float[]{0.1F, 1.0F});
        innerCircleAnimator.setDuration(200L);
        innerCircleAnimator.setStartDelay(200L);
        innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator starScaleYAnimator = ObjectAnimator.ofFloat(icon, ImageView.SCALE_Y, new float[]{0.2F, 1.0F});
        starScaleYAnimator.setDuration(350L);
        starScaleYAnimator.setStartDelay(250L);
        starScaleYAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);
        ObjectAnimator starScaleXAnimator = ObjectAnimator.ofFloat(icon, ImageView.SCALE_X, new float[]{0.2F, 1.0F});
        starScaleXAnimator.setDuration(350L);
        starScaleXAnimator.setStartDelay(250L);
        starScaleXAnimator.setInterpolator(OVERSHOOT_INTERPOLATOR);
        ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(dotsView, DotsView.DOTS_PROGRESS, new float[]{0.0F, 1.0F});
        dotsAnimator.setDuration(900L);
        dotsAnimator.setStartDelay(50L);
        dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
        animatorSet.playTogether(new Animator[]{outerCircleAnimator, innerCircleAnimator, starScaleYAnimator, starScaleXAnimator, dotsAnimator});
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                icon.setVisibility(View.VISIBLE);
                icon.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                circleView.setInnerCircleRadiusProgress(0.0F);
                circleView.setOuterCircleRadiusProgress(0.0F);
                dotsView.setCurrentProgress(0.0F);
                icon.setScaleX(1.0F);
                icon.setScaleY(1.0F);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;

                ObjectAnimator objectanimator = ObjectAnimator.ofFloat(icon, "translationY", -(height/2 + 200));
                objectanimator.setDuration(7000);
                objectanimator.start();
                objectanimator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        icon.setAlpha(0.0f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeOut(friendImage);
            }
        }, 6000);

    }


    static public float convertDpToPixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;

        return pixel;
    }

    void fadeOut(CircularImageView friendImage){

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(friendImage, "alpha",  1f, 0f);
        fadeOut.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                friendImage.setAlpha(0.0f);
            }
        });
        mAnimationSet.start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        stagePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stagePresenter.onStop();
    }
}