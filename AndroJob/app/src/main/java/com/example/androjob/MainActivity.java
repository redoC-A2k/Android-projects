package com.example.androjob;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button setAuthorizedAppsBtn;
    private Button clearAuthorizedAppsBtn;
    private CheckBox isMyAppAuthorizedChk;

    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminRcvr;

    private static final String MY_AUTHORIZED_APP = "com.android.test.androjob";
    private static final String[] AUTHORIZED_PINNING_APPS = {MY_AUTHORIZED_APP};
    private static final String[] NO_AUTHORIZED_PINNING_APPS = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAuthorizedAppsBtn = findViewById(R.id.set_authorized_apps_btn);
        clearAuthorizedAppsBtn = findViewById(R.id.clear_authorized_apps_btn);
        isMyAppAuthorizedChk = findViewById(R.id.is_app_pinnable);

        mDPM = (DevicePolicyManager) this.getSystemService(DEVICE_POLICY_SERVICE);
        mDeviceAdminRcvr = new ComponentName(this, MyDeviceAdminReceiver.class);

        setAuthorizedAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAuthorizedApps();
            }
        });
        clearAuthorizedAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAuthorizedApps();
            }
        });
    }

    private void setAuthorizedApps(){
        mDPM.setLockTaskPackages(mDeviceAdminRcvr, AUTHORIZED_PINNING_APPS);
        updateAppPinnableChk();
    }

    private void clearAuthorizedApps(){
        mDPM.setLockTaskPackages(mDeviceAdminRcvr, NO_AUTHORIZED_PINNING_APPS);
        updateAppPinnableChk();
    }

    private void updateAppPinnableChk(){
        boolean authorized = mDPM.isLockTaskPermitted(MY_AUTHORIZED_APP);
        isMyAppAuthorizedChk.setChecked(authorized);
    }

    public boolean isAppInLockTaskMode() {
        ActivityManager activityManager;

        activityManager = (ActivityManager)
                this.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For SDK version 23 and above.
            return activityManager.getLockTaskModeState()
                    != ActivityManager.LOCK_TASK_MODE_NONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // When SDK version >= 21. This API is deprecated in 23.
            return activityManager.isInLockTaskMode();
        }

        return false;
    }

    private void unpin(){
        if (isAppInLockTaskMode()){
            stopLockTask();
        }
        else {
            Toast.makeText(this, "Application already unpinned", Toast.LENGTH_SHORT).show();
        }
    }

    private void pin() {
//        if(!isAppInLockTaskMode()) {
            startLockTask();
//        } else {
//            Toast.makeText(this, "Application already pinned !", LENGTH_LONG).show();
//        }
    }
}