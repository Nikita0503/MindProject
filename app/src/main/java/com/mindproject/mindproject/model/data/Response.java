package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 15.03.2019.
 */

public class Response {
    @SerializedName("pagination")
    @Expose
    public Pagination pagination;
    @SerializedName("requests")
    @Expose
    public ArrayList<EventData> requests = null;
}
