package com.mindproject.mindproject.support;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.my_karma.MyKarmaActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nikita on 18.02.2019.
 */

public class SupportPresenter implements BaseContract.BasePresenter {

    private SupportActivity mActivity;
    private CompositeDisposable mDisposable;

    public SupportPresenter(SupportActivity activity){
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
