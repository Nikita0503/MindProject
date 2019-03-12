package com.mindproject.mindproject.edit_profile;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.PhotoDownloader;
import com.mindproject.mindproject.model.data.ChangeEmail;
import com.mindproject.mindproject.model.data.ChangeUsernameAndPhone;
import com.mindproject.mindproject.model.data.Photo;
import com.mindproject.mindproject.model.data.UserData;

import org.json.JSONArray;
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
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 18.02.2019.
 */

public class EditProfilePresenter implements BaseContract.BasePresenter {

    private boolean mEditedUsernameAndPhone;
    private boolean mEditedEmail;
    private boolean mEditedAvatar;
    private EditProfileActivity mActivity;
    private CompositeDisposable mDisposable;
    private MyMindAPIUtils mAPIUtils;

    public EditProfilePresenter(EditProfileActivity activity){
        mActivity = activity;
        mAPIUtils = new MyMindAPIUtils();
    }

    @Override
    public void onStart() {
        mDisposable = new CompositeDisposable();
    }

    public void changeUsernameAndPhone(String token, String username, String phone){
        Disposable changes = mAPIUtils.changeUsernameAndPhone(token, new ChangeUsernameAndPhone(username, phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mEditedUsernameAndPhone = true;
                        stopLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(changes);
    }

    public void changeAvatar(String token, Uri uri){
        if(uri!=null) {
            Log.d("TAG", uri.getPath());
            Disposable avatar = mAPIUtils.changeAvatar(token, getRealPathFromUri(mActivity.getApplicationContext(), uri))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            mEditedAvatar = true;
                            stopLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (e instanceof HttpException) {
                                HttpException exception = (HttpException) e;
                                ResponseBody responseBody = exception.response().errorBody();
                                try {
                                    JSONObject responseError = new JSONObject(responseBody.string());
                                    JSONArray arrayError = responseError.getJSONArray("errors");
                                    JSONObject errorMessage = arrayError.getJSONObject(0);
                                    mActivity.showMessage(errorMessage.getString("ERROR_MESSAGE"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
            mDisposable.add(avatar);
        }else {
            mEditedAvatar = true;
            stopLoading();
        }
    }

    public void changeEmail(String token, String email){
        Disposable emailDisposable = mAPIUtils.changeEmail(token, new ChangeEmail(email))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mEditedEmail = true;
                        stopLoading();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
        mDisposable.add(emailDisposable);
    }

    public void fetchUserData(String deviceId){
        mActivity.startLoading();
        Disposable tokenDisposable = mAPIUtils.getToken(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserData>() {
                    @Override
                    public void onSuccess(UserData data) {
                        Log.d("USER_DATA", data.accessToken);
                        if(data.avatar!=null) {
                            downloadPhoto(data.avatar);
                        }
                        if(data.username!=null) {
                            mActivity.setName(data.username);
                        }
                        if(data.phone!=null) {
                            mActivity.setPhone(data.phone);
                        }
                        if(data.email!=null) {
                            mActivity.setEmail(data.email);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(tokenDisposable);
    }

    public void downloadPhoto(Object photo){
        PhotoDownloader downloader = new PhotoDownloader(mActivity.getApplicationContext());
        Disposable data = downloader.fetchPhoto(photo.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap value) {
                        mActivity.setPhoto(value);
                        mActivity.stopLoading();
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(data);
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

    private void stopLoading(){
        if(mEditedUsernameAndPhone && mEditedAvatar && mEditedEmail){
            mActivity.stopLoading();
            mEditedUsernameAndPhone = false;
            mEditedEmail = false;
            mEditedAvatar = false;
            mActivity.showMessage("Profile has been updated successfully");
        }
    }

    @Override
    public void onStop() {
        mDisposable.clear();
    }
}
