package com.mindproject.mindproject.add_request;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.mindproject.mindproject.add_request.PhotosAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class AddRequestActivity extends AppCompatActivity implements BaseContract.BaseView{
    public static final int GALLERY_REQUEST = 1;
    public static final int MAKE_A_PHOTO = 0;
    private String mToken;
    private AddRequestPresenter mPresenter;
    private PhotosAdapter mAdapter;

    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.datePicker)
    MaterialCalendarView datePicker;
    @BindView(R.id.timePicker)
    NumberPicker timePicker;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    //@BindView(R.id.extended_edit_text_description)
    ExtendedEditText editTextDescription;
    @BindView(R.id.recycler_view_photos)
    RecyclerView recyclerViewPhotos;
    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
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
        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @OnClick(R.id.buttonMakePhoto)
    void onClickMakeAPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MAKE_A_PHOTO);
    }

    @OnClick(R.id.buttonSend)
    void onClickSendRequest(){
        String description = editTextDescription.getText().toString();
        String title = "eventWithoutTitle";
        Log.d("description", description);
        Log.d("title", title);
        String date = textViewDate.getText().toString();
        String time = textViewTime.getText().toString();
        if(mAdapter.getFileList().size()<=5) {
            if(!title.equals("")) {
                if(!description.equals("")) {
                    if(!date.equals(getResources().getString(R.string.pick_date))) {
                        if(!date.equals(getResources().getString(R.string.pick_time))) {
                            startLoading();
                            mPresenter.generateData(mToken, title, description, date, time, mAdapter.getFileList());
                        }else{
                            showMessage(getResources().getString(R.string.select_time));
                            return;
                        }
                    }else {
                        showMessage(getResources().getString(R.string.select_date));
                        return;
                    }
                }else{
                    showMessage(getResources().getString(R.string.enter_description));
                    return;
                }
            }else{
                showMessage(getResources().getString(R.string.enter_title));
                return;
            }
        }else{
            showMessage(getResources().getString(R.string.photo_count));
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        Log.d("token", mToken);
        //mPresenter = new AddRequestPresenter(this);
        mPresenter.onStart();
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
        timePicker.setMaxValue(23);
        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textViewTime.setText(picker.getValue() + ":00");
            }
        });
        //mAdapter = new PhotosAdapter(this);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setAdapter(mAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case GALLERY_REQUEST:
                    if(resultCode == RESULT_OK){
                        try {
                            Uri selectedImage = imageReturnedIntent.getData();
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //mAdapter.addPhotos(bitmap);
                            File file = new File(getRealPathFromUri(getApplicationContext(), selectedImage));
                            mAdapter.addFile(file);
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }
            }
            if(requestCode==MAKE_A_PHOTO){
                if (imageReturnedIntent == null) {
                } else {
                    Bundle bndl = imageReturnedIntent.getExtras();
                    if (bndl != null) {
                        Object obj = imageReturnedIntent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            try {
                                Bitmap bitmap = (Bitmap) obj;
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/saved_images");
                                myDir.mkdirs();

                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String fname = "Shutta_"+ timeStamp +".jpg";

                                File file = new File(myDir, fname);
                                if (file.exists()) file.delete ();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //mAdapter.addPhotos(bitmap);
                                mAdapter.addFile(file);
                            }catch (Exception c){
                                c.printStackTrace();
                            }
                        }
                    }
                }
            }
            //if(requestCode==ADD_PHOTOS) { File file = new File(getRealPathFromUri(mActivity.getApplicationContext(), uriList.get(i)));
            //    ClipData clipData = imageReturnedIntent.getClipData();
            //    if (clipData != null) {
            //        for (int i = 0; i < clipData.getItemCount(); i++) {
            //            try {
            //                Log.d("TAG", clipData.getItemAt(i).getUri().getPath());
            //                Uri uri = clipData.getItemAt(i).getUri();
            //                mUriList.add(uri);
            //                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            //                mAdapter.addPhotos(bitmap);
            //            } catch (Exception c) {
            //                c.printStackTrace();
            //            }
            //        }
            //    } else {
            //        try {
            //            Uri selectedImage = imageReturnedIntent.getData();
            //            Log.d("TAG", selectedImage.getPath());
            //            mUriList.add(selectedImage);
            //            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            //            mAdapter.addPhotos(bitmap);
            //        } catch (Exception c) {
            //            c.printStackTrace();
            //        }
            //    }
            //}
        }
    }



    public void startLoading(){
        rotateLoading.start();
        buttonSend.setEnabled(false);
    }

    public void stopLoading(){
        rotateLoading.stop();
        buttonSend.setEnabled(true);
    }

    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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