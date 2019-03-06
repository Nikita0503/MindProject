package com.mindproject.mindproject.my_karma;

import com.mindproject.mindproject.BaseContract;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nikita on 18.02.2019.
 */

public class MyKarmaPresenter implements BaseContract.BasePresenter{

    private MyKarmaActivity mActivity;
    private CompositeDisposable mDisposable;

    public MyKarmaPresenter(MyKarmaActivity activity){
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void fetchUserInfo(String deviceId){

    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
