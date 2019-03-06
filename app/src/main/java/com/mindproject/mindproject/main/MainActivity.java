package com.mindproject.mindproject.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mindproject.mindproject.my_karma.MyKarmaActivity;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mDeviceId;
    private String mToken;
    private MainPresenter mPresenter;
    private EventListAdapter mEventAdapter;

    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoint;

    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    @OnClick(R.id.textViewKarmaPoint)
    void onClick(){
        Intent intent = new Intent(getApplicationContext(), MyKarmaActivity.class);
        intent.putExtra("id", mDeviceId);
        intent.putExtra("token", mToken);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to karma activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this);
        mPresenter.onStart();
        mDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("DEVICE_ID", mDeviceId);

        mEventAdapter = new EventListAdapter(this);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewEvents.setAdapter(mEventAdapter);
        //mEventAdapter.addEvents(initData());
        mPresenter.fetchToken(mDeviceId);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mToken!=null){
            mPresenter.fetchEvents(mToken);
        }
    }

    public void setToken(String token){
        mToken = token;
        mPresenter.fetchEvents(mToken);
        Log.d("TOKEN", mToken);
    }

    private ArrayList<EventDataForEventList> initData(){
        Drawable drawable = getDrawable(R.drawable.ic_launcher_background);
        ArrayList<EventDataForEventList> events = new ArrayList<EventDataForEventList>();
        for (int i = 0; i < 10; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, i);
            EventDataForEventList data = new EventDataForEventList("Event"+i, calendar.getTime(), drawable);
            events.add(data);
        }
        return events;
    }

    public void addEventsToList(ArrayList<EventDataForEventList> events){
        mEventAdapter.setEvents(events);
    }


    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
