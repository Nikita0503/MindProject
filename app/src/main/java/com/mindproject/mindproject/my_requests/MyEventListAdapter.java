package com.mindproject.mindproject.my_requests;

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
import com.mindproject.mindproject.main.EventListAdapter;
import com.mindproject.mindproject.main.MainActivity;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.past_event.PastEventFragment;
import com.mindproject.mindproject.support.SupportFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;

/**
 * Created by Nikita on 15.03.2019.
 */

public class MyEventListAdapter extends RecyclerView.Adapter<MyEventListAdapter.ViewHolder> {

    private String mToken;
    private SimpleDateFormat mEventDateFormat;
    private ArrayList<EventDataForEventList> mEvents;
    private MyRequestsFragment mFragment;

    public MyEventListAdapter(MyRequestsFragment fragment, String token) {
        mEventDateFormat = new SimpleDateFormat("d MMM H:mm", Locale.ENGLISH);
        mEvents = new ArrayList<EventDataForEventList>();
        mFragment = fragment;
        mToken = token;
    }

    public void addEvents(ArrayList<EventDataForEventList> events){
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public MyEventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_event_list_item, parent, false);
        return new MyEventListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyEventListAdapter.ViewHolder holder, int position) {
        holder.textViewEventTitle.setText(mEvents.get(position).title);
        holder.imageViewEvent.setImageDrawable(mEvents.get(position).eventImage);
        holder.textViewActivationTime.setText(mEventDateFormat.format(mEvents.get(position).eventDate));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PastEventFragment pastEventFragment = new PastEventFragment();
                pastEventFragment.setToken(mToken);
                pastEventFragment.setEventData(mEvents.get(position).eventData);
                FragmentManager manager = mFragment.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, pastEventFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //if(position > mEvents.size()-3){
        //    mActivity.fetchMoreEvents(mEvents.size());
        //    Log.d("COUNT", mEvents.size()+"");
        //}
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventTitle;
        TextView textViewActivationTime;
        ImageView imageViewEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewEvent = (ImageView) itemView.findViewById(R.id.imageViewEvent);
            textViewEventTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewActivationTime = (TextView) itemView.findViewById(R.id.textViewActivationTime);
        }
    }

}
