package com.example.mynavjava;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

//import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private ImageView imageView11;
    private TextView textViewResult1;
    private Context cons;
    private String directoryPath;
    private ImageView selectedImage;
    private File photoFile = null;
    private Uri imageUri;

    public CameraFragment(Context context, String folderPath) {
        cons = context;
        directoryPath = folderPath;
    }

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
                //openCameraIntent();
                Intent cameraView = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
//                if (cameraView.resolveActivity(getActivity().getPackageManager()) != null) {
//                    // Create the File where the photo should go
//                    try {
//                        photoFile = createImageFile();
//                    } catch (IOException ex) {
//                        // Error occurred while creating the File
//                        Toast.makeText(getContext(), "Error Occured!", Toast.LENGTH_SHORT).show();
//                    }
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        startActivityForResult(cameraView, CAMERA_REQUEST_CODE);
//                    }
//                }
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

//            Uri photoURI = FileProvider.getUriForFile(getContext(),
//                    "com.example.mynavjava",
//                    photoFile);
//            data.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //File file = new File(directoryPath);
            Bundle extras = data.getExtras();
            Bitmap imageBitmapCamera = (Bitmap) extras.get("data");
            //Bitmap imageBitmapCamera = BitmapFactory.decodeFile(file.getAbsolutePath());
//            Uri source_uri = imageUri;
//            Uri dest_uri = Uri.fromFile(new File(getActivity().getCacheDir(),"cropped"));
//            Crop.of(source_uri,dest_uri).asSquare().start(getActivity());
            imageBitmapCamera = Bitmap.createScaledBitmap(imageBitmapCamera, INPUT_SIZE, INPUT_SIZE, false);


            Intent intent = new Intent(cons, ShowResult.class);
            intent.putExtra("photo", imageBitmapCamera);
            //intent.putExtra("DIR_PATH", directoryPath);
            startActivity(intent);


        } else if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri pickedImage = data.getData();
            try {
//                selectedImage.setImageURI(pickedImage);
                Bitmap imageBitmapGallery = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), pickedImage);
                imageBitmapGallery = Bitmap.createScaledBitmap(imageBitmapGallery, INPUT_SIZE, INPUT_SIZE, false);


                Intent intent = new Intent(cons, ShowResult.class);
                intent.putExtra("photo", imageBitmapGallery);
//              intent.putExtra("DIR_PATH", directoryPath);
//              intent.putExtra("URI_IMAGE", pickedImage);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } //if(requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK){
//            imageUri = Crop.getOutput(data);
//            Toast.makeText(cons, "into crop", Toast.LENGTH_SHORT).show();
//            try {
//                Bitmap image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
//                image = Bitmap.createScaledBitmap(image ,INPUT_SIZE, INPUT_SIZE, false);
//                Intent intent = new Intent(cons, ShowResult.class);
//                intent.putExtra("photo", image);
////                intent.putExtra("DIR_PATH", directoryPath);
////                intent.putExtra("URI_IMAGE", pickedImage);
//                Toast.makeText(getActivity(), "Cropped", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }catch (IOException e){
//                e.printStackTrace();
//                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        directoryPath = image.getAbsolutePath();
        return image;
    }
    // Crop
    private void openCameraIntent(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        // tell camera where to store the resulting picture
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(directoryPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
}
