package com.mindproject.mindproject.add_request;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.AddRequestData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

    public void generateData(String token, String title, String description, String date, String time, ArrayList<Uri> uriList){
        ArrayList<MultipartBody.Part> bodyList = new ArrayList<MultipartBody.Part>();
        for(int i = 0; i < uriList.size(); i++){
            File file = new File(getRealPathFromUri(mActivity.getApplicationContext(), uriList.get(i)));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            bodyList.add(body);
        }
        AddRequestData requestData = new AddRequestData(getDate(date, time), title, description);
        Disposable sendData = mAPIUtils.sendRequestData(token, requestData, bodyList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(mActivity.getApplicationContext(), "Event has been created!", Toast.LENGTH_SHORT).show();
                        mActivity.finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            ResponseBody responseBody = exception.response().errorBody();
                            try {
                                JSONObject responseError = new JSONObject(responseBody.string());
                                Log.d("Error", responseError.toString());
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        mDisposable.add(sendData);
    }


    private String getDate(String date, String time){
        String startTime = null;
        try {
            SimpleDateFormat dateFormatBefore = new SimpleDateFormat("dd MMM yyyy H:mm", Locale.ENGLISH);

            Date date1 = dateFormatBefore.parse(date + " " + time);
            SimpleDateFormat dateFormatAfter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);
            TimeZone timeZone = TimeZone.getDefault();
            int timeZoneInt = timeZone.getRawOffset()/3600000;
            String timeZoneStr = "";
            if(timeZoneInt > 0) {
                if (timeZoneInt < 10) {
                    timeZoneStr = "+0" + String.valueOf(timeZoneInt);
                } else {
                    timeZoneStr = "+" + String.valueOf(timeZoneInt);
                }
            }else{
                if(timeZoneInt>-10){
                    timeZoneStr = "-0" + String.valueOf(Math.abs(timeZoneInt));
                }else{
                    timeZoneStr = String.valueOf(timeZoneInt);
                }
            }

            Log.d("Жирный", timeZoneStr);

            startTime = dateFormatAfter.format(date1);
            startTime = startTime + timeZoneStr+":00";
        }catch (Exception c){
            c.printStackTrace();
        }
        return startTime;
    }

    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
