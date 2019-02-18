package com.mindproject.mindproject.add_request;

import com.mindproject.mindproject.BaseContract;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nikita on 18.02.2019.
 */

public class AddRequestPresenter implements BaseContract.BasePresenter {

    private AddRequestActivity mActivity;
    private CompositeDisposable mDisposable;

    public AddRequestPresenter(AddRequestActivity activity){
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
