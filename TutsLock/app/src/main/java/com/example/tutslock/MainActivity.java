package com.example.tutslock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button lock,enableBtn,disableBtn;
    private static final int RESULT_ENABLE=11;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName compName;
    private ActivityManager activityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager =  (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this,MyAdmin.class);
        lock = findViewById(R.id.lock);
        enableBtn = findViewById(R.id.enableBtn);
        disableBtn = findViewById(R.id.disableBtn);
        lock.setOnClickListener(this);
        enableBtn.setOnClickListener(this);
        disableBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disableBtn.setVisibility(isActive?View.VISIBLE:View.GONE);
        enableBtn.setVisibility(isActive?View.GONE:View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == lock){
            boolean active = devicePolicyManager.isAdminActive(compName);
            if (active){
                devicePolicyManager.lockNow();
            }
            else{
                Toast.makeText(this,"You need to enable the Admin Device Features",Toast.LENGTH_SHORT).show();
            }
        } else if(view == enableBtn){
//             boolean active = devicePolicyManager.isAdminActive(compName);
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.Extra)
        } else {
            Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
        }
    }
}