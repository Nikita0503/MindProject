package com.mindproject.mindproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.data.EventData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private EventListAdapter mEventAdapter;

    @BindView(R.id.textViewKarmaPoint)
    TextView textViewKarmaPoint;

    @BindView(R.id.recyclerViewEvents)
    RecyclerView recyclerViewEvents;

    @OnClick(R.id.textViewKarmaPoint)
    void onClick(){
        Intent intent = new Intent(getApplicationContext(), MyKarmaActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Welcome to karma activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mEventAdapter = new EventListAdapter(this);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewEvents.setAdapter(mEventAdapter);
        mEventAdapter.addEvents(initData());
    }

    private ArrayList<EventData> initData(){
        Drawable drawable = getDrawable(R.drawable.ic_launcher_background);
        ArrayList<EventData> events = new ArrayList<EventData>();
        for (int i = 0; i < 10; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, i);
            EventData data = new EventData("Event"+i, calendar.getTime(), drawable);
            events.add(data);
        }
        return events;
    }

    private void telephoneNumber(){
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String mPhoneNumber = tMgr.getLine1Number();
        String simID = tMgr.getSimSerialNumber();
        Log.d("TAG", mPhoneNumber);
        Log.d("TAG", simID);
    }
}
