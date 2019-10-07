package com.example.mynavjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TestRecycleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycle_view);

        String[] ss = {"A","B","C","D","E","F"};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.recycle_content, FragmentRecycle.newInstance(ss))
                .commit();
    }
}
