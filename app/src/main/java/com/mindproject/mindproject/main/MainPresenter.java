package com.mindproject.mindproject.main;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.UserData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
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
        Disposable tokenDisposable = mAPIUtils.getToken(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserData>() {
                    @Override
                    public void onSuccess(UserData data) {
                        Log.d("USER_DATA", data.accessToken);
                        mActivity.setToken(data.accessToken);
                        mActivity.setKarmaPoints(data.karma);
                        //Log.d("USER_DATA", data.username);
                        //Log.d("USER_DATA", data.avatar.toString());
                        //Log.d("USER_DATA", String.valueOf(data.karma));
                        //Log.d("USER_DATA", data.phone);
                        //Log.d("USER_DATA", data.accessToken);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(tokenDisposable);
    }

    public void fetchEvents(String token){
        Disposable requests = mAPIUtils.getEvents(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<EventData>>() {
                    @Override
                    public void onSuccess(ArrayList<EventData> events) {
                        Log.d("EVENT_COUNT", "size = " + events.size());
                        transformEvents(events);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(requests);
    }

    private void transformEvents(ArrayList<EventData> events) {
        ArrayList<EventDataForEventList> transformedEvents = new ArrayList<EventDataForEventList>();
        Disposable transform = mAPIUtils.getEventsForList(events, mActivity.getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EventDataForEventList>() {
                    @Override
                    public void onNext(EventDataForEventList value) {
                        transformedEvents.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mActivity.addEventsToList(transformedEvents);
                    }
                });
        mDisposable.add(transform);

        /*for(int i = 0; i < events.size(); i++){
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String title = events.get(i).title;
                Date date = simpleDateFormat.parse(events.get(i).startTime);
                Drawable image = new BitmapDrawable(mActivity.getResources(), Picasso.with(mActivity.getApplicationContext())
                        .load(events.get(i).photos.get(0).photo)
                        .into();
                EventDataForEventList event = new EventDataForEventList(title, date, image, events.get(i));

                transformedEvents.add(event);
            }catch (Exception c){
                c.printStackTrace();
            }
        }
        return transformedEvents;*/
    }


    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
