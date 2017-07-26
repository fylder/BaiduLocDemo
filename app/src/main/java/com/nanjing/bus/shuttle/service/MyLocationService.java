package com.nanjing.bus.shuttle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.LocationService;
import com.google.gson.Gson;
import com.nanjing.bus.shuttle.app.LocationApplication;
import com.nanjing.bus.shuttle.db.SharedTools;
import com.nanjing.bus.shuttle.event.MessageEvent;
import com.nanjing.bus.shuttle.event.ServiceEvent;
import com.nanjing.bus.shuttle.http.RetrofitClient;
import com.nanjing.bus.shuttle.http.api.LocationApi;
import com.nanjing.bus.shuttle.http.model.LineModel;
import com.nanjing.bus.shuttle.model.entity.LocationEntity;
import com.nanjing.bus.shuttle.notice.NoticeManager;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.LinkedList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.schedulers.Schedulers;

/**
 * Created by fylder on 2017/3/11.
 */

public class MyLocationService extends Service {

    private LocationService locService;
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果

    BDLocation mBdLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        locService = ((LocationApplication) getApplication()).locationService;

        locService.setLocationOption(getOption());
        locService.registerListener(listener);
    }

    private LocationClientOption getOption() {
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");
        mOption.setScanSpan(SharedTools.getDelay());//10s刷新
        return mOption;
    }


    /***
     *
     * @return DefaultLocationClientOption
     */
    private LocationClientOption getDefaultLocationClientOption() {
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        return mOption;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locService.start();
        NoticeManager.getInstance().showNotify();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        locService.unregisterListener(listener);
        locService.stop();
        NoticeManager.getInstance().close();
    }

//    void getLocation() {
//        if (locService != null) {
//            locService.start();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);
//                }
//            }, 10000);
//
//        }
//    }
//
//    Handler handler = new Handler(msg -> {
//        if (msg.what == 1) {
//            getLocation();
//        }
//        return false;
//    });


    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            // Thread: LocationClient
            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {

                mBdLocation = location;
                //接收定位结果
                String lineStr = SharedTools.getLine();
                LineModel model = new LineModel(lineStr, location.getLatitude(), location.getLongitude(), new Date().getTime() / 1000, location.getSpeed());
//                Logger.w(model.toString());

                Gson gson = new Gson();
                String json = gson.toJson(model);
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), json);
                RetrofitClient.getInstance(LocationApi.class)
                        .uploadLocation(requestBody)
                        .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                        .subscribe(s -> Logger.w("response:" + s), r -> Logger.e(r.getMessage()));

                int flag = 0;
                SharedTools.setLAT(location.getLatitude());
                SharedTools.setLON(location.getLongitude());
                EventBus.getDefault().post(new MessageEvent(location, flag));//发送消息
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(ServiceEvent msg) {
        if (msg.getFlag() == 1 && mBdLocation != null) {
            EventBus.getDefault().post(new MessageEvent(mBdLocation, 0));//发送消息
        }
    }


//    /***
//     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
//     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
//     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
//     *
//     * @param location test
//     * @return Bundle
//     */
//    private Bundle Algorithm(BDLocation location) {
//        Bundle locData = new Bundle();
//        double curSpeed = 0;
//        if (locationList.isEmpty() || locationList.size() < 2) {
//            LocationEntity temp = new LocationEntity();
//            temp.location = location;
//            temp.time = System.currentTimeMillis();
//            locData.putInt("iscalculate", 0);
//            locationList.add(temp);
//        } else {
//            if (locationList.size() > 5)
//                locationList.removeFirst();
//            double score = 0;
//            for (int i = 0; i < locationList.size(); ++i) {
//                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
//                        locationList.get(i).location.getLongitude());
//                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
//                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
//                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
//                score += curSpeed * Utils.EARTH_WEIGHT[i];
//            }
//            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
//                location.setLongitude(
//                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
//                                / 2);
//                location.setLatitude(
//                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
//                                / 2);
//                locData.putInt("iscalculate", 1);
//            } else {
//                locData.putInt("iscalculate", 0);
//            }
//            LocationEntity newLocation = new LocationEntity();
//            newLocation.location = location;
//            newLocation.time = System.currentTimeMillis();
//            locationList.add(newLocation);
//
//        }
//        return locData;
//    }


}
