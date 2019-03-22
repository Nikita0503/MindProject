package com.mindproject.mindproject.support;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
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

/**
 * Created by Nikita on 07.03.2019.
 */

public class SupportFragment extends Fragment implements BaseContract.BaseView {

    private boolean isTouched;
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
    @BindView(R.id.buttonFillCircle)
    Button buttonCircle;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @OnClick(R.id.buttonSendToFriend)
    void onClickSendToFriend(){
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.galCalApp&event_id=" + mEventData.id);
        startActivity(Intent.createChooser(myIntent, "Share with"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTouched = false;
        mPresenter = new SupportPresenter(this);
        mPresenter.onStart();
        mTimer = new Timer();
        Log.d("duration", String.valueOf(mEventData.supportDuration));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_support, container, false);
        Toast.makeText(getContext(), mEventData.title, Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this, view);
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
                if(elapsedMillis < -5000 && elapsedMillis > -6000){
                    chronometer.setText("6");
                }
                if(elapsedMillis < -4000 && elapsedMillis > -5000){
                    chronometer.setText("5");
                }
                if(elapsedMillis < -3000 && elapsedMillis > -4000){
                    chronometer.setText("4");
                }
                if(elapsedMillis < -2000 && elapsedMillis > -3000){
                    chronometer.setText("3");
                }
                if(elapsedMillis < -1000 && elapsedMillis > -2000){
                    chronometer.setText("2");
                }
                if(elapsedMillis < 0 && elapsedMillis > -1000){
                    chronometer.setText("1");
                }
                if(elapsedMillis > 0) {
                    chronometer.setText("0");
                    String strElapsedMillis = "Прошло больше 5 секунд";
                    mPresenter.voteForEvent(mToken, mEventData.id);
                    chronometerStop();
                }
            }
        });
        showMessage(mEventData.id+"");
        buttonCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        textViewRemainingTime.setVisibility(View.INVISIBLE);
                        chronometer.setVisibility(View.VISIBLE);
                        chronometer.setBase(SystemClock.elapsedRealtime()+5000);
                        chronometer.start();
                        chronometer.setTextColor(Color.RED);
                        isTouched = true;
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        chronometerStop();
                        isTouched = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        chronometerStop();
                        isTouched = false;
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
                        chronometer.setBase(SystemClock.elapsedRealtime()+5000);
                        chronometer.start();
                        chronometer.setTextColor(Color.RED);
                        isTouched = true;
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        chronometerStop();
                        isTouched = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        chronometerStop();
                        isTouched = false;
                        break;
                }
                return true;
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
        progressBar.setProgress(-5000);
        textViewRemainingTime.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.INVISIBLE);
    }

    public void setEventData(EventData eventData){
        mEventData = eventData;
    }

    public void setToken(String token){
        mToken = token;
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
                        Date date = simpleDateFormat.parse(mEventData.startTime);
                        Date difference = getTimeDifference(date);
                        if((difference.getTime()/1000)<300){
                            buttonSupport.setEnabled(false);
                            buttonSupport.setBackground(getResources().getDrawable(R.drawable.rect_red));
                            buttonSupport.setText("Not active");
                            buttonCircle.setVisibility(View.INVISIBLE);
                            buttonCircle.setEnabled(false);
                        }
                        textViewRemainingTime.setText(eventDateFormat.format(difference));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private Date getTimeDifference(Date date){
            TimeZone timeZone = TimeZone.getDefault();
            int timeZoneInt = timeZone.getRawOffset()/3600000;
            //mEventDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Calendar today = Calendar.getInstance();
            today.add(Calendar.HOUR_OF_DAY, timeZoneInt);
            Date currentDate = today.getTime();
            //Log.d("TAG", currentDate.getTime()+"");
            long differenceLong = date.getTime() - currentDate.getTime();
            return new Date(differenceLong);
        }
    }
}
