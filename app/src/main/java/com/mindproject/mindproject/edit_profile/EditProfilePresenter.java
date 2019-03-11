package com.mindproject.mindproject.edit_profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.model.MyMindAPIUtils;
import com.mindproject.mindproject.model.data.ChangeUsernameAndPhone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Nikita on 18.02.2019.
 */

public class EditProfilePresenter implements BaseContract.BasePresenter {

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
                        mActivity.showMessage("Username and phone have been changed successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mDisposable.add(changes);
    }

    public void changeAvatar(String token, Uri uri){
        Disposable avatar = mAPIUtils.changeAvatar(token, getRealPathFromUri(mActivity.getApplicationContext(), uri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mActivity.showMessage("Photo has been changed successfully");
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
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
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
