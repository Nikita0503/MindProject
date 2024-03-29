package com.mindproject.mindproject.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.AccessToken;
import com.mindproject.mindproject.model.data.AddRequestData;
import com.mindproject.mindproject.model.data.ChangeEmail;
import com.mindproject.mindproject.model.data.ChangeUsernameAndPhone;
import com.mindproject.mindproject.model.data.EventData;
import com.mindproject.mindproject.model.data.EventDataForEventList;
import com.mindproject.mindproject.model.data.Response;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.model.data.UserDataAuthorization;
import com.mindproject.mindproject.model.data.Vote;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikita on 18.02.2019.
 */

public class MyMindAPIUtils {

    //public static final String BASE_URL = "http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com/api/v1/";
    public static final String BASE_URL = "https://in-mind.sooprit.com/api/v1/";

    public Single<UserData> getToken(String deviceId){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getToken(new UserDataAuthorization(deviceId));
    }

    public Single<Response> getEvents(String token){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        return apiService.getEvents("Bearer " + token, simpleDateFormat.format(calendar.getTime()));
    }

    public Single<EventData> getEventDataByEventId(String token, String eventId){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.getEventDataByEventId(token, eventId);
    }

    //public Single<Response> getMyEvents(String token, String id, int count, int totalCount){
    //    Retrofit retrofit = getClient(BASE_URL);
    //    APIService apiService = retrofit.create(APIService.class);
    //    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    //    Calendar calendar = Calendar.getInstance();
    //    calendar.set(Calendar.MONTH, -1);
    //    int page = (count / 10) + 1;
    //    //Log.d("COUNT", "page = " + page + " totalCount = " + totalPageCount);
    //    if(count<=totalCount) {
    //        //Log.d("COUNT", "start = " + count / 20 + " end = " + totalCount);
    //        return apiService.getMyEvents("Bearer " + token, simpleDateFormat.format(calendar.getTime()), id, page, 10);
    //    }else{
    //        return null;
    //    }
    //}

    public Single<Response> getMyEvents(String token, String id){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Calendar calendar = Calendar.getInstance();
        return apiService.getMyEvents("Bearer " + token, simpleDateFormat.format(calendar.getTime()), id, 1, 20);
    }



    public Observable<EventDataForEventList> getEventsForList(ArrayList<EventData> events, Context context){
        return Observable.create(new ObservableOnSubscribe<EventDataForEventList>() {
            @Override
            public void subscribe(ObservableEmitter<EventDataForEventList> e) throws Exception {
                for(int i = 0; i < events.size(); i++){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    //Log.d("TIMEZONE", events.get(i).startTime); //String .valueOf(simpleDateFormat.getTimeZone().getRawOffset()/3600000)
                    String title = events.get(i).title;
                    Date date = simpleDateFormat.parse(events.get(i).startTime);
                    Bitmap image;
                    if(events.get(i).photos.size() != 0){
                        image = Glide.with(context)
                                .asBitmap()
                                .load("http://ec2-63-34-126-19.eu-west-1.compute.amazonaws.com"+events.get(i).photos.get(0).photo)
                                .into(200,200)
                                .get();
                    }else {
                        image = null;
                    }
                    EventDataForEventList event = new EventDataForEventList(title, date, image, events.get(i));
                    e.onNext(event);
                }
                e.onComplete();
            };
        });
    }

    public Single<Response> getEventsForDate(String token, String date){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return apiService.getEventsForDate(token, date, date);
    }

    public Completable sendRequestData(String token, AddRequestData data,  ArrayList<MultipartBody.Part> files) {
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        //Map<String, AddRequestData> map = new HashMap<String, AddRequestData>();
        //map.put("data", data);
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), data.title);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), data.description);
        RequestBody start_time = RequestBody.create(MediaType.parse("text/plain"), data.start_time);
        if(files.size()!=0) {
            return apiService.sendRequestDataWith1Photo("Bearer " + token, title, description, start_time, files);
        }else{
            return apiService.sendRequestData("Bearer " + token, title, description, start_time);
        }
    }

    public Completable voteForEvent(String token, Vote vote){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.voteForEvent("Bearer " + token, vote);
    }

    public Completable changeUsernameAndPhone(String token, ChangeUsernameAndPhone data){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.changeUsernameAndPhone("Bearer " + token, data);
    }

    public Completable changeAvatar(String token, String path) {
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        return apiService.changeAvatar("Bearer " + token, body);
    }

    public Completable changeEmail(String token, ChangeEmail email){
        Retrofit retrofit = getClient(BASE_URL);
        APIService apiService = retrofit.create(APIService.class);
        return apiService.changeEmail("Bearer " + token, email);
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
