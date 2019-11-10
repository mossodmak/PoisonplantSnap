package com.example.mynavjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowDetail extends AppCompatActivity {

    TextView title, detail;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Information");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.rTitleTv1);
        imageView = findViewById(R.id.imageView1);

        String images = getIntent().getStringExtra("image");
        String mtitle = getIntent().getStringExtra("title");
        //String desc = getIntent().getStringExtra("description");
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("plant").child(mtitle);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                title.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //title.setText(mtitle);
        //detail.setText(desc);
        //imageView.setImageBitmap(bitmap);
        Picasso.get().load(images).into(imageView);
    }

    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
