package com.wcf.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author wcf
 * @time 2017/7/25 9:34
 * @desc
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            System.out.println("监听到息屏广播");
            HooliganActivity. startHooligan();

        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            System.out.println("监听到亮屏广播");
            HooliganActivity. killHooligan();

        }
    }
}

