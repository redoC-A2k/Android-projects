package com.example.androservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.ACTION_SCREEN_ON.equals(intent.getAction())){
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            Toast.makeText(context , "Screen On", Toast.LENGTH_SHORT).show();
        }
    }
}
