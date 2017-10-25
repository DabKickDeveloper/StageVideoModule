package com.dabkick.videosdk;


import android.os.Build;

import com.dabkick.videosdk.retrofit.RegisterRequestBody;
import com.dabkick.videosdk.retrofit.RegisterResponse;
import com.dabkick.videosdk.retrofit.RetrofitCreator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class Util {

    static void register() {

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
                .subscribe(new SingleObserver<RegisterResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull RegisterResponse registerResponse) {
                        Timber.d("registered anonymous user");
                        saveUserRegistrationInfo(registerResponse);
                        registerUserWithFirebase();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("unable to register anonymous user");
                        Timber.e(e);
                    }
                });
    }

    private static void saveUserRegistrationInfo(RegisterResponse resp) {
        Prefs.setAccessToken(resp.getAccessToken());
        Prefs.setRefreshToken(resp.getRefreshToken());
        Prefs.setFirebaseToken(resp.getFirebaseToken());
    }

    private static void registerUserWithFirebase() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCustomToken(Prefs.getFirebaseToken())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d(TAG, "signInWithCustomToken:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.e(TAG, "signInWithCustomToken:failure", task.getException());
                    }
                });

    }

    public static String getQueryServerUrl() {
        if (BuildConfig.DEBUG) {
            return SdkApp.getAppContext().getString(R.string.query_server_staging);
        } else {
            return SdkApp.getAppContext().getString(R.string.query_server_production);
        }
    }

}