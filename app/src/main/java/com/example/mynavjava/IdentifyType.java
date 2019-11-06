package com.example.mynavjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IdentifyType extends AppCompatActivity {

    private Button btn_leaf;
    private Button btn_flower;
    private String modelName[] = {"model3_leaf_quant_false.tflite", "model3_flower_quant_false.tflite"};
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_type);
        bitmap = this.getIntent().getParcelableExtra("photo");

        imageView = findViewById(R.id.identified_image);
        btn_leaf = findViewById(R.id.identified_btn_leaf);
        btn_flower = findViewById(R.id.identified_btn_flower);

        imageView.setImageBitmap(bitmap);

        btn_leaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentifyType.this, ShowResult.class);
                intent.putExtra("photo", bitmap);
                intent.putExtra("modelName", modelName[0]);
                startActivity(intent);
            }
        });
        btn_flower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentifyType.this, ShowResult.class);
                intent.putExtra("photo", bitmap);
                intent.putExtra("modelName", modelName[1]);
                startActivity(intent);
            }
        });
    }

}
