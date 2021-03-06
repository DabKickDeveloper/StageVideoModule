package com.dabkick.videosdk.livesession.emoji;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.livesession.CircularImageView;
import com.like.CircleView;
import com.like.DotsView;

import java.util.Random;

public class AnimationUtils {

    public static void slideToAbove(Drawable emojiIcons, RelativeLayout innerContainer, ConstraintLayout container, Context mActivity, RelativeLayout chatLayout) {

        Random r = new Random();
        int Low = 10;
        int High = 100;
        int rightValue = r.nextInt(High - Low) + Low;

        ImageView bounceimage = new ImageView(mActivity);
        RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams((int) convertDpToPixel(mActivity, 34),
                (int) convertDpToPixel(mActivity, 34));

        vp.addRule(RelativeLayout.ABOVE, R.id.layout_chat);
        vp.bottomMargin = 250;
        vp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        vp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        bounceimage.setImageDrawable(emojiIcons);

        FrameLayout frameLayout = new FrameLayout(mActivity);
        CircularImageView friendImageIcon = new CircularImageView(mActivity);
        DotsView dotsView = new DotsView(mActivity);
        CircleView circleView = new CircleView(mActivity);

        circleView.requestLayout();
        circleView.setY(chatLayout.getY() - convertDpToPixel(mActivity,30));
        friendImageIcon.requestLayout();
        friendImageIcon.setY(chatLayout.getY() - convertDpToPixel(mActivity,30));
        bounceimage.requestFocus();
        bounceimage.setY(chatLayout.getY() - convertDpToPixel(mActivity,30));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)mActivity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams((int) convertDpToPixel(mActivity, 150),
                height);
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayoutParams.rightMargin = rightValue - 100;
        frameLayout.requestLayout();
        frameLayout.setLayoutParams(relativeLayoutParams);

        FrameLayout.LayoutParams dotsParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dotsParams.gravity = Gravity.CENTER_HORIZONTAL;
        FrameLayout.LayoutParams circleParams = new FrameLayout.LayoutParams(90, 90);
        circleParams.gravity = Gravity.CENTER_HORIZONTAL;
        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(90, 90);
        iconParams.gravity = Gravity.CENTER_HORIZONTAL;
        FrameLayout.LayoutParams bounceEmojiParams = new FrameLayout.LayoutParams(90, 90);
        bounceEmojiParams.gravity = Gravity.CENTER_HORIZONTAL;

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
        circleView.setStartColor(mActivity.getResources().getColor(R.color.pink));
        circleView.setEndColor(mActivity.getResources().getColor(R.color.white));
        dotsView.setColors(mActivity.getResources().getColor(R.color.white), mActivity.getResources().getColor(R.color.pink));
        //End


        friendImageIcon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.generic_avatar_cat_v70));
        friendImageIcon.setVisibility(View.VISIBLE);
        bounceimage.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                performCustomAnimation(frameLayout,bounceimage, dotsView, circleView, friendImageIcon, container, mActivity);
            }
        }, 500);

    }

    private static void performCustomAnimation(FrameLayout frameLayout,ImageView icon, DotsView dotsView, CircleView circleView, CircularImageView friendImage, ConstraintLayout container, Context mActivity) {
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
                ((Activity)mActivity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;

                ObjectAnimator objectanimator = ObjectAnimator.ofFloat(icon, "translationY", -(height/2 + 200));
                objectanimator.setDuration(2000);
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
                fadeOut(frameLayout, friendImage);
            }
        }, 1000);

    }


    public static float convertDpToPixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;

        return pixel;
    }

    private static void fadeOut(FrameLayout frameLayout,CircularImageView friendImage){

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(friendImage, "alpha",  1f, 0f);
        fadeOut.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                friendImage.setAlpha(0.0f);
                frameLayout.removeAllViewsInLayout();
                ((ViewManager)frameLayout.getParent()).removeView(frameLayout);
            }
        });
        mAnimationSet.start();
    }

    public static void leftToRight(FrameLayout frameLayout, Activity mActivity, ImageView imageView){

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator frameAnimation = ObjectAnimator.ofFloat(frameLayout,
                "translationX", -convertDpToPixel(mActivity,200), 0);
        frameAnimation.setDuration(700);
        frameAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                    frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator imageAnimation = ObjectAnimator.ofFloat(imageView,
                "translationX", 0, convertDpToPixel(mActivity,200));
        imageAnimation.setDuration(700);
        set.playTogether(new Animator[]{frameAnimation,imageAnimation});
        set.start();

    }

    public static void rightToLeft(View frameLayout, Activity mActivity, ImageView imageView){

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator frameAnimation = ObjectAnimator.ofFloat(frameLayout,
                "translationX", 0, -convertDpToPixel(mActivity,200));
        frameAnimation.setDuration(700);
        frameAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                frameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator imageAnimation = ObjectAnimator.ofFloat(imageView,
                "translationX", convertDpToPixel(mActivity,200), 0);
        imageAnimation.setDuration(700);
        set.playTogether(new Animator[]{frameAnimation,imageAnimation});
        set.start();


    }


}
