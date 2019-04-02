package com.mindproject.mindproject.past_event;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.mindproject.mindproject.support.PhotoPagerAdapter;
import com.mindproject.mindproject.support.SupportFragment;
import com.mindproject.mindproject.support.SupportPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikita on 18.03.2019.
 */

public class PastEventFragment extends Fragment implements BaseContract.BaseView {

    private String mToken;
    private EventData mEventData;
    private PastEventPresenter mPresenter;

    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewVotes)
    TextView textViewVotesCount;
    @BindView(R.id.textViewName)
    TextView textViewTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPagerPhotos;
    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new PastEventPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_event, container, false);
        Toast.makeText(getContext(), mEventData.title, Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this, view);
        mPresenter.downloadPhotos(mEventData.photos);
        return view;
    }

    public void showData(ArrayList<Bitmap> photos){
        if(photos.size()==0){
            viewPagerPhotos.setVisibility(View.GONE);
        }else {
            PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(getContext(), photos);
            viewPagerPhotos.setAdapter(photoPagerAdapter);
        }
        textViewTitle.setText(String.valueOf(mEventData.user.username));
        textViewDescription.setText(mEventData.description);
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            SimpleDateFormat showFormat = new SimpleDateFormat("yyy MMM dd',' HH:mm", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(mEventData.startTime);
            textViewDate.setText(showFormat.format(date));
        }catch (Exception c){
            c.printStackTrace();
        }
        textViewVotesCount.setText(String.valueOf(mEventData.votedCount) + " " + getResources().getString(R.string.votes));
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
}