package com.mindproject.mindproject.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.edit_profile.EditProfilePresenter;
import com.mindproject.mindproject.model.data.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;


/**
 * Created by Nikita on 07.03.2019.
 */

public class PhotoDownloader {

    private Context mContext;
    private BaseContract.BasePresenter mPresenter;

    public PhotoDownloader(Context context, BaseContract.BasePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    public Observable<Bitmap> fetchPhotos(List<Photo> photos){
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                for(int i = 0; i < photos.size(); i++){
                    Bitmap image = Glide.with(mContext)
                                .asBitmap()
                                .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com" + photos.get(i).photo)
                                .into(200,200)
                                .get();
                    e.onNext(image);
                }
                e.onComplete();
            }
        });
    }

    public void fetchPhoto(String photoURL){

        Glide.with(mContext)
                .asBitmap()
                .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com" + photoURL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        EditProfilePresenter presenter = (EditProfilePresenter) mPresenter;
                        presenter.setPhoto(resource);
                    }
                });


    }

}
