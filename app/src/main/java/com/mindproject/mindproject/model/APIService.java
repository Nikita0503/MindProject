package com.mindproject.mindproject.model;

import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Nikita on 18.02.2019.
 */

public interface APIService {
    @POST("user")
    Single<AccessToken> getToken(@Body UserDataAuthorization dataAuthorization);

    @GET("request")
    Single<ArrayList<EventData>> getEvents(@Header("Authorization") String header);
}
