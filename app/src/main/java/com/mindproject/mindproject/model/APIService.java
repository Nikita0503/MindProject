package com.mindproject.mindproject.model;

import android.provider.SyncStateContract;

import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.ChangeEmail;
import com.mindproject.mindproject.model.data.ChangeUsernameAndPhone;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.Response;
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
import retrofit2.http.Query;

/**
 * Created by Nikita on 18.02.2019.
 */

public interface APIService {
    @POST("user")
    Single<UserData> getToken(@Body UserDataAuthorization dataAuthorization);

    @GET("request")
    Single<Response> getEvents(@Header("Authorization") String header, @Query("start_time") String start_time);

    @GET("request")
    Single<Response> getMyEvents(@Header("Authorization") String header, @Query("end_time") String end_time, @Query("user_id") String user_id, @Query("page") int page, @Query("per_page") int per_page);

    @GET("request")
    Single<Response> getEventsForDate(@Header("Authorization") String header, @Query("start_time") String start_time, @Query("end_time") String end_time);

    @Multipart
    @POST("request")
    Completable sendRequestData(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time);

    @Multipart
    @POST("request")
    Completable sendRequestDataWith1Photo(@Header("Authorization") String header, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("start_time") RequestBody start_time, @Part ArrayList<MultipartBody.Part> photos);

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
