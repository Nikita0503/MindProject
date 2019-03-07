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

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.model.data.EventData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 07.03.2019.
 */

public class SupportFragment extends Fragment implements BaseContract.BaseView {

    private boolean isTouched;
    private String mToken;
    private EventData mEventData;
    private SupportPresenter mPresenter;

    @BindView(R.id.textViewName)
    TextView textViewTitle;

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
        mPresenter = new SupportPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_support, container, false);
        Toast.makeText(getContext(), mEventData.title, Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this, view);
        mPresenter.downloadPhotos(mEventData.photos);
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
                    //Toast.makeText(getContext(),
                    //        strElapsedMillis, Toast.LENGTH_SHORT)
                    //        .show();
                    mPresenter.voteForEvent(mToken, mEventData.id);
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
        textViewTitle.setText(mEventData.title);
        textViewDescription.setText(mEventData.description);
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

    public void setToken(String token){
        mToken = token;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
