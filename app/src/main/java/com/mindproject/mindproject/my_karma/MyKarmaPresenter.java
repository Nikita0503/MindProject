package com.mindproject.mindproject.my_karma;

import android.util.Log;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.UserData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 18.02.2019.
 */

public class MyKarmaPresenter implements BaseContract.BasePresenter{

    private MyKarmaActivity mActivity;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public MyKarmaPresenter(MyKarmaActivity activity){
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

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
