package com.mindproject.mindproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindproject.mindproject.data.EventData;

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
    private ArrayList<EventData> mEvents;
    private MainActivity mActivity;

    public EventListAdapter(MainActivity activity) {
        mEventDateFormat = new SimpleDateFormat("d MMM H:mm", Locale.ENGLISH);
        mDifferenceDateFormat = new SimpleDateFormat("H:mm:ss", Locale.ENGLISH);
        mEvents = new ArrayList<EventData>();
        mActivity = activity;
        Timer mTimer = new Timer();
        AdapterTimerTask mMyTimerTask = new AdapterTimerTask();
        mTimer.schedule(mMyTimerTask, 0, 500);
    }

    public void addEvents(ArrayList<EventData> events){
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
        holder.textViewEventDescription.setText(mEvents.get(position).description);
        holder.imageViewEvent.setImageDrawable(mEvents.get(position).eventImage);
        holder.textViewActivationTime.setText(mEventDateFormat.format(mEvents.get(position).eventDate));
        holder.textViewTimer.setText(getTimeDifference(mEvents.get(position).eventDate));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventDescription;
        TextView textViewActivationTime;
        TextView textViewTimer;
        ImageView imageViewEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewEvent);
            textViewEventDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
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
