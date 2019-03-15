package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 15.03.2019.
 */

public class Pagination {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("page")
    @Expose
    public Integer page;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;

}
