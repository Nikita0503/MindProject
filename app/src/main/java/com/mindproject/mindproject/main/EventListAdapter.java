package com.mindproject.mindproject.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.support.SupportActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 12.02.2019.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private SimpleDateFormat mDifferenceDateFormat;
    private SimpleDateFormat mEventDateFormat;
    private ArrayList<EventDataForEventList> mEvents;
    private MainActivity mActivity;

    public EventListAdapter(MainActivity activity) {
        mEventDateFormat = new SimpleDateFormat("d MMM H:mm", Locale.ENGLISH);
        mDifferenceDateFormat = new SimpleDateFormat("H:mm:ss", Locale.ENGLISH);
        mEvents = new ArrayList<EventDataForEventList>();
        mActivity = activity;
        Timer mTimer = new Timer();
        AdapterTimerTask mMyTimerTask = new AdapterTimerTask();
        mTimer.schedule(mMyTimerTask, 0, 500);
    }

    public void addEvents(ArrayList<EventDataForEventList> events){
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        return new EventListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewEventTitle.setText(mEvents.get(position).title);
        holder.imageViewEvent.setImageDrawable(mEvents.get(position).eventImage);
        holder.textViewActivationTime.setText(mEventDateFormat.format(mEvents.get(position).eventDate));
        holder.textViewTimer.setText(getTimeDifference(mEvents.get(position).eventDate));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity.getApplicationContext(), SupportActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventTitle;
        TextView textViewActivationTime;
        TextView textViewTimer;
        ImageView imageViewEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewEvent);
            textViewEventTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewActivationTime = (TextView) itemView.findViewById(R.id.textViewActivationTime);
            textViewTimer = (TextView) itemView.findViewById(R.id.textViewTimerToEvent);
        }
    }

    private String getTimeDifference(Date date){
        Date currentDate = Calendar.getInstance().getTime();
        long differenceLong = date.getTime() - currentDate.getTime();
        return mDifferenceDateFormat.format(new Date(differenceLong));
    }

    class AdapterTimerTask extends TimerTask {

        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }
}
