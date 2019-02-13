package com.mindproject.mindproject;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRequestActivity extends AppCompatActivity {

    PhotosAdapter mAdapter;

    @BindView(R.id.datePicker)
    MaterialCalendarView datePicker;

    @BindView(R.id.timePicker)
    TimePicker timePicker;

    @BindView(R.id.textViewDate)
    TextView textViewDate;

    @BindView(R.id.textViewTime)
    TextView textViewTime;

    @BindView(R.id.recycler_view_photos)
    RecyclerView recyclerViewPhotos;

    @OnClick(R.id.textViewDate)
    void onClickDate(){
        if(datePicker.getVisibility()==View.GONE) {
            datePicker.setVisibility(View.VISIBLE);
        }else{
            datePicker.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.textViewTime)
    void onClickTime(){
        if(timePicker.getVisibility()==View.GONE) {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            if(minute<10) {
                textViewTime.setText(hour + ":0" + minute);
            }else{
                textViewTime.setText(hour + ":" + minute);
            }
            timePicker.setVisibility(View.VISIBLE);
        }else{
            timePicker.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.buttonAddPhotos)
    void onClickAddPhotos(){
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        ButterKnife.bind(this);
        datePicker.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d M yyyy", Locale.ENGLISH);
                    Date date = dateFormat.parse(calendarDay.getDay() + " " + calendarDay.getMonth() + " " + calendarDay.getYear());
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    textViewDate.setText(newFormat.format(date));
                    datePicker.setVisibility(View.GONE);
                }catch (Exception c){
                    c.printStackTrace();
                }
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(minute<10) {
                    textViewTime.setText(hourOfDay + ":0" + minute);
                }else{
                    textViewTime.setText(hourOfDay + ":" + minute);
                }
            }
        });
        mAdapter = new PhotosAdapter(this);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setAdapter(mAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            ClipData clipData = imageReturnedIntent.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    try {
                        Uri uri = clipData.getItemAt(i).getUri();
                        mAdapter.addPhotos(uri);
                    } catch (Exception c) {
                        c.printStackTrace();
                    }
                }
            } else {
                Uri selectedImage = imageReturnedIntent.getData();
                mAdapter.addPhotos(selectedImage);
            }
        }
    }

}
