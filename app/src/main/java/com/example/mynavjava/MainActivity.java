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

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    String testStringOut = "String Fragment";
    private static final int STORAGE_PERMISSION_CODE = 23;
    private final String filename = "PoisonPlantSnap";
    private File folder;
    private String folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String testString = "String Fragment";

        Toast.makeText(MainActivity.this, "Fire base is connect", Toast.LENGTH_LONG).show();

        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        folderPath = folder.getPath();

        // Passing Data to Fragment Method 1
//        bundle.putString("test","hello");
//        ResultFragment fg = new ResultFragment();
//        fg.setArguments(bundle);

        if(checkPermission()==false) verifyPermission();

        //BottomNavigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchFragment()).commit();

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
             Toast.makeText(this, folder+" Created Folder", Toast.LENGTH_SHORT).show();
         }else{
             Toast.makeText(this, folder.getPath()+" Folder is Exist", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
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
