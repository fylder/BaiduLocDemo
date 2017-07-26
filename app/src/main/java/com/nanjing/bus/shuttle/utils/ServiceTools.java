package com.nanjing.bus.shuttle.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.nanjing.bus.shuttle.app.AppData;

import java.util.ArrayList;

/**
 * Created by fylder on 2017/3/11.
 */

public class ServiceTools {

    /**
     * 检查Service是否正在进行
     *
     * @return
     */
    public static boolean isRunning() {
        ActivityManager myManager = (ActivityManager) AppData.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            String s = runningService.get(i).service.getClassName();
            Log.w("check", "service name : " + s);
            if (s.equals("com.nanjing.bus.shuttle.service.MyLocationService")) {
                return true;
            }
        }
        return false;
    }
}
