package com.mindproject.mindproject.main;

import android.util.Log;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.Response;
import com.mindproject.mindproject.model.data.UserData;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 18.02.2019.
 */

public class ListPresenter implements BaseContract.BasePresenter {

    private ListFragment mFragment;
    private MyMindAPIUtils mAPIUtils;
    private CompositeDisposable mDisposable;

    public ListPresenter(ListFragment fragment){
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
        mAPIUtils = new MyMindAPIUtils();
    }

    public void fetchToken(String deviceId){
        Disposable tokenDisposable = mAPIUtils.getToken(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserData>() {
                    @Override
                    public void onSuccess(UserData data) {
                        Log.d("USER_DATA", data.accessToken);
                        mFragment.setToken(data.accessToken);
                        mFragment.setKarmaPoints(data.karma);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(tokenDisposable);
    }

    public void fetchEvents(String token){
        Disposable requests = mAPIUtils.getEvents(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response events) {
                        Log.d("EVENT_COUNT", "size = " + events.requests.size());
                        transformEvents(events.requests);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(requests);
    }

    private void transformEvents(ArrayList<EventData> events) {
        ArrayList<EventDataForEventList> transformedEvents = new ArrayList<EventDataForEventList>();
        Disposable transform = mAPIUtils.getEventsForList(events, mFragment.getContext())
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
                        mFragment.addEventsToList(transformedEvents);
                    }
                });
        mDisposable.add(transform);
    }


    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
