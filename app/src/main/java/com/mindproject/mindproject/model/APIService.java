package com.mindproject.mindproject.model;

import android.provider.SyncStateContract;

import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.ChangeEmail;
import com.mindproject.mindproject.model.data.ChangeUsernameAndPhone;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;
import com.mindproject.mindproject.model.data.Vote;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Nikita on 18.02.2019.
 */

public interface APIService {
    @POST("user")
    Single<UserData> getToken(@Body UserDataAuthorization dataAuthorization);

    @GET("request")
    Single<ArrayList<EventData>> getEvents(@Header("Authorization") String header);

    @Multipart
    @POST("request")
    Completable sendRequestData(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith1Photo(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part MultipartBody.Part photo);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith2Photos(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part MultipartBody.Part photo, @Part MultipartBody.Part photo2);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith3Photos(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part MultipartBody.Part photo, @Part MultipartBody.Part photo2, @Part MultipartBody.Part photo3);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith4Photos(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part MultipartBody.Part photo, @Part MultipartBody.Part photo2, @Part MultipartBody.Part photo3, @Part MultipartBody.Part photo4);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith5Photos(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part MultipartBody.Part photo, @Part MultipartBody.Part photo2, @Part MultipartBody.Part photo3, @Part MultipartBody.Part photo4, @Part MultipartBody.Part photo5);


    @POST("vote")
    Completable voteForEvent(@Header("Authorization") String header, @Body Vote id);

    @PUT("user/me")
    Completable changeUsernameAndPhone(@Header("Authorization") String header, @Body ChangeUsernameAndPhone data);

    @Multipart
    @POST("user/me/avatar")
    Completable changeAvatar(@Header("Authorization") String header, @Part MultipartBody.Part data);

    @POST("email")
    Completable changeEmail(@Header("Authorization") String header, @Body ChangeEmail email);
}
