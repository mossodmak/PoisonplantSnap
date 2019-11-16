package com.example.mynavjava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShowResult extends AppCompatActivity {

    private ImageView imageResult;
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private Handler handler = new Handler();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Button button_result1, btn_back;
    private ImageView imageView1;
    private Classifier classifier;
    private String currentPhotoPath;
    private String pic1;
    private String modelName;
    private String check;
    private TextView unknown;
    private String b = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B8%A5%E0%B8%B3%E0%B9%82%E0%B8%9E%E0%B8%87%E0%B8%94%E0%B8%AD%E0%B8%81%E0%B8%82%E0%B8%B2%E0%B8%A7.jpg?alt=media&token=ab487e37-7767-46cb-9923-12e704e9e508";
    private String a = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B9%80%E0%B8%97%E0%B8%B5%E0%B8%A2%E0%B8%99%E0%B8%AB%E0%B8%A2%E0%B8%94.jpg?alt=media&token=cd85118d-f3e1-4a63-9f46-53730c5fd8b2";
    private String c = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B8%AB%E0%B8%87%E0%B8%AD%E0%B8%99%E0%B9%84%E0%B8%81%E0%B9%88.jpg?alt=media&token=5454de0c-29ea-499c-b55b-52bc6eaef40a";
    private String d = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/sakul.jfif?alt=media&token=19cae5a2-ef88-434b-82a0-83e2bf4845db";
    private String command;

    //For save picture to Storage
    private Button btn_share;
    private Uri filePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String photo_filepath;
    //For upload in database
    private String photo_url;
    private String timestamp;
    private String percent;
    private String plant;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //For check user already login or not?
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    //Save bitmap image
    private Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = this.getIntent().getParcelableExtra("photo");
        //currentPhotoPath = this.getIntent().getStringExtra("DIR_PATH");
        modelName = this.getIntent().getStringExtra("modelName");
        initTensorFlowAndLoadModel(modelName);
        //getUser login
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setContentView(R.layout.show_result);
        unknown = findViewById(R.id.show_result_textResult);
        imageView1=findViewById(R.id.show_result_imv1);
        button_result1 = findViewById(R.id.show_result_btn1);
        btn_share = findViewById(R.id.btn_share);

        //image result
        imageResult = findViewById(R.id.show_result_imageResult);
        imageResult.setImageBitmap(bitmap);
        getResultByTF(bitmap);
        check = cutWongLeb(button_result1.getText().toString());
        getImagebyResult();
        checkUnknown();
        //Upload to FireDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("shares");
        //Upload to FireStorage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = getImageUri(ShowResult.this, bitmap);
                uploadImage();
            }
        });
        //Bug: btn_save never show on ShowResult at running application
        button_result1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this, ShowDetail.class);
                intent.putExtra("title",check);
                startActivity(intent);
            }
        });
        //Btn back to select type
        btn_back.findViewById(R.id.show_result_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this, IdentifyType.class);
                intent.putExtra("photo", bitmap);
                startActivity(intent);
            }
        });
    }
    private void shareData(){
        ShareObject post;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        timestamp = sdf.format(new Date());
        plant = cutWongLeb(button_result1.getText().toString());
        percent = "Predict with "+getPercent(button_result1.getText().toString());
        if(user != null) {
            post = new ShareObject(photo_url, user.getDisplayName(), timestamp, plant, percent);
        }else{
            post = new ShareObject(photo_url, timestamp, plant, percent);
        }

        databaseReference.push() // Use this method to create unique id of commit
                .setValue(post);

        Intent intent = new Intent(ShowResult.this, MainActivity.class);
        command = "feeds";
        intent.putExtra("command", command);
        startActivity(intent);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void uploadImage(){
        //Save from data in memory method

        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference = firebaseStorage.getReference().child("photos/"+ UUID.randomUUID().toString());
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ShowResult.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    photo_url = uri.toString() ;
                                    shareData();
                                    //url.setText(img_url);
                                    //url2.setText(uri.toString());
                                    //Glide.with(ShowResult.this).load(uri).into(img_retrieve);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShowResult.this, " "+storageReference.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ShowResult.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress  = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    private void getResultByTF(Bitmap imageBitmapCamera) {

        if (classifier != null) {
            final List<Classifier.Recognition> resultsCamera = classifier.recognizeImage(imageBitmapCamera);

                    button_result1.setText(resultsCamera.get(0).toString());

                    /*if(getInt(resultsCamera.get(0).toString())<50){
                        unknown.setText("Unknown");
                    }*/

            }

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    classifier.close();
                }
            });

        }

    private void initTensorFlowAndLoadModel(final String filename) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowLite.create(
                            getAssets(),
                            filename,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public String cutWongLeb(String str){
        String reString = "";
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '('){
                reString = str.substring(0, i);
            }
        }
        return reString.trim();
    }
    public int getInt(String str){
        int conf = 0;
            for(int i = 0; i < str.length(); i++){
                if(str.charAt(i)=='('){
                    for(int j = i; j < str.length(); j++){
                        if(str.charAt(j)=='.'){
                            conf = Integer.parseInt(str.substring(i+1,j));
                        }
                    }
                }
            }
        return conf;
    }
    public String getPercent(String str){
        String reString = "";
        int n = str.length();
        for(int i = 0; i < n; i++){
            if(str.charAt(i) == '('){
                reString = str.substring(i+1, n-1);
            }
        }
        return reString.trim();
    }
    public void checkUnknown(){
        int n = getInt(button_result1.getText().toString());
        if(n<50){
            unknown.setText("Unidentified");
        }else{
            unknown.setText(cutWongLeb(button_result1.getText().toString()));
        }
    }
    private void getImagebyResult(){
        pic1 = cutWongLeb(button_result1.getText().toString());
        if(pic1.equals("เทียนหยด")){
            arrange(a);
        }
        if(pic1.equals("ลำโพงขาว")) {
            arrange(b);
        }

        if(pic1.equals("หงอนไก่")){
            arrange(c);
        }
        if(pic1.equals("สกุลเอื้อง")){
            arrange(d);
        }
    }
    private void arrange(String a) {
        Glide.with(ShowResult.this).load(a).into(imageView1);
    }
}
