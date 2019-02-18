package com.mindproject.mindproject.edit_profile;

import com.mindproject.mindproject.BaseContract;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Nikita on 18.02.2019.
 */

public class EditProfilePresenter implements BaseContract.BasePresenter {

    private EditProfileActivity mActivity;
    private CompositeDisposable mDisposable;

    public EditProfilePresenter(EditProfileActivity activity){
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
