package com.mindproject.mindproject.past_event;

import android.graphics.Bitmap;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.PhotoDownloader;
import com.mindproject.mindproject.model.data.Photo;
import com.mindproject.mindproject.support.SupportFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nikita on 18.03.2019.
 */

public class PastEventPresenter implements BaseContract.BasePresenter {

    private PastEventFragment mFragment;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public PastEventPresenter(PastEventFragment fragment){
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

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
