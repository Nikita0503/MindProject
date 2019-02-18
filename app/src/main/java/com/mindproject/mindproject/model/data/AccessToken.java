package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 18.02.2019.
 */

public class AccessToken {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
}
