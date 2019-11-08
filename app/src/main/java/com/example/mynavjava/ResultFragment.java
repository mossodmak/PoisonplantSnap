package com.example.mynavjava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ResultFragment extends Fragment {

    private ImageView imageResult;
    private static final String MODEL_PATH = "model3_quant_false.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;
    private Handler handler = new Handler();
    private Executor executor = Executors.newSingleThreadExecutor();
    private Button button_result1;
    private Classifier classifier;
    private Bitmap bitmap;

    public ResultFragment(){
    }
    public ResultFragment(Bitmap bit){
        bitmap = bit;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);
        initTensorFlowAndLoadModel();

        Bitmap bitmap = (Bitmap) this.getActivity().getIntent().getParcelableExtra("photo");
        button_result1 = getActivity().findViewById(R.id.button_result1);
        imageResult = getActivity().findViewById(R.id.imageResult);
        imageResult.setImageBitmap(bitmap);
        getResultByTF(bitmap);

        TextView res = v.findViewById(R.id.title_result);

        return  v;
    }
    private void getResultByTF(Bitmap imageBitmapCamera) {

        if (classifier != null) {
            final List<Classifier.Recognition> resultsCamera = classifier.recognizeImage(imageBitmapCamera);
            button_result1.setText(resultsCamera.get(0).toString());


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
                            getContext().getAssets(),
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
