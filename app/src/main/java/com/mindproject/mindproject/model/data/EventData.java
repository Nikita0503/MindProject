
package com.mindproject.mindproject.model.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("photos")
    @Expose
    public List<Photo> photos = null;
    @SerializedName("support_duration")
    @Expose
    public Integer supportDuration;
    @SerializedName("voted_count")
    @Expose
    public Integer votedCount;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("start_time")
    @Expose
    public String startTime;

}
