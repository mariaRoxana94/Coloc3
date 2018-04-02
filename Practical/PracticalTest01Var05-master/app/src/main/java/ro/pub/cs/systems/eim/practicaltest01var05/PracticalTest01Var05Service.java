package ro.pub.cs.systems.eim.practicaltest01var05;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by student on 30.03.2018.
 */

public class PracticalTest01Var05Service extends Service {

    public static boolean status = false;
    private static final String TAG = "PracticalTestService";
    private ProcessingThread processingThread = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        status = true;

        int sum = Integer.parseInt(intent.getStringExtra(Constants.SUM_EXTRA));

        processingThread = new ProcessingThread(PracticalTest01Var05Service.this, sum);
        processingThread.start();

        Log.d(TAG, "onStartCommand: Thread started");
        
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
        Log.d(TAG, "Service stopped");
        status = false;
    }


}
