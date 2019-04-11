package com.mindproject.mindproject.support;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.main.EventListAdapter;
import com.mindproject.mindproject.main.MainActivity;
import com.mindproject.mindproject.model.data.EventData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Nikita on 07.03.2019.
 */

public class SupportFragment extends Fragment implements BaseContract.BaseView {

  private boolean mIsTouched;
  private int mIsAnimated;
  private Animation mAnimation;
  private String mToken;
  private EventData mEventData;
  private SupportPresenter mPresenter;
  private Timer mTimer;

  @BindView(R.id.textViewRemainingTime)
  TextView textViewRemainingTime;
  @BindView(R.id.textViewName)
  TextView textViewName;
  @BindView(R.id.checkBox)
  CheckBox checkBox;
  @BindView(R.id.viewPager)
  ViewPager viewPagerPhotos;
  @BindView(R.id.textViewDescription)
  TextView textViewDescription;
  @BindView(R.id.textViewTimer)
  Chronometer chronometer;
  @BindView(R.id.buttonSendToFriend)
  Button buttonSendToFriend;
  @BindView(R.id.buttonSupport)
  Button buttonSupport;
  @BindView(R.id.buttonEmpty)
  Button buttonEmpty;
  @BindView(R.id.buttonFillCircle)
  Button buttonCircle;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @OnClick(R.id.buttonSendToFriend)
  void onClickSendToFriend(){
    Intent myIntent = new Intent(Intent.ACTION_SEND);
    myIntent.setType("text/plain");
    myIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.help_me) + "\n"
            + "https://play.google.com/store/apps/details?id=com.mindproject.mindproject&event_id=" + mEventData.id);
    startActivity(Intent.createChooser(myIntent, "Share with"));
  }
  @OnClick(R.id.textViewDescription)
  void onClickDescription(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder
            .setMessage(mEventData.description);
    AlertDialog alert = builder.create();
    alert.show();
  }
  @OnClick(R.id.buttonEmpty)
  void onClickEmpty(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder
            .setMessage(getResources().getString(R.string.you_can_not_support)
                    +" "+textViewRemainingTime.getText().toString());
    AlertDialog alert = builder.create();
    alert.show();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mIsTouched = false;
    mIsAnimated = 0;
    mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
    mAnimation.setRepeatMode(Animation.REVERSE);
    mAnimation.setRepeatCount(200);
    mPresenter = new SupportPresenter(this);
    mPresenter.onStart();
    mTimer = new Timer();
    Log.d("duration", String.valueOf(mEventData.supportDuration));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_support, container, false);
    //Toast.makeText(getContext(), mEventData.title, Toast.LENGTH_SHORT).show();
    ButterKnife.bind(this, view);
    mPresenter.isExistId(mEventData.id);
    mPresenter.downloadPhotos(mEventData.photos);
    textViewRemainingTime.setVisibility(View.VISIBLE);
    chronometer.setVisibility(View.INVISIBLE);
    AdapterTimerTask mMyTimerTask = new AdapterTimerTask();
    mTimer.schedule(mMyTimerTask, 0, 500);
    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
      @Override
      public void onChronometerTick(Chronometer chronometer) {
        long elapsedMillis = SystemClock.elapsedRealtime()
                - chronometer.getBase();
        Log.d("SEC", elapsedMillis+"");
        //progressBar.setProgress( Math.abs((int)elapsedMillis));
        if(elapsedMillis > 0) {
          chronometer.setText("0");
          mPresenter.voteForEvent(mToken, mEventData.id);
          chronometerStop();
        }
        for(int i = -60; i <= 0; i++){
          if(elapsedMillis < (i * 1000) && elapsedMillis > (i-1)*1000){
            chronometer.setText(String.valueOf(Math.abs(i-1)));
            progressBar.setProgress(60+i);
          }
        }
        //if(elapsedMillis < -5000 && elapsedMillis > -6000){
        //    chronometer.setText("6");
        //}
        //if(elapsedMillis < -4000 && elapsedMillis > -5000){
        //    chronometer.setText("5");
        //}
        //if(elapsedMillis < -3000 && elapsedMillis > -4000){
        //    chronometer.setText("4");
        //}
        //if(elapsedMillis < -2000 && elapsedMillis > -3000){
        //    chronometer.setText("3");
        //}z
        //if(elapsedMillis < -1000 && elapsedMillis > -2000){
        //    chronometer.setText("2");
        //}
        //if(elapsedMillis < 0 && elapsedMillis > -1000){
        //    chronometer.setText("1");
        //}

      }
    });
    buttonCircle.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN: // нажатие
            textViewRemainingTime.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime()+60000);
            chronometer.start();
            chronometer.setTextColor(Color.RED);
            mIsTouched = true;
            break;
          case MotionEvent.ACTION_MOVE: // движение
            break;
          case MotionEvent.ACTION_UP: // отпускание
            chronometerStop();
            mIsTouched = false;
            break;
          case MotionEvent.ACTION_CANCEL:
            chronometerStop();
            mIsTouched = false;
            break;
        }
        return true;
      }
    });
    buttonSupport.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN: // нажатие
            textViewRemainingTime.setVisibility(View.INVISIBLE);
            chronometer.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime()+60000);
            chronometer.start();
            chronometer.setTextColor(Color.RED);
            mIsTouched = true;
            break;
          case MotionEvent.ACTION_MOVE: // движение
            break;
          case MotionEvent.ACTION_UP: // отпускание
            chronometerStop();
            mIsTouched = false;
            break;
          case MotionEvent.ACTION_CANCEL:
            chronometerStop();
            mIsTouched = false;
            break;
        }
        return true;
      }
    });
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), TimeNotification.class);
        intent.putExtra("id", mEventData.id);
        intent.putExtra("description", mEventData.description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT );
        am.cancel(pendingIntent);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        if(isChecked){
          try {
            Date date = simpleDateFormat.parse(mEventData.startTime);
            if(!date.before(new Date())) {
              am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            }
          } catch (Exception c){
            c.printStackTrace();
          }
          mPresenter.addEventId(mEventData.id);
        }else{
          am.cancel(pendingIntent);
          mPresenter.deleteEventId(mEventData.id);
        }
      }
    });
    return view;
  }

  public void showData(ArrayList<Bitmap> photos){
    if(photos.size()==0){
      viewPagerPhotos.setVisibility(View.GONE);
    }else {
      PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getContext(), photos);
      viewPagerPhotos.setAdapter(photoPagerAdapter);
    }
    if(mEventData.user.username == null){
      textViewName.setVisibility(View.GONE);
    }else{
      textViewName.setText(String.valueOf(mEventData.user.username));
    }
    textViewDescription.setText(mEventData.description);
  }

  private void chronometerStop(){
    chronometer.stop();
    chronometer.setTextColor(Color.GRAY);
    progressBar.setProgress(0);
    textViewRemainingTime.setVisibility(View.VISIBLE);
    chronometer.setVisibility(View.INVISIBLE);
  }

  public void setEventData(EventData eventData){
    mEventData = eventData;
  }

  public void setToken(String token){
    mToken = token;
  }

  public void setIsReminded(boolean isReminded){
    checkBox.setChecked(isReminded);
  }

  @Override
  public void showMessage(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onStop(){
    super.onStop();
    mPresenter.onStop();
  }

  @Override
  public void onDestroy(){
    super.onDestroy();
    mTimer.cancel();



  }



  class AdapterTimerTask extends TimerTask {
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("H:mm:ss",Locale.ENGLISH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public void run() {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          try {
            if(mIsAnimated == 1){
              if(buttonCircle.getAnimation()==null) {
                buttonEmpty.setVisibility(View.GONE);
                buttonCircle.startAnimation(mAnimation);
              }
            }
            if(mIsAnimated == 2){
              if(buttonCircle.getAnimation()!=null) {
                buttonCircle.clearAnimation();
              }
            }
            Date date = simpleDateFormat.parse(mEventData.startTime);
            long difference = getTimeDifference(date);
            if(difference>0) {
              mIsAnimated = 0;
              buttonSupport.setEnabled(false);
              textViewRemainingTime.setText(String.format("%02d:%02d:%02d", difference / 1000 / 3600, difference / 1000 / 60 % 60, difference / 1000 % 60));
              buttonSupport.setText(getResources().getString(R.string.not_active));
              buttonEmpty.setVisibility(View.VISIBLE);
              buttonCircle.setVisibility(View.INVISIBLE);
              buttonCircle.setEnabled(false);
            }else {
              if (difference > -300*1000) {
                mIsAnimated = 1;
                textViewRemainingTime.setText(getResources().getString(R.string.you_can_vote));
                buttonSupport.setEnabled(true);
                buttonSupport.setText(getResources().getString(R.string.support));
                buttonCircle.setVisibility(View.VISIBLE);
                buttonCircle.setEnabled(true);
              }else{
                if(!mIsTouched) {
                  mIsAnimated = 2;
                  buttonSupport.setEnabled(false);
                  buttonSupport.setVisibility(View.GONE);
                  textViewRemainingTime.setText(getResources().getString(R.string.you_late));
                  buttonCircle.setVisibility(View.INVISIBLE);
                  buttonCircle.setEnabled(false);
                }
              }
            }
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      });
    }

    private long getTimeDifference(Date date){
      TimeZone timeZone = TimeZone.getDefault();
      Calendar today = Calendar.getInstance(timeZone);
      Date currentDate = today.getTime();
      long differenceLong = date.getTime() - currentDate.getTime();
      return differenceLong;
    }
  }
}