package com.mindproject.mindproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.my_karma.MyKarmaActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nikita on 18.03.2019.
 */

public class ListFragment extends Fragment implements BaseContract.BaseView{

    private int mKarmaPoints;
    private String mDeviceId;
    private String mToken;
    private ListPresenter mPresenter;
    private EventListAdapter mEventAdapter;

    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoint;
    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    //@OnClick(R.id.textViewKarmaPoint)
    //void onClick(){
    //    Intent intent = new Intent(getContext(), MyKarmaActivity.class);
    //    intent.putExtra("token", mToken);
    //    intent.putExtra("deviceId", mDeviceId);
    //    startActivity(intent);
    //    Toast.makeText(getContext(), "Welcome to karma activity", Toast.LENGTH_SHORT).show();
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ListPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //mPresenter.fetchToken(mDeviceId);
        //if(mToken!=null){
        //    mPresenter.fetchEvents(mToken);
        //    rotateLoading.start();
        //}
        mPresenter.fetchToken(mDeviceId);
        rotateLoading.start();
        mEventAdapter = new EventListAdapter(this, mToken);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEvents.setAdapter(mEventAdapter);
    }

    public void setDeviceId(String deviceId){
        mDeviceId = deviceId;
    }

    public void setToken(String token){
        mToken = token;
        mPresenter.fetchEvents(mToken);
    }

    public void setKarmaPoints(int karmaPoints){
        mKarmaPoints = karmaPoints;
        textViewKarmaPoint.setText("Ваша карма " + mKarmaPoints);
    }

    public void addEventsToList(ArrayList<EventDataForEventList> events){
        mEventAdapter.setEvents(events);
        rotateLoading.stop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
