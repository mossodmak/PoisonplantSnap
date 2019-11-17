package com.example.mynavjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowDetail extends AppCompatActivity {

    TextView title, poisonpart, sympton, recover;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String destination, result, modelName;
    Button btn_back;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Information");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.rTitleTv1);
        poisonpart = findViewById(R.id.poisonpart);
        imageView = findViewById(R.id.imageView1);
        sympton = findViewById(R.id.sympton);
        recover = findViewById(R.id.recover);

        //String images = getIntent().getStringExtra("image");
        String mtitle = this.getIntent().getStringExtra("title");
        //Save destination for btn_back
        destination = this.getIntent().getStringExtra("destination");
        result = this.getIntent().getStringExtra("result");
        //Toast.makeText(this, "result: "+result, Toast.LENGTH_SHORT).show();
        modelName = this.getIntent().getStringExtra("modelName");
        bitmap = this.getIntent().getParcelableExtra("photo");
        // back button
        btn_back = findViewById(R.id.show_detail_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("showresult".equalsIgnoreCase(destination)){
                    Intent intent = new Intent(ShowDetail.this, ShowResult.class);
                    intent.putExtra("photo", bitmap);
                    intent.putExtra("result", result);
                    intent.putExtra("modelName", modelName);
                    startActivity(intent);
                }else if("search".equalsIgnoreCase(destination)){
                    Intent intent = new Intent(ShowDetail.this, MainActivity.class);
                    String command = "search";
                    intent.putExtra("command",command);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ShowDetail.this, MainActivity.class);
                    String command = "search";
                    intent.putExtra("command", command);
                    startActivity(intent);
                }
            }
        });
        //String desc = getIntent().getStringExtra("description");
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("plant").child(mtitle);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String poisonparts = dataSnapshot.child("poisonpart").getValue().toString();
                String symptons = dataSnapshot.child("sympton").getValue().toString();
                String recovers = dataSnapshot.child("recover").getValue().toString();
                String paths = dataSnapshot.child("path").getValue().toString();

                title.setText(name);
                poisonpart.setText(poisonparts);
                sympton.setText(symptons);
                recover.setText(recovers);
                Glide.with(ShowDetail.this).load(paths).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //title.setText(mtitle);
        //detail.setText(desc);
        //imageView.setImageBitmap(bitmap);
    }

    @Override
    public  boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
