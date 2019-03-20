package com.mindproject.mindproject.main;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private String mToken;
    private SimpleDateFormat mDifferenceDateFormat;
    private SimpleDateFormat mEventDateFormat;
    private ArrayList<EventDataForEventList> mEvents;
    private ListFragment mFragment;

    public EventListAdapter(ListFragment fragment, String token) {
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
        notifyDataSetChanged();
        //for(int i = 0; i < events.size(); i++){
        //    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        //    Log.d("TIMEZONE", simpleDateFormat.format(events.get(i).eventDate));
        //}
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
        Date difference = getTimeDifference(mEvents.get(position).eventDate);
        if(position == 0 && difference.getTime()<1800000 ){
            holder.textViewTimer.setTextColor(mFragment.getResources().getColor(R.color.A400red));
        }
        holder.textViewTimer.setText(mDifferenceDateFormat.format(difference));
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

    private Date getTimeDifference(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        //Log.d("timezone", dateFormat.format(date));
        TimeZone timeZone = TimeZone.getDefault();
        int timeZoneInt = timeZone.getRawOffset()/3600000;
        //mEventDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar today = Calendar.getInstance(timeZone);
        today.add(Calendar.HOUR_OF_DAY, timeZoneInt);
        Date currentDate = today.getTime();
        //Log.d("TAG", currentDate.getTime()+"");
        long differenceLong = date.getTime() - currentDate.getTime();
        return new Date(differenceLong);
    }

    class AdapterTimerTask extends TimerTask {

        @Override
        public void run() {
            mFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }
}
