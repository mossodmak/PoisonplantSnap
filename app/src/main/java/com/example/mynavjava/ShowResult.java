package com.example.mynavjava;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private Classifier classifier;
    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTensorFlowAndLoadModel();
        Bitmap bitmap = this.getIntent().getParcelableExtra("photo");
        currentPhotoPath = this.getIntent().getStringExtra("DIR_PATH");
        setContentView(R.layout.show_result);

        button_result1 = findViewById(R.id.show_result_btn1);
        button_result2 = findViewById(R.id.show_result_btn2);
        button_result3 = findViewById(R.id.show_result_btn3);
        imageResult = findViewById(R.id.show_result_imageResult);
        imageResult.setImageBitmap(bitmap);
        getResultByTF(bitmap);
    }
    private void getResultByTF(Bitmap imageBitmapCamera) {

        if (classifier != null) {
            final List<Classifier.Recognition> resultsCamera = classifier.recognizeImage(imageBitmapCamera);
            button_result1.setText(resultsCamera.get(0).toString());
            button_result2.setText(resultsCamera.get(1).toString());
            button_result3.setText(resultsCamera.get(2).toString());

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    classifier.close();
                }
            });

        }
    }
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowLite.create(
                            getAssets(),
                            MODEL_PATH,
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
        return reString;
    }
}
