package com.mindproject.mindproject.my_requests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.UserData;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 18.03.2019.
 */

public class MyRequestsFragment extends Fragment implements BaseContract.BaseView{

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MyRequestsPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_requests, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();
        mPresenter.fetchUserData(mDeviceId);
    }

    public void setDeviceId(String deviceId){
        mDeviceId = deviceId;
    }

    public void setToken(String token){
        mToken = token;
    }

    public void setUserData(UserData userData){
        mUserData = userData;
        mPresenter.fetchMyRequests(mToken, mUserData.id);
        mEventAdapter = new MyEventListAdapter(this, mToken);
        recyclerViewMyEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyEvents.setAdapter(mEventAdapter);
        recyclerViewMyEvents.setVisibility(View.INVISIBLE);
        rotateLoading.setVisibility(View.VISIBLE);
        rotateLoading.start();
    }

    public void addEventsToList(ArrayList<EventDataForEventList> events){
        mEventAdapter.addEvents(events);
        recyclerViewMyEvents.setVisibility(View.VISIBLE);
        rotateLoading.setVisibility(View.INVISIBLE);
        rotateLoading.stop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onStop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
