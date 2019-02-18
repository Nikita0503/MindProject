
package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("avatar")
    @Expose
    public Object avatar;
    @SerializedName("carma")
    @Expose
    public Integer carma;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("access_token")
    @Expose
    public String accessToken;

}
