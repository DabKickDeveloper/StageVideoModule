package com.dabkick.videosdk.usersetup;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dabkick.videosdk.R;

import static android.app.Activity.RESULT_OK;

public class GetUserDetails extends DialogFragment{

        ImageView cameraImage;
        Button continueBtn;
        EditText nameTxt;
        DabkickGetUserDetails userDetailListener;
        Bitmap userImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.get_user_details_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return  view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraImage = view.findViewById(R.id.camera_img);
        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Call intent to open camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        continueBtn = view.findViewById(R.id.cont_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if listener is set
                if(userDetailListener != null){
                    //Chek if user has set image
                    if(userImage != null)
                        userDetailListener.getUserDetails(nameTxt.getText().toString(), userImage);
                    else{

                        //Create a dummy bitmap if user has not set image
                        Bitmap bmp = Bitmap.createBitmap(cameraImage.getWidth(), cameraImage.getHeight(), Bitmap.Config.ARGB_8888);
                        userImage = bmp;
                        userDetailListener.getUserDetails(nameTxt.getText().toString(), userImage);

                    }
                }
                dismiss();
            }
        });

        nameTxt = view.findViewById(R.id.name_txt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //get image from the intent
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userImage = imageBitmap;
            cameraImage.setImageBitmap(imageBitmap);
        }
    }

    public void setUserDetailListener(DabkickGetUserDetails listner){

        this.userDetailListener = listner;

    }

    @Override
    public void onPause() {
        super.onPause();

        if(nameTxt != null) {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(nameTxt.getWindowToken(), 0);

        }
    }

    //Listener to return the name and bitmap collected from the user
    public interface DabkickGetUserDetails{

        public void getUserDetails(String name, Bitmap userImage);

    }

}
