package com.mindproject.mindproject.support;

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

import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 07.03.2019.
 */

public class SupportFragment extends Fragment {

    EventData mEventData;

    private boolean isTouched;

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

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter = new CalendarPresenter(this);
        //mPresenter.onStart();
        isTouched = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_support, container, false);
        Toast.makeText(getContext(), mEventData.title, Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this, view);
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_user));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_send));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive));
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getContext(), bitmaps);
        viewPagerPhotos.setAdapter(photoPagerAdapter);
        chronometer.setCountDown(true);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime()
                        - chronometer.getBase();
                Log.d("SEC", elapsedMillis+"");
                progressBar.setProgress((int) elapsedMillis);
                if (elapsedMillis > 0) {
                    String strElapsedMillis = "Прошло больше 5 секунд";
                    Toast.makeText(getContext(),
                            strElapsedMillis, Toast.LENGTH_SHORT)
                            .show();
                    chronometerStop();
                }
            }
        });

        buttonSupport.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        chronometer.setBase(SystemClock.elapsedRealtime()+6000);
                        chronometer.start();
                        chronometer.setTextColor(Color.RED);
                        isTouched = true;

                        /*Observable.interval(1,TimeUnit.SECONDS, Schedulers.io())
                                .take(100)
                                .map(v -> 100 - v)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Long value) {
                                        if(isTouched){
                                            progressBar.setProgress(Math.toIntExact(value));
                                        }else{
                                            onComplete();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onComplete() {
                                        progressBar.setProgress(0);
                                    }
                                });*/
                        /*countDownTimer = new CountDownTimer(10000, 100) {
                            public void onTick(long millisUntilFinished) {
                                progressBar.setProgress(Math.abs((int) millisUntilFinished / 100 - 100));
                            }

                            @Override
                            public void onFinish() {

                            }
                        };*/


                        /*Observable.interval(50000,TimeUnit.MICROSECONDS, Schedulers.io())
                                .take(100)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Long value) {
                                        Log.d("TAG2", value+"");
                                        if(isTouched) {
                                            progressBar.setProgress(Math.toIntExact(value));
                                        }else{
                                            onComplete();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        progressBar.setProgress(0);
                                    }
                                });*/


                        Log.d("TAG", "DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        Log.d("TAG", "MOVE");
                        //progressBar.setProgress((int) mSecondsRemaining);
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        Log.d("TAG", "UP");
                        chronometerStop();
                        isTouched = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("TAG", "CANCEL");
                        chronometerStop();
                        isTouched = false;
                        break;
                }
                return true;
            }
        });


        return view;
    }

    private void chronometerStop(){
        chronometer.stop();
        chronometer.setTextColor(Color.GRAY);
        chronometer.setText("00:05");
        progressBar.setProgress(-6000);
    }

    public void setEventData(EventData eventData){
        mEventData = eventData;
    }
}
