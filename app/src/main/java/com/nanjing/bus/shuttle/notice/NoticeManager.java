package com.nanjing.bus.shuttle.notice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.nanjing.bus.shuttle.DemoActivity;
import com.nanjing.bus.shuttle.R;
import com.nanjing.bus.shuttle.app.AppData;

/**
 * Created by fylder on 2017/3/11.
 */

public class NoticeManager {

    Context mContext;

    static NoticeManager noticeManager;

    private static NotificationManager mNotificationManager;

    private static String LOCATION_TAG = "location";

    public NoticeManager() {
        mContext = AppData.getContext();
    }

    public static NoticeManager getInstance() {
        if (noticeManager == null) {
            noticeManager = new NoticeManager();
        }
        return noticeManager;
    }

    NotificationCompat.Builder downBuilder;

    /**
     * 开启通知
     */
    public void showNotify() {
        try {
            if (mNotificationManager == null) {
                mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            Intent notificationIntent = new Intent(mContext, DemoActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            downBuilder = new NotificationCompat.Builder(mContext)
                    .setTicker("正在定位")
                    .setSmallIcon(R.drawable.bus_icon)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .setContentTitle("巴士")
                    .setContentText("正在使用定位...");
            Notification notification = downBuilder.build();
            notification.defaults = Notification.DEFAULT_ALL;
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            mNotificationManager.notify(LOCATION_TAG, 1, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭通知
     */
    public void close() {
        mNotificationManager.cancel(LOCATION_TAG, 1);
    }


}
