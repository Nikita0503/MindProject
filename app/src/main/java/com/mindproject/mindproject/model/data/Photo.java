
package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("id")
    @Expose
    public Integer id;

}
