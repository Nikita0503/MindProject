package com.mindproject.mindproject.my_requests;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.Response;
import com.mindproject.mindproject.model.data.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 15.03.2019.
 */

public class MyRequestsPresenter implements BaseContract.BasePresenter {

    private MyRequestsActivity mActivity;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public MyRequestsPresenter(MyRequestsActivity activity) {
        mActivity = activity;
        mAPIUtils = new MyMindAPIUtils();
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchUserData(String deviceId){
        Disposable tokenDisposable = mAPIUtils.getToken(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserData>() {
                    @Override
                    public void onSuccess(UserData data) {
                        mActivity.setUserData(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(tokenDisposable);
    }

    //public void fetchMyRequests(String token, int id, int count, int totalCount){
    //    Disposable myRequests = mAPIUtils.getMyEvents(token, String.valueOf(id), count, totalCount)
    //            .subscribeOn(Schedulers.io())
    //            .observeOn(AndroidSchedulers.mainThread())
    //            .subscribeWith(new DisposableSingleObserver<Response>() {
    //                @Override
    //                public void onSuccess(Response events) {
    //                    transformEvents(events.requests);
    //                }
//
    //                @Override
    //                public void onError(Throwable e) {
    //                    e.printStackTrace();
    //                }
    //            });
    //    mDisposable.add(myRequests);
    //}

    public void fetchMyRequests(String token, int id){
        Disposable myRequests = mAPIUtils.getMyEvents(token, String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response events) {
                        transformEvents(events.requests);
                        //mActivity.setTotalCount(events.pagination.count);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(myRequests);
    }

    private void transformEvents(ArrayList<EventData> events) {
        ArrayList<EventDataForEventList> transformedEvents = new ArrayList<EventDataForEventList>();
        Disposable transform = mAPIUtils.getEventsForList(events, mActivity.getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EventDataForEventList>() {
                    @Override
                    public void onNext(EventDataForEventList value) {
                        transformedEvents.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mActivity.addEventsToList(transformedEvents);
                    }
                });
        mDisposable.add(transform);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
