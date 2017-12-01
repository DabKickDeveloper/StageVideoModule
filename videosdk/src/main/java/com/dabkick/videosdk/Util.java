package com.dabkick.videosdk;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.dabkick.videosdk.retrofit.RegisterRequestBody;
import com.dabkick.videosdk.retrofit.RegisterResponse;
import com.dabkick.videosdk.retrofit.RetrofitCreator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Util {



    public static void register(SingleObserver<RegisterResponse> observer) {

        // String devId, String devToken
        RegisterRequestBody body = new RegisterRequestBody(
                true,
                Integer.toString(BuildConfig.VERSION_CODE),
                "Android",
                Build.VERSION.RELEASE,
                "com.dabkick.videosdk",
                "667e332081744dad3f03c6628d12fe16f2cc8a161e5ee22890230e24ba85c54e", // TODO need to get from prefs
                "667e332081744dad3f03c6628d12fe16f2cc8a161e5ee22890230e24ba85c54e" // TODO need to get from prefs
        );

        RetrofitCreator.getUnauthenticatedApiInterface()
                .register(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public static void saveUserRegistrationInfo(RegisterResponse resp) {
        Prefs.setAccessToken(resp.getAccessToken());
        Prefs.setRefreshToken(resp.getRefreshToken());
        Prefs.setFirebaseToken(resp.getFirebaseToken());
        Prefs.setUserId(Integer.toString(resp.getUserDetails().getUserid()));
        Prefs.setDabname(resp.getUserDetails().getUserDabName());
        Prefs.setProfilePicUrl(resp.getUserDetails().getUserphoto());
    }

    public static void registerUserWithFirebase() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCustomToken(Prefs.getFirebaseToken())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Timber.d("registered user with Firebase");
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e("cannot register user with Firebase. %s", task.getException());
                    }
                });

    }

    public static String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static String getQueryServerUrl() {
        if (BuildConfig.DEBUG) {
            return SdkApp.getAppContext().getString(R.string.query_server_staging);
        } else {
            return SdkApp.getAppContext().getString(R.string.query_server_production);
        }
    }


    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }






}