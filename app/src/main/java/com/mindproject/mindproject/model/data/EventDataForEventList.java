package com.mindproject.mindproject.model.data;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Nikita on 12.02.2019.
 */

public class EventDataForEventList {
    public String title;
    public Date eventDate;
    public Drawable eventImage;
    public EventData eventData;

    public EventDataForEventList(String description, Date eventDate, Drawable eventImage) {
        this.title = description;
        this.eventDate = eventDate;
        this.eventImage = eventImage;
    }

    public EventDataForEventList(String description, Date eventDate, Drawable eventImage, EventData eventData) {
        this.title = description;
        this.eventDate = eventDate;
        this.eventImage = eventImage;
        this.eventData = eventData;
    }
}
