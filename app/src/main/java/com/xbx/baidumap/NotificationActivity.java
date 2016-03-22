package com.xbx.baidumap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

/**
 * Created by EricYuan on 2016/3/18.
 */
public class NotificationActivity extends Activity implements View.OnClickListener{
    private Button show_notification_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        show_notification_btn = (Button) findViewById(R.id.show_notification_btn);
        show_notification_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_notification_btn:
                Bitmap btm = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher);
                Intent deleteIntent = new Intent(NotificationActivity.this, MyService.class);
                deleteIntent.setAction("com.xbx.broadcast.test");
                int deleteCode = (int) SystemClock.uptimeMillis();
                PendingIntent deletePendingIntent = PendingIntent.getService(this,
                        deleteCode, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        NotificationActivity.this).setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("5 new message")
                        .setContentText("twain@android.com")
                        .setDeleteIntent(deletePendingIntent);
                mBuilder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
                mBuilder.setNumber(12);
                mBuilder.setLargeIcon(btm);
                mBuilder.setAutoCancel(false).setOngoing(false);//自己维护通知的消失

                Notification notification = mBuilder.build();
//                notification.flags = Notification.FLAG_ONGOING_EVENT;
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, notification);
                break;
        }
    }
}
