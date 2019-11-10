package com.example.mynavjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Splash extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,MainActivity.class));
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(handler != null && runnable !=null){
            handler.removeCallbacks(runnable);
        }

    }
}

