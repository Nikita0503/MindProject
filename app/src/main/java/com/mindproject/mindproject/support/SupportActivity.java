package com.mindproject.mindproject.support;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mindproject.mindproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportActivity extends AppCompatActivity {

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @BindView(R.id.viewPager)
    ViewPager viewPagerPhotos;

    @BindView(R.id.textViewDescription)
    TextView textViewDescription;

    @BindView(R.id.textViewTimer)
    TextView textViewTimer;

    @BindView(R.id.buttonSendToFriend)
    Button buttonSendToFriend;

    @BindView(R.id.buttonSupport)
    Button buttonSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
    }
}
