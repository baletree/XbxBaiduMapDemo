package com.xbx.baidumap;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by EricYuan on 2016/3/18.
 */
public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        IntentFilter localIntentFilter = new IntentFilter("android.intent.action.USER_PRESENT");
        localIntentFilter.addCategory("android.provider.Telephony.SMS_RECEIVED");
        localIntentFilter.setPriority(Integer.MAX_VALUE);// 整形最大值
        MyBroadcastRecive searchReceiver = new MyBroadcastRecive();
        registerReceiver(searchReceiver, localIntentFilter);
        Log.i("Tag","启动服务并且注册了广播！！");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, MyService.class); // 销毁时重新启动Service
        this.startService(localIntent);
        super.onDestroy();
    }
}
