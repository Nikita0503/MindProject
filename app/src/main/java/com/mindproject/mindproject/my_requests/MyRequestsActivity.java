package com.mindproject.mindproject.my_requests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.main.EventListAdapter;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.my_karma.MyKarmaPresenter;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRequestsActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mDeviceId;
    private String mToken;
    private UserData mUserData;
    private MyRequestsPresenter mPresenter;
    private MyEventListAdapter mEventAdapter;

    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
    @BindView(R.id.recycler_view_my_events)
    RecyclerView recyclerViewMyEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mDeviceId = intent.getStringExtra("deviceId");
        mPresenter = new MyRequestsPresenter(this);
        mPresenter.onStart();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mPresenter.fetchUserData(mDeviceId);
    }

    public void setUserData(UserData userData){
        mUserData = userData;
        mPresenter.fetchMyRequests(mToken, mUserData.id);
        mEventAdapter = new MyEventListAdapter(this, mToken);
        recyclerViewMyEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewMyEvents.setAdapter(mEventAdapter);
        recyclerViewMyEvents.setVisibility(View.INVISIBLE);
        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();
    }

    //public void setTotalCount(int count){
    //    mTotalCount = count;
    //}

    //public void fetchMoreEvents(int count){
    //    mPresenter.fetchMyRequests(mToken, mUserData.id);
    //}

    public void addEventsToList(ArrayList<EventDataForEventList> events){
        mEventAdapter.addEvents(events);
        recyclerViewMyEvents.setVisibility(View.VISIBLE);
        rotateLoading.setVisibility(View.INVISIBLE);
        rotateLoading.stop();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
