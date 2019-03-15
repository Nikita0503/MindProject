package com.mindproject.mindproject.my_karma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.add_request.AddRequestActivity;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.edit_profile.EditProfileActivity;
import com.mindproject.mindproject.model.data.UserData;
import com.mindproject.mindproject.my_requests.MyRequestsActivity;

public class MyKarmaActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mDeviceId;
    private String mToken;
    private UserData mUserData;
    private MyKarmaPresenter mPresenter;

    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoints;

    @OnClick(R.id.buttonEdit)
    void onClickEdit(){
        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
        intent.putExtra("token", mToken);
        intent.putExtra("deviceId", mDeviceId);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to edit activity", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonArchive)
    void onClickMyRequests(){
        Intent intent = new Intent(getApplicationContext(), MyRequestsActivity.class);
        intent.putExtra("token", mToken);
        intent.putExtra("deviceId", mDeviceId);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to requests activity", Toast.LENGTH_SHORT).show();
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
        mToken = intent.getStringExtra("token");
        mDeviceId = intent.getStringExtra("deviceId");
        Log.d("token", mToken);
        mPresenter = new MyKarmaPresenter(this);
        mPresenter.onStart();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mPresenter.fetchUserData(mDeviceId);
    }

    public void setUserData(UserData userData){
        mUserData = userData;
        textViewKarmaPoints.setText("Ваша карма " + userData.karma);
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
