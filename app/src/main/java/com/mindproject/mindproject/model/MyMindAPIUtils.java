package com.mindproject.mindproject.model;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikita on 18.02.2019.
 */

public class MyMindAPIUtils {

    public static final String BASE_URL = "http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com/api/v1/";

    public Single<AccessToken> getToken(String deviceId){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getToken(new UserDataAuthorization(deviceId));
    }

    public Single<ArrayList<EventData>> getEvents(String token){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getEvents("Bearer " + token);
    }

    public Observable<EventDataForEventList> getEventsForList(ArrayList<EventData> events, Context context){
        return Observable.create(new ObservableOnSubscribe<EventDataForEventList>() {
            @Override
            public void subscribe(ObservableEmitter<EventDataForEventList> e) throws Exception {
                for(int i = 0; i < events.size(); i++){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    String title = events.get(i).title;
                    Date date = simpleDateFormat.parse(events.get(i).startTime);
                    Drawable image;
                    if(events.get(i).photos.size() != 0){
                        image = new BitmapDrawable(context.getResources(), Picasso.with(context)
                                .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com"+events.get(i).photos.get(0).photo)
                                .get());
                    }else {
                        image = context.getResources().getDrawable(R.drawable.ic_photo);
                    }
                    //Drawable image = context.getResources().getDrawable(R.drawable.ic_user);
                    EventDataForEventList event = new EventDataForEventList(title, date, image, events.get(i));
                    e.onNext(event);
                }
                e.onComplete();
            };
        });
    }

    public Completable sendRequestData(String token, AddRequestData data){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.sendRequestData("Bearer " + token, data);
    }


    public static Retrofit getClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
