package com.gzgamut.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;



//使用HandlerThread。使用HandlerThread是一种具有循环的线程

public class HandlerThreadMainActivity extends AppCompatActivity {
        private TextView tvMain;

        private HandlerThread mHandlerThread;
        //子线程中的handler,创建HandlerThread之后创建Handler，参数为handlerThread.getloop(),getLoop前要先start
        private Handler mThreadHandler;

        //UI线程中的handler,ui中的handler在这里创建即可
        private Handler mMainHandler = new Handler();

        //以防退出界面后Handler还在执行
        private boolean isUpdateInfo;
        //用以表示该handler的常熟
        private static final int MSG_UPDATE_INFO = 0x110;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            tvMain = (TextView) findViewById(R.id.tv_main);

            initThread();
        }


        private void initThread()
        {

            //1初始化HandlerThread
            mHandlerThread = new HandlerThread("check-message-coming");
            //2，调用start
            mHandlerThread.start();

            //创建多线程的Handler，接收参数HandlerThread.getLooper()
            mThreadHandler = new Handler(mHandlerThread.getLooper())
            {
                @Override
                public void handleMessage(Message msg)
                {
                    //接收消息之后更新数据,执行
                    update();

                    if (isUpdateInfo)
                        //不断发送消息
                        mThreadHandler.sendEmptyMessage(MSG_UPDATE_INFO);
                }
            };

        }

        private void update()
        {
            try
            {
                //子线程中睡眠两秒。然后通过主线程中的handler更新界面
                Thread.sleep(2000);
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String result = "每隔2秒更新一下数据：";
                        result += Math.random();
                        tvMain.setText(result);
                    }
                });

            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected void onResume()
        {
            super.onResume();
            //开始查询
            isUpdateInfo = true;
            //调用完onCreate的方法，在onResume中发送一条消息触发
            mThreadHandler.sendEmptyMessage(MSG_UPDATE_INFO);
        }

        @Override
        protected void onPause()
        {
            super.onPause();
            //停止查询
            //以防退出界面后Handler还在执行
            isUpdateInfo = false;
            mThreadHandler.removeMessages(MSG_UPDATE_INFO);
        }

        @Override
        protected void onDestroy()
        {
            super.onDestroy();
            //释放资源
            mHandlerThread.quit();
        }
    }