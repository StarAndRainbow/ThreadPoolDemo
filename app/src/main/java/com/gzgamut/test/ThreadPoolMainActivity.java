package com.gzgamut.test;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**

 * 2018/2/24
 * guanbp@gzgamut.com
 * 线程池参考http://blog.csdn.net/u012702547/article/details/52259529和android开发艺术探究
 */
public class ThreadPoolMainActivity  extends AppCompatActivity{


    private ThreadPoolExecutor poolExecutor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建线程池，三个核心线程，
        // 线程池中最大线程数量为5，
        //非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收
        //单位
        //线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务

    }

    /**
     * 任务随机执行，每次打印出三个
     * 1.execute一个线程之后，如果线程池中的线程数未达到核心线程数，则会立马启用一个核心线程去执行
     * 2.execute一个线程之后，如果线程池中的线程数已经达到核心线程数，且workQueue未满，则将新线程放入workQueue中等待执行
     * @param view
     */
    public void btnClick(View view) {
        poolExecutor = new ThreadPoolExecutor(3, 5,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.d("google_lenve_fb", "run: " + finalI);
                }
            };
            poolExecutor.execute(runnable);
        }
    }


    /**
     * 首先打印出来0，1，2说明往核心线程添加了三个任务，然后将3，4，5，6，7，8六个任务添加到了任务队列中，
     * 接下来要添加的任务因为核心线程已满，队列已满所以就直接开一个非核心线程来执行，因此后添加的任务反而会先执行
     * （3，4，5，6，7，8都在队列中等着），所以我们看到的打印结果是先是0～2，然后9～29，然后3~8，当然，
     * 我们在实际开发中不会这样来配置最大线程数和线程队列。那如果我们需要自己来配置这些参数，该如何配置呢？参考一下AsyncTask
     public abstract class AsyncTask<Params, Progress, Result> {
     private static final String LOG_TAG = "AsyncTask";

     private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
     private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
     private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
     private static final int KEEP_ALIVE = 1;

     到，核心线程数为手机CPU数量+1（cpu数量获取方式Runtime.getRuntime().availableProcessors()），

     最大线程数为手机CPU数量×2+1，线程队列的大小为128，OK，
     那么小伙伴们在以后使用线程池的过程中可以参考这个再结合自己的实际情况来配置参数
     * @param view
     */
    public void btnClick2(View view) {
        poolExecutor = new ThreadPoolExecutor(3, 30,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(6));

        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.d("google_lenve_fb", "run: " + finalI);
                }
            };
            poolExecutor.execute(runnable);
        }
    }


    /**
     * FixedThreadPool中没有非核心线程，所有的线程都是核心线程，且线程的超时时间为0，
     * 说明核心线程即使在没有任务可执行的时候也不会被销毁（这样可让FixedThreadPool更快速的响应请求），
     * 最后的线程队列是一个LinkedBlockingQueue，但是LinkedBlockingQueue却没有参数，
     * 这说明线程队列的大小为Integer.MAX_VALUE（2的31次方减1），OK，看完参数，我们大概也就知道了FixedThreadPool的工作特点了，
     * 当所有的核心线程都在执行任务的时候，新的任务只能进入线程队列中进行等待，直到有线程被空闲出来
     * @param view
     */
    public void btnClick3(View view) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    SystemClock.sleep(3000);
                    Log.d("google_lenve_fb", "run: "+ finalI);
                }
            };
            fixedThreadPool.execute(runnable);
        }
    }


    /**
     * singleThreadExecutor和FixedThreadPool很像，不同的就是SingleThreadExecutor的核心线程数只有1
     * @param view
     */
    public void btnClick4(View view) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("google_lenve_fb", "run: " + Thread.currentThread().getName() + "-----" + finalI);
                    SystemClock.sleep(1000);
                }
            };
            singleThreadExecutor.execute(runnable);
        }
    }



}
