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

    @FormUrlEncoded
    @POST("request")
    Completable sendRequestData(@Header("Authorization") String header, @Field("title") String title, @Field("description") String description, @Field("start_time") String start_time);

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
