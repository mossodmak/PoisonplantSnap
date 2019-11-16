package com.example.mynavjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    String testStringOut = "String Fragment";
    private static final int STORAGE_PERMISSION_CODE = 23;
    private final String filename = "PoisonPlantSnap";
    private File folder;
    private String folderPath;
    private String command = "";
    //private String test;
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        command = this.getIntent().getStringExtra("command");

        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        folderPath = folder.getPath();

        if(checkPermission()==false) verifyPermission();

        //BottomNavigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if("feeds".equalsIgnoreCase(command)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GalleryFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_gallery);
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
        }

    }
    // Passing Data to Fragment Method 2 (Outside onCreate)
    public String getTestString(){
        return testStringOut;
    }


    private boolean checkPermission(){
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if(ContextCompat.checkSelfPermission(this.getApplicationContext() ,permission) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    private void verifyPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    public void createPictureFolder (){
         if(!folder.exists()){
             folder.mkdirs();
             //Toast.makeText(this, folder+" Created Folder", Toast.LENGTH_SHORT).show();
         }else{
             //Toast.makeText(this, folder.getPath()+" Folder is Exist", Toast.LENGTH_SHORT).show();
         }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_gallery:
                            selectedFragment = new GalleryFragment();
                            break;
                        case R.id.nav_camera:
                            createPictureFolder();
                            selectedFragment = new CameraFragment(MainActivity.this, folderPath);
                            break;
                        case R.id.nav_more:
                            selectedFragment = new MoreFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}
