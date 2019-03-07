package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 07.03.2019.
 */

public class Vote {

    public int request_id;

    public Vote(int request_id) {
        this.request_id = request_id;
    }
}
