package com.mindproject.mindproject.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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

    public PhotoDownloader(Context context) {
        mContext = context;
    }

    public Observable<Bitmap> fetchPhotos(List<Photo> photos){
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                for(int i = 0; i < photos.size(); i++){
                    Bitmap image = Picasso.with(mContext)
                                .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com" + photos.get(0).photo)
                                .get();
                    e.onNext(image);
                }
                e.onComplete();
            }
        });
    }

    public Single<Bitmap> fetchPhoto(String photoURL){
        return Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> e) throws Exception {
                Bitmap image = Picasso.with(mContext)
                        .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com" + photoURL)
                        .get();
                e.onSuccess(image);
            }
        });
    }
}
