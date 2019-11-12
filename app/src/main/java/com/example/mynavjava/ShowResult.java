package com.example.mynavjava;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShowResult extends AppCompatActivity {

    private ImageView imageResult;
    private static final String MODEL_PATH = "model3_quant_false.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private Handler handler = new Handler();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Button button_result1;
    private Button button_result2;
    private Button button_result3;
    private Button btn_save;
    private Button btn_discard;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private Classifier classifier;
    private String currentPhotoPath;
    private String pic1,pic2,pic3;
    private String modelName;
    private String check;
    private TextView unknown;
    private String b = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B8%A5%E0%B8%B3%E0%B9%82%E0%B8%9E%E0%B8%87%E0%B8%94%E0%B8%AD%E0%B8%81%E0%B8%82%E0%B8%B2%E0%B8%A7.jpg?alt=media&token=ab487e37-7767-46cb-9923-12e704e9e508";
    private String a = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B9%80%E0%B8%97%E0%B8%B5%E0%B8%A2%E0%B8%99%E0%B8%AB%E0%B8%A2%E0%B8%94.jpg?alt=media&token=cd85118d-f3e1-4a63-9f46-53730c5fd8b2";
    private String c = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/%E0%B8%AB%E0%B8%87%E0%B8%AD%E0%B8%99%E0%B9%84%E0%B8%81%E0%B9%88.jpg?alt=media&token=5454de0c-29ea-499c-b55b-52bc6eaef40a";
    private String d = "https://firebasestorage.googleapis.com/v0/b/mynavjava.appspot.com/o/sakul.jfif?alt=media&token=19cae5a2-ef88-434b-82a0-83e2bf4845db";

    //For save picture to Storage
    private Uri filePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference photoRef;
    private String photo_url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bitmap bitmap = this.getIntent().getParcelableExtra("photo");
        //currentPhotoPath = this.getIntent().getStringExtra("DIR_PATH");
        modelName = this.getIntent().getStringExtra("modelName");
        initTensorFlowAndLoadModel(modelName);


        setContentView(R.layout.show_result);
        unknown = findViewById(R.id.show_result_textResult);
        imageView1=findViewById(R.id.show_result_imv1);
        button_result1 = findViewById(R.id.show_result_btn1);

        btn_save = findViewById(R.id.show_result_btn_save);
        btn_discard = findViewById(R.id.show_result_btn_discard);
        //image result
        imageResult = findViewById(R.id.show_result_imageResult);
        imageResult.setImageBitmap(bitmap);
        getResultByTF(bitmap);
        check = cutWongLeb(button_result1.getText().toString());
        getImagebyResult();
        checkUnknown();

        //Upload to FireStorage

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        photoRef = storageReference.child("photos/"+ UUID.randomUUID().toString());
        photo_url = photoRef.toString();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        //Bug: btn_save never show on ShowResult at running application

        button_result1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this,ShowDetail.class);
                intent.putExtra("title",check);
                startActivity(intent);

            }
        });

    }
    private void uploadImage(){
        //Save from data in memory method
        imageResult.setDrawingCacheEnabled(true);
        imageResult.buildDrawingCache();
        Bitmap bitmap2 = ((BitmapDrawable) imageResult.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ShowResult.this, "Failed", Toast.LENGTH_SHORT);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ShowResult.this, "Uploaded", Toast.LENGTH_SHORT);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
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
