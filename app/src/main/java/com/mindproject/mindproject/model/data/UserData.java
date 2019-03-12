
package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("avatar")
    @Expose
    public Object avatar;
    @SerializedName("karma")
    @Expose
    public Integer karma;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("access_token")
    @Expose
    public String accessToken;

}
