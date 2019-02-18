package com.mindproject.mindproject.model;

import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Nikita on 18.02.2019.
 */

public interface APIService {
    @POST("user")
    Single<AccessToken> getUserData(@Body UserDataAuthorization dataAuthorization);
}
