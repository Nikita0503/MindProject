package com.mindproject.mindproject.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.add_request.AddRequestFragment;
import com.mindproject.mindproject.edit_profile.EditProfileFragment;
import com.mindproject.mindproject.model.data.EventDataForEventList;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mindproject.mindproject.my_karma.MyKarmaActivity;
import com.mindproject.mindproject.my_requests.MyRequestsFragment;
import com.victor.loading.rotate.RotateLoading;

public class MainActivity extends AppCompatActivity implements BaseContract.BaseView {

    private String mDeviceId;
    private String mToken;
    private MainPresenter mPresenter;
    private Fragment mFragment;
    private ListFragment mListFragment;
    private AddRequestFragment mAddRequestFragment;
    private EditProfileFragment mEditProfileFragment;
    private MyRequestsFragment mMyRequestFragment;
    private FragmentManager mFragmentManager;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        bottomNavigation.setVisibility(View.INVISIBLE);
        initNavigationView();
        mPresenter = new MainPresenter(this);
        mPresenter.onStart();
        mPresenter.fetchToken(mDeviceId);
        mFragmentManager = getSupportFragmentManager();
        mListFragment = new ListFragment();
        mAddRequestFragment = new AddRequestFragment();
        mEditProfileFragment = new EditProfileFragment();
        mMyRequestFragment = new MyRequestsFragment();
        rotateLoading.start();
        Log.d("DEVICE_ID", mDeviceId);

    }

    private void initNavigationView(){
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.eventsFragment:
                        mFragment = mListFragment;
                        break;
                    case R.id.addEventFragment:
                        mFragment = mAddRequestFragment;
                        break;
                    case R.id.profileFragment:
                        mFragment = mEditProfileFragment;
                        break;
                    case R.id.archiveFragment:
                        mFragment = mMyRequestFragment;
                        break;
                }
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, mFragment).commit();
                transaction.addToBackStack(null);
                return true;
            }
        });
    }

    public void setToken(String token){
        mToken = token;
        mListFragment.setDeviceId(mDeviceId);
        mAddRequestFragment.setToken(mToken);
        mEditProfileFragment.setDeviceId(mDeviceId);
        mMyRequestFragment.setDeviceId(mDeviceId);
        bottomNavigation.setVisibility(View.VISIBLE);
        rotateLoading.stop();
        bottomNavigation.setSelectedItemId(R.id.eventsFragment);
    }

    public void updateKarmaPoints(){
        mPresenter.fetchToken(mDeviceId);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
