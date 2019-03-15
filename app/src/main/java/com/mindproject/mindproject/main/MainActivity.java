package com.mindproject.mindproject.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.my_karma.MyKarmaActivity;
import com.victor.loading.rotate.RotateLoading;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {

    private int mKarmaPoints;
    private String mDeviceId;
    private String mToken;
    private MainPresenter mPresenter;
    private EventListAdapter mEventAdapter;

    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoint;
    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    @OnClick(R.id.textViewKarmaPoint)
    void onClick(){
        Intent intent = new Intent(getApplicationContext(), MyKarmaActivity.class);
        intent.putExtra("token", mToken);
        intent.putExtra("deviceId", mDeviceId);
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
    }

    @Override
    protected void onStart(){
        super.onStart();
        mPresenter.fetchToken(mDeviceId);
        if(mToken!=null){
            mPresenter.fetchEvents(mToken);
            rotateLoading.start();
        }
    }

    public void setToken(String token){
        mToken = token;
        mPresenter.fetchEvents(mToken);
        rotateLoading.start();
        mEventAdapter = new EventListAdapter(this, mToken);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewEvents.setAdapter(mEventAdapter);
    }

    public void updateKarmaPoints(){
        mPresenter.fetchToken(mDeviceId);
    }

    public void setKarmaPoints(int karmaPoints){
        mKarmaPoints = karmaPoints;
        textViewKarmaPoint.setText("Ваша карма " + mKarmaPoints);
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
        rotateLoading.stop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
