package com.nanjing.bus.shuttle.app;


import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.service.LocationService;
import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.Logger;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * <p>
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * <p>
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class LocationApplication extends Application {

    private static final String TAG = "fylder_tag";

    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        AppData.init(this);
        Logger.init(TAG);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());


    }
}
