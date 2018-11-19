package com.puquan.health.bodyhealth.main.activity;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.puquan.health.bodyhealth.R;
import com.puquan.health.bodyhealth.common.service.DownloadService;
import com.puquan.health.bodyhealth.common.service.UpdateService;
import com.puquan.health.bodyhealth.jpush.ExampleUtil;
import com.puquan.health.bodyhealth.jpush.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends InstrumentedActivity {

    private TextView mRegId;
    private String strMac = "";//设备mac地址

    public VideoView vv;

    public Button btn_load;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_load=findViewById(R.id.btn_load);
        registerMessageReceiver();  // used for receive msg
        requestPermission();//申请权限

        setListener();
    }

    public void setListener(){
        btn_load.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "myVideo.mp4");
                if (file.exists()){//文件存在 直接播放
                    Log.e("===开始播放==",Environment.getExternalStorageDirectory().getAbsolutePath()+"/myVideo.mp4");
                    setupVideo(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myVideo.mp4");
                }else {
                    Log.e("===开始下载文件==","loading...");
                    Intent intent=new Intent(MainActivity.this,UpdateService.class);
                    intent.putExtra("url","http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
                    intent.putExtra("name","myVideo.mp4");
                    startService(intent);
                }
            }
        });
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateService.LOADFINISH);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           if (intent.getAction().equals(UpdateService.LOADFINISH)){
                Log.e("==文件下载完成==","loadFinished...");
                setupVideo(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myVideo.mp4");
                stopService(new Intent(MainActivity.this,UpdateService.class));
            }
        }
    }

    //播放器的设置
    private void setupVideo(String url) {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.start();
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaybackVideo();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {
            Uri uri = Uri.parse(url);
            vv.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaybackVideo() {
        try {
            vv.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取设备mac地址
    public String getWifiMac(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String str = info.getMacAddress();
        if (str == null) str = "";
        return str;
    }

    //动态申请权限
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS
                        , Manifest.permission.READ_PHONE_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
    }
	
	
	@Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}