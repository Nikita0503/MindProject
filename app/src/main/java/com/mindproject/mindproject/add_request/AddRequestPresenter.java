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
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.Response;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 18.02.2019.
 */

public class AddRequestPresenter implements BaseContract.BasePresenter {

    private AddRequestFragment mFragment;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public AddRequestPresenter(AddRequestFragment fragment){
        mFragment = fragment;
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
        mAPIUtils = new MyMindAPIUtils();
    }

    public void generateData(String token, String title, String description, String date, String time, ArrayList<File> fileList){
        ArrayList<MultipartBody.Part> bodyList = new ArrayList<MultipartBody.Part>();
        for(int i = 0; i < fileList.size(); i++){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", fileList.get(i).getName(), requestFile);
            bodyList.add(body);
        }
        AddRequestData requestData = new AddRequestData(getDate(date, time), title, description);
        Disposable sendData = mAPIUtils.sendRequestData(token, requestData, bodyList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mFragment.showMessage(mFragment.getResources().getString(R.string.event_created));
                        mFragment.stopLoading();
                        mFragment.showMainPage();
                        //mFragment.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFragment.stopLoading();
                        e.printStackTrace();
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            ResponseBody responseBody = exception.response().errorBody();
                            try {
                                JSONObject responseError = new JSONObject(responseBody.string());
                                Log.d("TAG", responseError.toString());
                                mFragment.showMessage(responseError.getString("message"));
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

    public void fetchForDateEvents(String token, Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Disposable dayEvents = mAPIUtils.getEventsForDate(token, simpleDateFormat.format(date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        ArrayList<EventData> events = response.requests;
                        for(int i = 0; i < events.size(); i++){
                            Log.d("time", events.get(i).startTime);
                        }
                        try {
                            mFragment.setAvailableTime(getAvailableTime(events));
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(dayEvents);
    }

    private ArrayList<Integer> getAvailableTime(ArrayList<EventData> events) throws ParseException {
        int hourCurrent = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int dayCurrent = CalendarDay.today().getDay();
        String thatDay;
        int thatDayint;
        if(events.size()>0) {
            thatDay = events.get(0).startTime.split("T")[0];
            thatDayint = Integer.parseInt(thatDay.substring(thatDay.length() - 2));
        }else{
            thatDayint = -1;
        }

        Log.d("time_that_day", thatDayint+"");
        Log.d("time_hour", hourCurrent+"");
        Log.d("time_day", dayCurrent+"");

        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
//Import part : x.0 for double number
        double timeZone = tz.getOffset(now.getTime()) / 3600000.0;

        //TimeZone tZ = TimeZone.getDefault();
        //int timeZone = tZ.getRawOffset()/3600000;
        ArrayList<Integer> hours = new ArrayList<Integer>();
        ArrayList<Integer> eventHours = new ArrayList<Integer>();
        for(int i = 0; i < events.size(); i++){
            int hour = Integer.parseInt(events.get(i).startTime.split("T")[1].substring(0, 2));
            hour+=timeZone;
            //if(hour<0){
            //    hour+=24;
            //}
            eventHours.add(hour);
        }
        boolean isAvailable;
        for(int i = 0; i < 24; i++){
            isAvailable = true;
            for(int j = 0; j < eventHours.size(); j++){
                if(i == eventHours.get(j)){
                    isAvailable = false;
                }
                if(thatDayint == dayCurrent) {
                    if (i <= hourCurrent) {
                        isAvailable = false;
                    }
                }
            }
            if(isAvailable){
                hours.add(i);
            }
        }

        for(int i = 0; i < hours.size(); i++){
            Log.d("time", "h " + hours.get(i));
        }
        return hours;
    }

    private String getDate(String date, String time){
        String startTime = null;
        try {
            SimpleDateFormat dateFormatBefore = new SimpleDateFormat("dd MMM yyyy H:mm", Locale.ENGLISH);
            Date date1 = dateFormatBefore.parse(date + " " + time);
            SimpleDateFormat dateFormatAfter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            //TimeZone timeZone = TimeZone.getDefault();
            //int timeZoneInt = timeZone.getRawOffset()/3600000;

            TimeZone tz = TimeZone.getDefault();
            Date now = new Date();
//Import part : x.0 for double number
            int timeZoneInt = tz.getOffset(now.getTime()) / 3600000;
            //Log.d("TIMEZONE_ADD", String.valueOf(timeZoneInt));
            String timeZoneStr = "";
            if(timeZoneInt > 0) {
                if (timeZoneInt < 10) {
                    timeZoneStr = "+0" + String.valueOf(timeZoneInt);
                } else {
                    timeZoneStr = "+" + String.valueOf(timeZoneInt);
                }
            }if(timeZoneInt < 0){
                if(timeZoneInt>-10){
                    timeZoneStr = "-0" + String.valueOf(Math.abs(timeZoneInt));
                }else{
                    timeZoneStr = String.valueOf(timeZoneInt);
                }
            }
            if(timeZoneInt==0){
                timeZoneStr = "00";
            }
            startTime = dateFormatAfter.format(date1);
            startTime = startTime + timeZoneStr+":00";
            //Log.d("TIMEZONE_ADD", startTime);
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
