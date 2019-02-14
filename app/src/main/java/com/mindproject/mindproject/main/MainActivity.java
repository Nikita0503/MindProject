package com.mindproject.mindproject.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.R;
import com.mindproject.mindproject.data.EventDataInList;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mindproject.mindproject.main.EventListAdapter;
import com.mindproject.mindproject.my_karma.MyKarmaActivity;

public class MainActivity extends AppCompatActivity {

    private EventListAdapter mEventAdapter;

    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoint;

    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    @OnClick(R.id.textViewKarmaPoint)
    void onClick(){
        Intent intent = new Intent(getApplicationContext(), MyKarmaActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to karma activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mEventAdapter = new EventListAdapter(this);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewEvents.setAdapter(mEventAdapter);
        mEventAdapter.addEvents(initData());
    }

    private ArrayList<EventDataInList> initData(){
        Drawable drawable = getDrawable(R.drawable.ic_launcher_background);
        ArrayList<EventDataInList> events = new ArrayList<EventDataInList>();
        for (int i = 0; i < 10; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, i);
            EventDataInList data = new EventDataInList("Event"+i, calendar.getTime(), drawable);
            events.add(data);
        }
        return events;
    }
}
