
package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventUser {

    @SerializedName("karma")
    @Expose
    public Integer karma;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("phone")
    @Expose
    public String phone;

}
