package com.mindproject.mindproject.main;

import android.util.Log;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.UserData;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 18.02.2019.
 */

public class MainPresenter implements BaseContract.BasePresenter {

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
        Disposable tokenDisposable = mAPIUtils.getUserData(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AccessToken>() {
                    @Override
                    public void onSuccess(AccessToken data) {
                        //Toast.makeText(mActivity.getApplicationContext(), data.accessToken, Toast.LENGTH_SHORT).show();
                        Log.d("USER_DATA", data.accessToken);
                        /*Log.d("USER_DATA", data.username);
                        Log.d("USER_DATA", data.avatar.toString());
                        Log.d("USER_DATA", String.valueOf(data.carma));
                        Log.d("USER_DATA", data.phone);
                        Log.d("USER_DATA", data.accessToken);*/

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
