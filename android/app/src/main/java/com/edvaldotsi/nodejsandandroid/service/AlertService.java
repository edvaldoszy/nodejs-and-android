package com.edvaldotsi.nodejsandandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Edvaldo on 05/05/2016.
 */
public class AlertService extends Service {

    private boolean started = false;
    private Worker worker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!started) {
            worker = new Worker(startId);
            worker.start();
            started = true;
            Log.i("ALERT_SERVICE", "SERVICE STARTED");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        worker.interrupt();
        Log.i("ALERT_SERVICE", "SERVICE STOPED");
        super.onDestroy();
    }

    private class Worker extends Thread {

        private int startID;
        private boolean active = true;
        private long interval = 30000;

        public Worker(int startID) {
            this.startID = startID;
        }

        @Override
        public void run() {
            while (active) {
                execute();

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    active = false;
                }
            }
            Log.i("ALERT_SERVICE", "SERVICE THREAD FINISHED");
            stopSelf(startID);
        }

        private void execute() {
            Log.i("ALERT_SERVICE", "SERVICE COMMAND EXECUTED");
        }
    }
}
