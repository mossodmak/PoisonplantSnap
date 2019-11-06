package com.example.mynavjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewInfo extends AppCompatActivity {

    private DatabaseReference reff;
    private String result;
    //String name,recover,poisondetail,poisonpart,sympton,sciencename,plantdetail,family;
    private TextView a,b,c,d,e,f,g,h;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);

        result = this.getIntent().getStringExtra("result");
        btn = findViewById(R.id.activity_view_btn);

        a = findViewById(R.id.activity_view_tv1);
        b = findViewById(R.id.activity_view_tv2);
        c = findViewById(R.id.activity_view_tv3);
        d = findViewById(R.id.activity_view_tv4);
        e = findViewById(R.id.activity_view_tv5);
        f = findViewById(R.id.activity_view_tv6);
        g = findViewById(R.id.activity_view_tv7);
        h = findViewById(R.id.activity_view_tv8);



        reff = FirebaseDatabase.getInstance().getReference().child("Plant").child(result);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String recover=dataSnapshot.child("recover").getValue().toString();
                String poisondetail=dataSnapshot.child("poisondetail").getValue().toString();
                String poisonpart=dataSnapshot.child("poisonpart").getValue().toString();
                String sympton=dataSnapshot.child("sympton").getValue().toString();
                String sciencename=dataSnapshot.child("sciencename").getValue().toString();
                String plantdetail=dataSnapshot.child("plantdetail").getValue().toString();
                String family=dataSnapshot.child("family").getValue().toString();
                a.setText(name);
                b.setText(sciencename);
//                c.setText(family);
//                d.setText(plantdetail);
//                e.setText(poisonpart);
//                f.setText(poisondetail);
//                g.setText(sympton);
//                h.setText(recover);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewInfo.this, "wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
