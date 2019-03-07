package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MultipartBody;

/**
 * Created by Nikita on 06.03.2019.
 */

public class AddRequestData {

    @SerializedName("start_time")
    @Expose
    public String start_time;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("photo")
    @Expose
    public File photo;
    public AddRequestData(String start_time, String title, String description) {
        this.start_time = start_time;
        this.title = title;
        this.description = description;
        this.photo = photo;
    }
}
