package com.mindproject.mindproject.add_request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.AddRequestData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Nikita on 18.02.2019.
 */

public class AddRequestPresenter implements BaseContract.BasePresenter {

    private AddRequestActivity mActivity;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public AddRequestPresenter(AddRequestActivity activity){
        mActivity = activity;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
        mAPIUtils = new MyMindAPIUtils();
    }

    public void generateData(String token, String title, String description, String date, String time, Bitmap photo){
        //RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), createFileFromBitmap(photo));
        //MultipartBody.Part body = MultipartBody.Part.createFormData("upload", "photo", reqFile);
        AddRequestData requestData = new AddRequestData(getDate(date, time), title, description, createFileFromBitmap(photo));
        Disposable sendData = mAPIUtils.sendRequestData(token, requestData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(mActivity.getApplicationContext(), "Event has been created!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mActivity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
        mDisposable.add(sendData);
    }

    private File createFileFromBitmap(Bitmap photo){
        File f = new File(mActivity.getCacheDir(), "photo");
        try {
            f.createNewFile();

//Convert bitmap to byte array
            Bitmap bitmap = photo;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch (Exception c){
            c.printStackTrace();
        }
        return f;
    }

    private String getDate(String date, String time){
        String startTime = null;
        try {
            SimpleDateFormat dateFormatBefore = new SimpleDateFormat("dd MMM yyyy H:mm", Locale.ENGLISH);

            Date date1 = dateFormatBefore.parse(date + " " + time);
            SimpleDateFormat dateFormatAfter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
            startTime = dateFormatAfter.format(date1);
        }catch (Exception c){
            c.printStackTrace();
        }
        return startTime;
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
