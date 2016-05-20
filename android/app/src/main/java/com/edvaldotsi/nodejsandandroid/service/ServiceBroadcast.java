package com.edvaldotsi.nodejsandandroid.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Edvaldo on 05/05/2016.
 */
public class ServiceBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, AlertService.class));
    }
}
