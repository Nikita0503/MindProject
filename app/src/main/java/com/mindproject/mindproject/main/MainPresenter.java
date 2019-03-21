package com.mindproject.mindproject.main;

import android.util.Log;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.UserData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 18.03.2019.
 */

public class MainPresenter implements BaseContract.BasePresenter{

    private MainActivity mActivity;
    private MyMindAPIUtils mAPIUtils;
    private CompositeDisposable mDisposable;

    public MainPresenter(MainActivity activity){
        mActivity = activity;
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
                        mActivity.setToken(data.accessToken);
                        //mActivity.setKarmaPoints(data.karma);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(tokenDisposable);
    }

    public void fetchEventDataByEventId(String token, String eventId){
        Disposable eventData = mAPIUtils.getEventDataByEventId(token, eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<EventData>() {
                    @Override
                    public void onSuccess(EventData data) {
                        mActivity.openSupportFragmentByInvitation(data);
                        //mActivity.setKarmaPoints(data.karma);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
