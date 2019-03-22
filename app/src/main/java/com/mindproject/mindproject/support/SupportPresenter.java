package com.mindproject.mindproject.support;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.main.MainActivity;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.PhotoDownloader;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.Photo;
import com.mindproject.mindproject.model.data.Vote;
import com.mindproject.mindproject.my_karma.MyKarmaActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 18.02.2019.
 */

public class SupportPresenter implements BaseContract.BasePresenter {

    private SupportFragment mFragment;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public SupportPresenter(SupportFragment fragment){
        mFragment = fragment;
        mAPIUtils = new MyMindAPIUtils();
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void downloadPhotos(List<Photo> photos){
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        PhotoDownloader downloader = new PhotoDownloader(mFragment);
        Disposable data = downloader.fetchPhotos(photos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Bitmap>() {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        bitmaps.add(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mFragment.showData(bitmaps);
                    }
                });
        mDisposable.add(data);
    }

    public void voteForEvent(String token, int id) {
        Disposable vote = mAPIUtils.voteForEvent(token, new Vote(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mFragment.showMessage("Voted successfully!");
                        MainActivity activity = (MainActivity) mFragment.getActivity();
                        activity.updateKarmaPoints();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            if(exception.code()==422){
                                mFragment.showMessage("You can not vote for own requests.");
                            }else {
                                ResponseBody responseBody = exception.response().errorBody();
                                try {
                                    JSONObject responseError = new JSONObject(responseBody.string());
                                    String errorMessage = responseError.getString("message");
                                    mFragment.showMessage(errorMessage);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });
        mDisposable.add(vote);
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
