package com.gzgamut.test;

import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

/**
 * 2018/2/24
 * guanbp@gzgamut.com
 */
public class MyIntentServiceMainActivity extends AppCompatActivity implements View.OnClickListener {
    public   final String SERVICEACTION = "com.gzgamut.serviceaction";
    public   final String THREADACTION = "com.gzgamut.threadaction";
    public  final String STATUS = "status";
    public  final String  PROGRESS= "progress";

    private TextView tv_threadStatus;
    private TextView tv_serviceStatus;
    private TextView tv_progress_chedule;
    private Button start;
    private Button stop;

    private Intent startIntent;

    LocalBroadcastManager mLocalBroadcastManager;

    private MyReceivr myReceivr ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_threadStatus  = (TextView) findViewById(R.id.tv_threadStatus);
        tv_serviceStatus = (TextView)findViewById(R.id.tv_serviceStatus);
        tv_progress_chedule = (TextView)findViewById(R.id.tv_progress_chedule);
        start =  (Button) findViewById(R.id.start);
        stop =  (Button )findViewById(R.id.stop);
        startIntent = new Intent(this, MyIntentService.class);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        myReceivr = new MyReceivr();
        mLocalBroadcastManager.registerReceiver(myReceivr,new IntentFilter(THREADACTION));
        mLocalBroadcastManager.registerReceiver(myReceivr,new IntentFilter(SERVICEACTION));
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.start:
                 this.startService(startIntent);
                 break;
             case R.id.stop:
                 stopService(startIntent);
                 break;
         }
    }


    //广播
    class MyReceivr extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case SERVICEACTION:
                    tv_serviceStatus.setText("服务状态：" + intent.getStringExtra(STATUS));
                break;
                case THREADACTION:
                    tv_threadStatus.setText("线程状态：" + intent.getStringExtra(STATUS));
                    tv_progress_chedule.setText("进度：" + intent.getStringExtra(PROGRESS));
                break;
                default:
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(myReceivr);
    }
}
