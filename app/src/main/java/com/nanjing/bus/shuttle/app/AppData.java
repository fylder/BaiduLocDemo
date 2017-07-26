package com.nanjing.bus.shuttle.app;

import android.content.Context;

/**
 * Created by 剑指锁妖塔 on 16-1-12.
 */
public class AppData {

    static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
