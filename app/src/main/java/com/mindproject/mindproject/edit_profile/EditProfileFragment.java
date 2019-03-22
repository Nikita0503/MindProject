package com.mindproject.mindproject.edit_profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mindproject.mindproject.BaseContract;
import com.mindproject.mindproject.R;
import com.victor.loading.rotate.RotateLoading;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nikita on 18.03.2019.
 */

public class EditProfileFragment extends Fragment implements BaseContract.BaseView{

    static final int GALLERY_REQUEST = 1;
    private String mDeviceId;
    private String mToken;
    private Uri mImageURL;
    private EditProfilePresenter mPresenter;

    @BindView(R.id.text_field_boxes_name)
    TextFieldBoxes textFieldBoxesName;
    @BindView(R.id.text_field_boxes_phone)
    TextFieldBoxes textFieldBoxesPhone;
    @BindView(R.id.text_field_boxes_email)
    TextFieldBoxes textFieldBoxesEmail;
    @BindView(R.id.extended_edit_text_name)
    ExtendedEditText extendedEditTextName;
    @BindView(R.id.extended_edit_text_phone)
    ExtendedEditText extendedEditTextPhone;
    @BindView(R.id.extended_edit_text_email)
    ExtendedEditText extendedEditTextEmail;
    @BindView(R.id.imageViewUser)
    ImageView imageViewUser;
    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
    @BindView(R.id.buttonEdit)
    Button buttonEdit;
    @OnClick(R.id.buttonEdit)
    void onClickEdit(){
        String username = extendedEditTextName.getText().toString();
        String phone = extendedEditTextPhone.getText().toString();
        String email = extendedEditTextEmail.getText().toString();
        boolean validateName = validateName(username);
        boolean validateEmail = validateEmail(email);
        boolean validatePhone = validatePhone(phone);
        if(validateName){
            if(validatePhone){
                if(validateEmail){
                    mPresenter.changeUsernameAndPhone(mToken, username, phone);
                    mPresenter.changeEmail(mToken, email);
                    mPresenter.changeAvatar(mToken, mImageURL);
                    startLoading();
                }else{
                    showMessage("Not correct email");
                }
            }else{
                showMessage("Not correct phone");
            }
        }else{
            showMessage("Not correct name");
        }
    }

    @OnClick(R.id.imageViewUser)
    void onClick(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new EditProfilePresenter(this);
        mPresenter.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        ButterKnife.bind(this, view);
        mPresenter.fetchUserData(mDeviceId);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
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

    public void setToken(String token){
        mToken = token;
    }

    public void setDeviceId(String deviceId){
        mDeviceId = deviceId;
    }

    public void setPhoto(Bitmap bitmap){
        imageViewUser.setImageBitmap(bitmap);
    }

    public void setName(String name){
        extendedEditTextName.setText(name);
    }

    public void setPhone(String phone){
        extendedEditTextPhone.setText(phone);
    }

    public void setEmail(String email){
        extendedEditTextEmail.setText(email);
    }

    private boolean validateEmail(String emailStr) {
        if(emailStr.length()==0){
            return true;
        }
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private boolean validatePhone(String phone){
        if(phone.length() == 0){
            return true;
        }
        if(phone.startsWith("+")){
            return true;
        }else{
            return false;
        }
    }

    private boolean validateName(String name){
        if(name.length()==0){
            return true;
        }
        if(name.length()>3 && name.length() < 128){
            return true;
        }else{
            return false;
        }
    }

    public void startLoading(){
        textFieldBoxesName.setEnabled(false);
        textFieldBoxesPhone.setEnabled(false);
        textFieldBoxesEmail.setEnabled(false);
        buttonEdit.setEnabled(false);
        imageViewUser.setEnabled(false);
        rotateLoading.start();
    }

    public void stopLoading(){
        textFieldBoxesName.setEnabled(true);
        textFieldBoxesPhone.setEnabled(true);
        textFieldBoxesEmail.setEnabled(true);
        buttonEdit.setEnabled(true);
        imageViewUser.setEnabled(true);
        rotateLoading.stop();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();
        mPresenter.onStop();
    }
}
