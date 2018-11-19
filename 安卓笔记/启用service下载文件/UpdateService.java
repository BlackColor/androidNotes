package com.puquan.health.bodyhealth.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.puquan.health.bodyhealth.common.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 更新apk
 */

public class UpdateService extends Service {

    public static String LOADING = "loading";
    public static String LOADFINISH = "finish";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = intent.getStringExtra("url");
        final String fileName = intent.getStringExtra("name");
        //下载apk文件
        Request request = new Request.Builder().url(url).build();
        OkHttpClient clint = new OkHttpClient();
        clint.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("===网络请求失败==", e.toString() + "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();

                byte[] buf = new byte[2048];
                FileOutputStream fos = null;
                int len;
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //通知activity下载完成
                    broadcastUpdate(LOADFINISH);
                } catch (Exception e) {
                    Log.e("===下载 error==", e.toString() + "");
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        });
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 发送广播
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // 发送带有数据的广播
    private void broadcastUpdate(final String action, String time) {
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        sendBroadcast(intent);
    }
}
