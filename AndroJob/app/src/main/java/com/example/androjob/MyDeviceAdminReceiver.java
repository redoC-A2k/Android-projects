package com.example.androjob;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = "AndroJobApp";

    @Override
    public void onLockTaskModeEntering(@NonNull Context context, @NonNull Intent intent, @NonNull String pkg) {
//        super.onLockTaskModeEntering(context, intent, pkg);
        Toast.makeText(context, "Lock task mode entered", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Lock task mode entered");
        Log.i(TAG, "action : "+intent.getAction());
        Log.i(TAG, "package : "+intent.getStringExtra(DeviceAdminReceiver.EXTRA_LOCK_TASK_PACKAGE));
    }

    @Override
    public void onLockTaskModeExiting(@NonNull Context context, @NonNull Intent intent) {
//        super.onLockTaskModeExiting(context, intent);
        Toast.makeText(context, "Lock task mode exited",Toast.LENGTH_LONG).show();
        Log.i(TAG, "Lock task mode exited");
        Log.i(TAG, "action : "+intent.getAction());
        Log.i(TAG, "action : "+intent.getStringExtra(DeviceAdminReceiver.EXTRA_LOCK_TASK_PACKAGE));
    }
}
