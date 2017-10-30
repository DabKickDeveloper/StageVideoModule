package com.dabkick.videosdk.retrofit;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Defined endpoints for Retrofit
 */
public interface ApiInterface {

    // register an anonymous user with the DabKick server
    @POST("v200/register.php")
    Single<RegisterResponse> register(@Body RegisterRequestBody body);

    @POST("v200/token.php")
    @FormUrlEncoded
    Call<RefreshAccessTokenResponse> refreshAccessToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId);

    // retrieves a Twilio access token
    @GET("/iOS/getLiveStreamAccessTokenById.php")
    Single<TwilioAccessToken> getLivestreamAccessToken();

}