package com.mindproject.mindproject.add_request;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nikita on 18.03.2019.
 */

public class AddRequestFragment extends Fragment implements BaseContract.BaseView {

    public static final int GALLERY_REQUEST = 1;
    public static final int MAKE_A_PHOTO = 0;
    private String mToken;
    private AddRequestPresenter mPresenter;
    private PhotosAdapter mAdapter;

    @BindView(R.id.buttonSend)
    Button buttonSend;
    @BindView(R.id.datePicker)
    MaterialCalendarView datePicker;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    @BindView(R.id.extended_edit_text_description)
    ExtendedEditText editTextDescription;
    @BindView(R.id.recycler_view_photos)
    RecyclerView recyclerViewPhotos;
    @BindView(R.id.timePicker)
    RecyclerView recyclerViewtimePicker;
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
        if(recyclerViewtimePicker.getVisibility()==View.GONE) {
            recyclerViewtimePicker.setVisibility(View.VISIBLE);
        }else{
            recyclerViewtimePicker.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.buttonAddPhotos)
    void onClickAddPhotos(){
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
        String title = "EventWithoutTitle";
        Log.d("description", description);
        Log.d("title", title);
        String date = textViewDate.getText().toString();
        String time = textViewTime.getText().toString();
        if(mAdapter.getFileList().size()<=5) {
            if(!title.equals("")) {
                if(!description.equals("")) {
                    if(!date.equals("Pick date")) {
                        if(!time.equals("Pick time")) {
                            startLoading();
                            mPresenter.generateData(mToken, title, description, date, time, mAdapter.getFileList());
                        }else{
                            showMessage("Select time, please");
                            return;
                        }
                    }else {
                        showMessage("Select date, please");
                        return;
                    }
                }else{
                    showMessage("The description must not be empty");
                    return;
                }
            }else{
                showMessage("The title must not be empty");
                return;
            }
        }else{
            showMessage("Number of photos should not be more than 5");
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("token", mToken);
        mPresenter = new AddRequestPresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_request, container, false);
        ButterKnife.bind(this, view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        datePicker.state().edit().setMinimumDate(CalendarDay.today().getDate()).commit();
        datePicker.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d M yyyy HH", Locale.ENGLISH);
                    Date date = dateFormat.parse(calendarDay.getDay() + " " + calendarDay.getMonth() + " " + calendarDay.getYear() + " 12");
                    Log.d("time_day", calendarDay.getDay() + " " + calendarDay.getMonth() + " " + calendarDay.getYear());
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    textViewDate.setText(newFormat.format(date));
                    datePicker.setVisibility(View.GONE);
                    textViewTime.setText("Pick time");
                    textViewTime.setVisibility(View.GONE);
                    mPresenter.fetchForDateEvents(mToken, date);
                    rotateLoading.start();
                }catch (Exception c){
                    c.printStackTrace();
                }
            }
        });

        mAdapter = new PhotosAdapter(this);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case GALLERY_REQUEST:
                    if(resultCode == RESULT_OK){
                        try {
                            Uri selectedImage = imageReturnedIntent.getData();
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            //Bitmap bitmap = Picasso
                            //        .with(getContext())
                            //        .load(selectedImage)
                            //        .get();
                            //mAdapter.addPhotos(selectedImage);
                            File file = new File(getRealPathFromUri(getContext(), selectedImage));
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
        }
    }

    public void setToken(String token){
        mToken = token;
    }

    public void setTime(String time){
        textViewTime.setText(time);
        recyclerViewtimePicker.setVisibility(View.GONE);
    }

    public void setAvailableTime(ArrayList<Integer> list){
        textViewTime.setVisibility(View.VISIBLE);
        TimeAdapter timeAdapter = new TimeAdapter(list, this);
        recyclerViewtimePicker.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewtimePicker.setAdapter(timeAdapter);
        rotateLoading.stop();
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
