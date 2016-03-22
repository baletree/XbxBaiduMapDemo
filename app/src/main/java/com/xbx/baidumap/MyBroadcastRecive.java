package com.xbx.baidumap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by EricYuan on 2016/3/18.
 */
public class MyBroadcastRecive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if("com.xbx.broadcast.test".equals(intent.getAction())){
            Log.i("Tag","Reciever Message!");
        }*/
        context.startService(new Intent(context, MyService.class));
    }
}
