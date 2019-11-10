package com.example.mynavjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    //For save picture to Storage
    private Uri filepath;
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
//        button_result2 = findViewById(R.id.show_result_btn2);
//        button_result3 = findViewById(R.id.show_result_btn3);

        btn_save = findViewById(R.id.show_result_btn_save);
        btn_discard = findViewById(R.id.show_result_btn_discard);
        //image result
        imageResult = findViewById(R.id.show_result_imageResult);
        imageResult.setImageBitmap(bitmap);
        getResultByTF(bitmap);
        getImagebyResult();
        checkUnknown();

        //Upload to FireStorage
        //btn_save.
        check = cutWongLeb(button_result1.getText().toString());

        button_result1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowResult.this,ShowDetail.class);
                intent.putExtra("title",check);
                if(check == "เทียนหยด") {
                    intent.putExtra("image",a);
                    Toast.makeText(ShowResult.this, check, Toast.LENGTH_SHORT).show();
                }
                if(check == "ลำโพงขาว") {
                    intent.putExtra("image",b);
                    Toast.makeText(ShowResult.this, check, Toast.LENGTH_SHORT).show();
                }
                if(check == "หงอนไก่") {
                    intent.putExtra("image",c);
                    Toast.makeText(ShowResult.this, check, Toast.LENGTH_SHORT).show();
                }
                if(check == "สกุลเอื้อง") {
                    intent.putExtra("image",d);
                    Toast.makeText(ShowResult.this, check, Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);

            }
        });
//        button_result2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShowResult.this,ViewInfo.class);
//                intent.putExtra("result",cutWongLeb(button_result3.getText().toString()));
//                startActivity(intent);
//
//            }
//        });
//        button_result3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShowResult.this,ViewInfo.class);
//                intent.putExtra("result",cutWongLeb(button_result3.getText().toString()));
//                startActivity(intent);
//
//            }
//        });
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
