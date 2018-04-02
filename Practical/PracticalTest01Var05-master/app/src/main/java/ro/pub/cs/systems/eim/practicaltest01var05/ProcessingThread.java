package ro.pub.cs.systems.eim.practicaltest01var05;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

/**
 * Created by student on 30.03.2018.
 */

public class ProcessingThread extends Thread {

    private static final String TAG = "ProcessingThread";

    private Context context = null;
    private int sum = -1;

    private boolean isRunning = false;

    public ProcessingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;
    }

    @Override
    public synchronized void start() {
        super.start();
        isRunning = true;
    }

    @Override
    public void run() {
        Log.d(TAG, "Thread is running");
        while (isRunning) {
            sendMessage();
            sleep (Constants.SLEEP_TIME);
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();

        intent.setAction(Constants.ACTION1);

        intent.putExtra(Constants.BROADCAST_EXTRA, new Date(System.currentTimeMillis()) +
                " ; Sum = " + sum);

        context.sendBroadcast(intent);
        Log.d(TAG, "Sending broadcast");
    }



    private void sleep(int sleepTime) {
        Log.d(TAG, "sleep: 2 seconds");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  void stopThread() {
        isRunning = false;
    }
}
