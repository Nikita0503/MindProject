package com.mindproject.mindproject.model;

import android.provider.SyncStateContract;

import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;
import com.mindproject.mindproject.model.data.Vote;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
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
import retrofit2.http.Part;

/**
 * Created by Nikita on 18.02.2019.
 */

public interface APIService {
    @POST("user")
    Single<AccessToken> getToken(@Body UserDataAuthorization dataAuthorization);

    @GET("request")
    Single<ArrayList<EventData>> getEvents(@Header("Authorization") String header);

    @FormUrlEncoded
    @POST("request")
    Completable sendRequestData(@Header("Authorization") String header, @Field("title") String title, @Field("description") String description, @Field("start_time") String start_time);

    @POST("vote")
    Completable voteForEvent(@Header("Authorization") String header, @Body Vote id);
}
