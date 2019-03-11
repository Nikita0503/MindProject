package com.mindproject.mindproject.edit_profile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class EditProfileActivity extends AppCompatActivity implements BaseContract.BaseView {

    static final int GALLERY_REQUEST = 1;
    private String mDeviceId;
    private String mToken;
    private Uri mImageURL;
    private EditProfilePresenter mPresenter;

    @BindView(R.id.extended_edit_text_name)
    ExtendedEditText extendedEditTextName;
    @BindView(R.id.extended_edit_text_phone)
    ExtendedEditText extendedEditTextPhone;
    @BindView(R.id.extended_edit_text_email)
    ExtendedEditText extendedEditTextEmail;
    @BindView(R.id.imageViewUser)
    ImageView imageViewUser;
    @OnClick(R.id.buttonEdit)
    void onClickEdit(){
        String username = extendedEditTextName.getText().toString();
        String phone = extendedEditTextPhone.getText().toString();
        mPresenter.changeUsernameAndPhone(mToken, username, phone);
        mPresenter.changeAvatar(mToken, mImageURL);
    }

    @OnClick(R.id.imageViewUser)
    void onClick(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        mPresenter = new EditProfilePresenter(this);
        mPresenter.onStart();
        Intent intent = getIntent();
        mDeviceId = intent.getStringExtra("id");
        mToken = intent.getStringExtra("token");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageViewUser.setImageURI(selectedImage);
                    mImageURL = selectedImage;
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
