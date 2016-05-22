package com.jiang.deliciousfood.commen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.jiang.deliciousfood.MainActivity;
import com.jiang.deliciousfood.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.helper.NotificationCompat;

/**
 * Created by Jiang on 2016/5/1.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private String parseMessage;
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
        Log.i("BmobClient", "收到的推送消息：" + message);
        //Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_LONG).show();
        //使用cn.bmob.v3.helper包下的NotificationCompat来创建通知栏，也可以使用support_v4里面的NotificationCompat类
        try {
            JSONObject object=new JSONObject(message);
            parseMessage=object.getString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent i = new Intent();
        i.setClass(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(context)
                .setTicker("BmobExample收到消息推送")
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle("您有一条新消息")
                .setContentText(parseMessage)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pi);
        // 发送通知
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = mBuilder.build();
        nm.notify(0, n);
    }

}