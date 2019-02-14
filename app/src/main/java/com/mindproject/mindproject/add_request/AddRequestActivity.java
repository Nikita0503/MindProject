package com.mindproject.mindproject.add_request;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mindproject.mindproject.R;
import com.mindproject.mindproject.add_request.PhotosAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddRequestActivity extends AppCompatActivity {

    public static final int MAKE_A_PHOTO = 0;
    public static final int ADD_PHOTOS = 1;
    private PhotosAdapter mAdapter;

    @BindView(R.id.datePicker)
    MaterialCalendarView datePicker;

    @BindView(R.id.timePicker)
    NumberPicker timePicker;

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
            textViewTime.setText(hour + ":00");
            timePicker.setVisibility(View.VISIBLE);
        }else{
            timePicker.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.buttonAddPhotos)
    void onClickAddPhotos(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @OnClick(R.id.buttonMakePhoto)
    void onClickMakeAPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
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
        timePicker.setMinValue(0);
        timePicker.setMaxValue(24);
        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textViewTime.setText(picker.getValue() + ":00");
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
            if(requestCode==MAKE_A_PHOTO){
                if (imageReturnedIntent == null) {
                } else {
                    Bundle bndl = imageReturnedIntent.getExtras();
                    if (bndl != null) {
                        Object obj = imageReturnedIntent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            mAdapter.addPhotos(bitmap);
                        }
                    }
                }
            }
            if(requestCode==ADD_PHOTOS) {
                ClipData clipData = imageReturnedIntent.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        try {
                            Uri uri = clipData.getItemAt(i).getUri();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            mAdapter.addPhotos(bitmap);
                        } catch (Exception c) {
                            c.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        mAdapter.addPhotos(bitmap);
                    } catch (Exception c) {
                        c.printStackTrace();
                    }
                }
            }
        }
    }
}
