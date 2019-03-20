package com.mindproject.mindproject.add_request;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nikita on 19.03.2019.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private HashMap<Integer, Boolean> mAllowed;
    private ArrayList<Integer> mNumbers;
    private AddRequestFragment mFragment;

    public TimeAdapter(ArrayList<Integer> numbers, AddRequestFragment fragment) {
        mNumbers = numbers;
        mFragment = fragment;
        mAllowed = new HashMap<Integer, Boolean>();
        for(int i = 0; i < 24; i++){
            boolean isAllowed = false;
            for(int j = 0; j < numbers.size(); j++) {
                if (i == numbers.get(j)) {
                    isAllowed = true;
                    break;
                }
            }
            mAllowed.put(i, isAllowed);
        }
    }

    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.choose_time_list_item, parent, false);
        return new TimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeAdapter.ViewHolder holder, int position) {
        //holder.textViewTime.setText(mNumbers.get(position)+":00");
        holder.textViewTime.setText(position+":00");
        if(!mAllowed.get(position)){
            holder.textViewTime.setTextColor(Color.RED);
            holder.itemView.setBackgroundColor(Color.GRAY);
            holder.itemView.setEnabled(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mFragment.getContext(), position+"", Toast.LENGTH_SHORT).show();
                if(mAllowed.get(position)) {
                    mFragment.setTime(position + ":00");
                }else{
                    Toast.makeText(mFragment.getContext(), "Time not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAllowed.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        }
    }
}
