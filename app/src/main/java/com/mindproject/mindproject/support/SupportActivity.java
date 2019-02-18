package com.mindproject.mindproject.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SupportActivity extends AppCompatActivity implements BaseContract.BaseView{

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTouched = false;
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_user));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_send));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive));
        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getApplicationContext(), bitmaps);
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
                    Toast.makeText(getApplicationContext(),
                            strElapsedMillis, Toast.LENGTH_SHORT)
                            .show();
                    chronometerStop();
                }
            }
        });











        /*CountDownTimer cdt = new CountDownTimer(1 * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int total = (int) ((timePassed/ 60) * 100);
                progressBar.setProgress(total);
            }

            public void onFinish() {
                // DO something when 1 minute is up
            }
        }.start();*/



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
    }

    private void chronometerStop(){
        chronometer.stop();
        chronometer.setTextColor(Color.GRAY);
        chronometer.setText("00:05");
        progressBar.setProgress(-6000);
    }





}
