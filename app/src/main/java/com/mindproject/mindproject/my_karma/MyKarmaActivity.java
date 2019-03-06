package com.mindproject.mindproject.my_karma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mindproject.mindproject.add_request.AddRequestActivity;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.edit_profile.EditProfileActivity;

public class MyKarmaActivity extends AppCompatActivity {

    private String mDeviceId;
    private String mToken;
    private MyKarmaPresenter mPresenter;

    @OnClick(R.id.buttonEdit)
    void onClickEdit(){
        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
        intent.putExtra("id", mDeviceId);
        intent.putExtra("token", mToken);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to edit activity", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonSend)
    void onClickAdd(){
        Intent intent = new Intent(getApplicationContext(), AddRequestActivity.class);
        intent.putExtra("token", mToken);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to send activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_karma);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mDeviceId = intent.getStringExtra("id");
        mToken = intent.getStringExtra("token");
        Log.d("deviceId", mDeviceId);
        Log.d("token", mToken);
        mPresenter = new MyKarmaPresenter(this);
    }
}
