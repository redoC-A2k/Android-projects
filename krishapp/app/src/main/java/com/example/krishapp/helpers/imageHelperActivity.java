package com.example.krishapp.helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.krishapp.R;

public class imageHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_helper);

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M);
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        },0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(imageHelperActivity.class.getSimpleName(),"grant result for"+permissions[0]+"is"+grantResults[0]);
    }
}