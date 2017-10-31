package com.dabkick.videosdk.retrofit;


import com.dabkick.videosdk.BuildConfig;
import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.Util;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import timber.log.Timber;

public class RetrofitCreator {

    private static ApiInterface authenticatedApiInterface;
    private static ApiInterface unauthenticatedApiInterface;
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    private static Retrofit.Builder unauthenticatedBuilder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static <S> S createService(Class<S> serviceClass, boolean authenticated) {

        // add converters/adapters
        GsonBuilder gsonBuilder = new GsonBuilder();

        if (authenticated) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            builder.addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(gsonBuilder.create()));
        } else {
            unauthenticatedBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            unauthenticatedBuilder.addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(gsonBuilder.create()));
        }

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // set authentication
        if (authenticated) {
            // access token
            httpClientBuilder.addInterceptor(chain -> {
                String accessToken = Prefs.getAccessToken();
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-type", "application/json")
                        .header("Authorization", "Bearer " + accessToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            // refresh token
            okhttp3.Authenticator authenticator = (route, response) -> {
                Timber.d("authenticate() response: %s, challenges: %s", response.toString(), response.challenges());

                if (responseCount(response) >= 2) {
                    // both original response and refresh token failed, so probably won't authenticate
                    return null;
                }

                String refreshToken = Prefs.getRefreshToken();

                // Create a new client for our new access token
                ApiInterface tokenService = getUnauthenticatedApiInterface();
                Call<RefreshAccessTokenResponse> call = tokenService.refreshAccessToken(
                        "refresh_token",
                        refreshToken,
                        "DabKick");

                retrofit2.Response<RefreshAccessTokenResponse> tokenResponse = call.execute();
                if (tokenResponse.code() == 200) {
                    Timber.d("successfully refreshed access token");
                    RefreshAccessTokenResponse newAccessToken = tokenResponse.body();
                    Prefs.setAccessToken(newAccessToken.getAccessToken());
                    return response.request().newBuilder()
                            .header("Authorization", "Bearer " + newAccessToken.getAccessToken())
                            .build();
                } else {
                    Timber.e("unable to refresh token: %s", tokenResponse.code());
                    return null;
                }

            };
            httpClientBuilder.authenticator(authenticator);
        }


        if (BuildConfig.DEBUG) {
            Timber.d("Turn on HTTP logging");
            // anyone can see auth/refresh tokens here
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }

        OkHttpClient okHttpClient = httpClientBuilder.build();

        Retrofit retrofit;
        if(authenticated) {
            builder.client(okHttpClient);
            retrofit = builder.build();
        } else {
            unauthenticatedBuilder.client(okHttpClient);
            retrofit = unauthenticatedBuilder.build();
        }
        return retrofit.create(serviceClass);
    }

    private static int responseCount(okhttp3.Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    /**
     * Client which provides no authentication
     * For use with login and signup
     */
    public static ApiInterface getUnauthenticatedApiInterface() {
        unauthenticatedBuilder.baseUrl(Util.getQueryServerUrl());
        if (unauthenticatedApiInterface == null)
            unauthenticatedApiInterface = RetrofitCreator.createService(ApiInterface.class, false);
        return unauthenticatedApiInterface;
    }

    // Standard RetroFit client. For use with all authenticated requests.
    public static ApiInterface getAuthenticatedApiInterface() {
        builder.baseUrl(Util.getQueryServerUrl());
        if (authenticatedApiInterface == null)
            authenticatedApiInterface = RetrofitCreator.createService(ApiInterface.class, true);
        return authenticatedApiInterface;
    }

}