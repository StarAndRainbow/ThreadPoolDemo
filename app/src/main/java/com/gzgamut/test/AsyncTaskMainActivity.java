package com.gzgamut.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 2018/2/23
 * guanbp@gzgamut.com
 */


//使用异步，AsyncTask可以更加方便地执行后台任务，以及在主线程更新ui。但不适合特别好使的后台任务

public class AsyncTaskMainActivity extends AppCompatActivity {

    private ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.iv_img);
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("http://f.hiphotos.baidu.com/image/pic/item/503d269759ee3d6db032f61b48166d224e4ade6e.jpg");
    }


    //http://f.hiphotos.baidu.com/image/pic/item/503d269759ee3d6db032f61b48166d224e4ade6e.jpg
    class MyAsyncTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            //这是在后台子线程中执行的
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                publishProgress(70);//这里是更新进度
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //这里是开始线程之前执行的,是在UI线程
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //当任务执行完成是调用,在UI线程
            img.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //更新进度
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            //当任务被取消时回调
            super.onCancelled(bitmap);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //当任务被取消时回调
        }
    }
}
