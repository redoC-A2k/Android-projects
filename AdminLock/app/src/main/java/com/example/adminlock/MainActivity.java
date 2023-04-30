package com.example.adminlock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    static final int REQ_DEV_ADMIN = 154 ;
    static int CAM_REQ_CODE=102;
    DevicePolicyManager deviceManager ;
    ComponentName compName ;
    Button startLockServiceBtn, stopLockServiceBtn, setQueBtn;
    EditText editNumQue , editTimeMin ;

    int numQue, timeMin ;
    String difficultyLevel ;
    static final String prefsLockStatusKey = "isLockServiceActive";
    static final String prefsNumQueKey = "NoOfQues";
    static final String prefsTimeMinKey = "TimeInMins";
    static final String prefsDropKey = "DifficultyLevel" ;


    boolean isLockServiceActive ;
    String TAG="MAIN ACTIVITY";
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 11;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main ) ;

        prefs = getSharedPreferences("AdminLock",MODE_PRIVATE);
        editor = prefs.edit();
        isLockServiceActive = prefs.getBoolean(prefsLockStatusKey,false);
        numQue = prefs.getInt(prefsNumQueKey,4);
        timeMin = prefs.getInt(prefsTimeMinKey, 15);
        difficultyLevel = prefs.getString(prefsDropKey, "Easy");

        startLockServiceBtn = findViewById(R.id.startLockServiceBtn);
        stopLockServiceBtn = findViewById(R.id.stopLockServiceBtn);

//        Log.i(TAG, "Current is lock service status"+isLockServiceActive);
        setIsLockServiceActive(isLockServiceActive);
        startLockServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllPermissions();
                setIsLockServiceActive(true);
            }
        });

        stopLockServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIsLockServiceActive(false);
            }
        });

        editTimeMin = findViewById(R.id.editTimeMin);
        editTimeMin.setText(Integer.toString(timeMin));
        editTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i(TAG,charSequence.toString());
                String tempText = charSequence.toString();
                if (tempText.equals("")) tempText="0";
                editor.putInt(prefsTimeMinKey,Integer.parseInt(tempText));
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        setQueBtn = findViewById(R.id.setQueBtn);
        setQueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateQueActivity.class);
                startActivity(intent);
            }
        });

        if (OpenCVLoader.initDebug()){
            Log.i(TAG,"OpenCv4 Loaded successfully");
        }

    }

    private void setIsLockServiceActive(boolean value){
        if (value){
            editor.putBoolean(prefsLockStatusKey,true);
            startLockServiceBtn.setVisibility(View.GONE);
            stopLockServiceBtn.setVisibility(View.VISIBLE);
            if (!isMyServiceRunning(MyService.class)){
                intent = new Intent(this,MyService.class);
                ContextCompat.startForegroundService(this,intent);
            }
            editor.apply();
        }
        else if(value == false){
            editor.putBoolean(prefsLockStatusKey,false);
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MyService.class.getName().equals(service.service.getClassName())) {
                    Log.i(TAG,"service is running");
                    intent = new Intent(this,MyService.class);
                }
            }
            if (intent != null){
                stopService(intent);
                startLockServiceBtn.setVisibility(View.VISIBLE);
                stopLockServiceBtn.setVisibility(View.GONE);
                editor.apply();
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void getAllPermissions(){
        deviceManager = (DevicePolicyManager)
                getSystemService(Context. DEVICE_POLICY_SERVICE ) ;
        compName = new ComponentName( this, DeviceAdmin.class ) ;
        boolean active = deviceManager.isAdminActive( compName ) ;
        if (active){
            Log.d(TAG,"Admin is active");
        }
        else{
            Log.d(TAG,"Admin is not active requesting permission");
            Intent intent = new Intent(DevicePolicyManager. ACTION_ADD_DEVICE_ADMIN ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_DEVICE_ADMIN , compName ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_ADD_EXPLANATION , "You should enable the grant device admin permission to make this app work!" ) ;
            startActivityForResult(intent , REQ_DEV_ADMIN ) ;

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                //Permission Granted-System will work
                Log.d(TAG,"Permission granted for drawing over other apps");
            }
        }
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAM_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super .onActivityResult(requestCode , resultCode , data) ;
        switch (requestCode) {
            case REQ_DEV_ADMIN:
            case ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:
                if (resultCode != Activity. RESULT_OK ) {
                    Toast.makeText (getApplicationContext(),
                            "Please grant permsission!" ,
                            Toast.LENGTH_SHORT
                    ).show() ;
                    finish();
                }
                break;
        }
    }


}