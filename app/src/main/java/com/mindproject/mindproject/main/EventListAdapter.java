package com.mindproject.mindproject.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.support.SupportFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikita on 12.02.2019.
 */

public class EventListAdapter extends RecyclerView.Adapter {

    private static final int EVENT_ROW_TYPE = 0;
    private static final int ADVERTISEMENT_ROW_TYPE = 1;
    private String mToken;
    private SimpleDateFormat mDifferenceDateFormat;
    private SimpleDateFormat mEventDateFormat;
    private ArrayList<EventDataForEventList> mEvents;
    private ListFragment mFragment;
    private AdRequest mAdRequest;

    public EventListAdapter(ListFragment fragment, String token) {
        mAdRequest = new AdRequest.Builder().build();
        mEventDateFormat = new SimpleDateFormat("d MMM H:mm", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getDefault();
        mDifferenceDateFormat = new SimpleDateFormat("H:mm:ss", Locale.ENGLISH);
        mDifferenceDateFormat.setTimeZone(timeZone);
        mEvents = new ArrayList<EventDataForEventList>();
        mFragment = fragment;
        Timer mTimer = new Timer();
        AdapterTimerTask mMyTimerTask = new AdapterTimerTask();
        mTimer.schedule(mMyTimerTask, 0, 500);
        mToken = token;
    }

    public void setEvents(ArrayList<EventDataForEventList> events){
        mEvents.clear();
        mEvents.addAll(events);
        for(int i = 0; i < mEvents.size(); i++){
            Log.d("sssaa", mEventDateFormat.format(mEvents.get(i).eventDate));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0) {
            return ADVERTISEMENT_ROW_TYPE;
        }else {
            return EVENT_ROW_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == ADVERTISEMENT_ROW_TYPE){
            view = inflater.inflate(R.layout.advertisement_list_item, parent, false);
            return new EventListAdapter.AdvertisementViewHolder(view);
        }else {
            view = inflater.inflate(R.layout.event_list_item, parent, false);
            return new EventListAdapter.EventViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position1) {
        if(position1 % 5 != 0) {
            for(int i = 0; i < 25; i++){
                if(position1>i*4){
                    position1-=1;
                }
            }
            int position = position1;
            EventViewHolder holder = (EventViewHolder) viewHolder;
            holder.textViewEventTitle.setText(mEvents.get(position).eventData.description);
            if(mEvents.get(position).eventImage!=null) {
                Glide
                        .with(mFragment.getContext())
                        .load(mEvents.get(position).eventImage)
                        .into(holder.imageViewEvent);
            }else{
                holder.imageViewEvent.setVisibility(View.GONE);
            }
            holder.textViewActivationTime.setText(mEventDateFormat.format(mEvents.get(position).eventDate));
            long difference = getTimeDifference(mEvents.get(position).eventDate);
            if (position == 0 && difference < 1800000) {
                holder.textViewTimer.setTextColor(mFragment.getResources().getColor(R.color.A400red));
            }
            if (difference < 0) {
                if (difference < -300 * 1000) {
                    mFragment.fetchEvents();
                }
                holder.textViewTimer.setText("You can vote!");
                holder.textViewTimer.setTextColor(Color.GREEN);
            } else {
                holder.textViewTimer.setText(String.format("%02d:%02d:%02d", difference / 1000 / 3600, difference / 1000 / 60 % 60, difference / 1000 % 60));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SupportFragment supportFragment = new SupportFragment();
                    supportFragment.setToken(mToken);
                    supportFragment.setEventData(mEvents.get(position).eventData);
                    FragmentManager manager = mFragment.getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragment_container, supportFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        else {
            AdvertisementViewHolder holder = (AdvertisementViewHolder) viewHolder;
            holder.adView.loadAd(mAdRequest);
            //holder.setIsRecyclable(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int advertisementCount = mEvents.size()/4;
        return mEvents.size() + advertisementCount;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventTitle;
        TextView textViewActivationTime;
        TextView textViewTimer;
        ImageView imageViewEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewEvent);
            textViewEventTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewActivationTime = (TextView) itemView.findViewById(R.id.textViewActivationTime);
            textViewTimer = (TextView) itemView.findViewById(R.id.textViewTimerToEvent);
        }
    }

    public static class AdvertisementViewHolder extends RecyclerView.ViewHolder{

       AdView adView;

        public AdvertisementViewHolder(View itemView) {
            super(itemView);
            adView = (AdView) itemView.findViewById(R.id.adView);
            //setIsRecyclable(false);
        }
    }

    private long getTimeDifference(Date date){
        TimeZone timeZone = TimeZone.getDefault();
        Calendar today = Calendar.getInstance(timeZone);
        Date currentDate = today.getTime();
        long differenceLong = date.getTime() - currentDate.getTime();
        return differenceLong;
    }

    class AdapterTimerTask extends TimerTask {

        @Override
        public void run() {
            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //notifyDataSetChanged();
                    for(int i = 0; i < getItemCount(); i++) {
                        if (i % 5 != 0) {
                            notifyItemChanged(i);
                        }
                    }
                }
            });
        }
    }
}
