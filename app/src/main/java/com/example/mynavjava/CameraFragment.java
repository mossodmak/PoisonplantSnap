package com.example.mynavjava;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {

    //From Peepject
    private TextView mTextMessage;
    private static final int INPUT_SIZE = 224;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 200;
    private static final int GALLERY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private Handler handler = new Handler();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Button btn_captureImage;
    private Button btn_gallery;
    private Button button_result;
    private ImageView imageView1;
    private TextView textViewResult1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        btn_gallery = view.findViewById(R.id.btn_gallery);
        btn_captureImage = view.findViewById(R.id.btn_captureImage);

        if(checkPermission() == false){
            verifyPermission();
        }

        btn_captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraView = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraView, CAMERA_REQUEST_CODE);

            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent galleryView = new Intent(Intent.ACTION_PICK);
                galleryView.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryView
                        , "Select photo from"),GALLERY_REQUEST_CODE);
            }
        });

        return  view;
    }
    private boolean checkPermission(){
        String permission = Manifest.permission.CAMERA;
        if(ContextCompat.checkSelfPermission(getContext(),permission) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    private void verifyPermission(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_CAMERA_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * return list of classifier
         * name resultsCamera and resultGallery
         */

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmapCamera = (Bitmap) extras.get("data");
            imageBitmapCamera = Bitmap.createScaledBitmap(imageBitmapCamera, INPUT_SIZE, INPUT_SIZE, false);


            Intent intent = new Intent(getContext(), ResultFragment.class);
            intent.putExtra("photo", imageBitmapCamera);
            startActivity(intent);


        }
        else if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri pickedImage = data.getData();
            try {
                Bitmap imageBitmapGallery = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), pickedImage);
                imageBitmapGallery = Bitmap.createScaledBitmap(imageBitmapGallery, INPUT_SIZE, INPUT_SIZE, false);
                imageView1.setImageBitmap(imageBitmapGallery);
                Intent intent = new Intent(getContext(), ResultFragment.class);
                intent.putExtra("photo", imageBitmapGallery);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
