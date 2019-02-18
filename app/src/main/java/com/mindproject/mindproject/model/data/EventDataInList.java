package com.mindproject.mindproject.model.data;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Nikita on 12.02.2019.
 */

public class EventDataInList {
    public String description;
    public Date eventDate;
    public Drawable eventImage;

    public EventDataInList(String description, Date eventDate, Drawable eventImage) {
        this.description = description;
        this.eventDate = eventDate;
        this.eventImage = eventImage;
    }
}
