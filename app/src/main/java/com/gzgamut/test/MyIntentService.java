package com.gzgamut.test;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * 2018/2/24
 * guanbp@gzgamut.com
 */

//intentservice有个特性是：当前执行的任务无法打断
public class MyIntentService extends IntentService{

    private static final String TAG = "MyIntentService";

    public   final String SERVICEACTION = "com.gzgamut.serviceaction";
    public   final String THREADACTION = "com.gzgamut.threadaction";
    public  final String STATUS = "status";
    public  final String  PROGRESS= "progress";
    private boolean isRunning = true;
    private int count = 0;
    private  LocalBroadcastManager mLocalBroadcastManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService() {
        super("MyIntentService_name");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //ocalBroadcastManager是Android Support包提供了一个工具，用于在同一个应用内的不同组件间发送Broadcast。
        // LocalBroadcastManager也称为局部通知管理器
         mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        sendServiceStatus("服务开始");
        Log.e(TAG, "onCreate: " );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent: " );
        try {
            sendThreadStatus("线程启动", count);
            Thread.sleep(1000);
            sendServiceStatus("服务运行中...");

            isRunning = true;
            count = 0;
            while (isRunning) {
                count++;
                if (count >= 100) {
                    isRunning = false;
                }
                Thread.sleep(50);
                sendThreadStatus("线程运行中...", count);
            }
            sendThreadStatus("线程结束", count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sendServiceStatus("服务结束");
        Log.e(TAG, "onCreate: " );
    }

    // 发送服务状态信息
    private void sendServiceStatus(String status) {
        Intent intent = new Intent(SERVICEACTION);
        intent.putExtra(STATUS, status);
        mLocalBroadcastManager.sendBroadcast(intent);
    }


    // 发送线程状态信息
    private void sendThreadStatus(String status, int progress) {
        Intent intent = new Intent(THREADACTION);
        intent.putExtra(STATUS, status);
        intent.putExtra(PROGRESS, progress+"");
        mLocalBroadcastManager.sendBroadcast(intent);
    }
}
