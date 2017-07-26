package com.nanjing.bus.shuttle;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nanjing.bus.shuttle.db.SharedTools;
import com.nanjing.bus.shuttle.event.MessageEvent;
import com.nanjing.bus.shuttle.event.ServiceEvent;
import com.nanjing.bus.shuttle.service.MyLocationService;
import com.nanjing.bus.shuttle.utils.ServiceTools;
import com.nanjing.bus.shuttle.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DemoActivity extends AppCompatActivity {

    @BindView(R.id.demo_mapView)
    MapView mMapView;
    @BindView(R.id.demo_line_btn)
    Button startBtn;
    @BindView(R.id.demo_line_edit)
    EditText lineEdit;
    @BindView(R.id.demo_info_text)
    TextView infoText;
    private BaiduMap mBaiduMap;


    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        double lat = SharedTools.getLAT();
        double lon = SharedTools.getLON();
        if (lat != 0 && lon != 0) {
            LatLng cenpt = new LatLng(lat, lon);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(17).build();

            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            if (null != mMapStatusUpdate) {
                mBaiduMap.setMapStatus(mMapStatusUpdate);
            }
        }
//        reset.setOnClickListener((View.OnClickListener) v -> {
//            // TODO Auto-generated method stub
//            if (mBaiduMap != null)
//                mBaiduMap.clear();
//        });

        if (ServiceTools.isRunning()) {
            Logger.w("service is running");
            EventBus.getDefault().post(new ServiceEvent(1));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    void init() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        lineEdit.setText(SharedTools.getLine());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 从MyService收到消息
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {/* Do something */
        BDLocation location = msg.getLocation();
        int iscal = msg.getFlag();
        if (location != null) {

            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            // 构建Marker图标
            BitmapDescriptor bitmap = null;
            if (iscal == 0) {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaji); // 非推算结果
            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
            }

            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));

            String info = "刷新时间:" + TimeUtils.getTime() +
                    "\n位置信息:地址-" + location.getAddress().address +
                    "\n经度:" + location.getLatitude() + ",纬度:" + location.getLongitude() + ",速度:" + location.getSpeed() + ",时间:" + location.getTime();
            infoText.setText(info);

            String s = startBtn.getText().toString();
            if (!s.equals(getString(R.string.location_finish))) {
                startBtn.setText(R.string.location_finish);
                startBtn.setBackgroundResource(R.color.colorAccent);
            }
        }
    }


    /**
     * 开始定位
     */
    @OnClick(R.id.demo_line_btn)
    void clickLocation() {

        String lineStr = lineEdit.getText().toString().trim();
        if (TextUtils.isEmpty(lineStr)) {
            Toast.makeText(this, "请输入路线", Toast.LENGTH_SHORT).show();
        } else {
            SharedTools.setLine(lineStr);
            String s = startBtn.getText().toString();
            Intent intent = new Intent(this, MyLocationService.class);
            if (s.equals(getString(R.string.location_finish))) {
                //切换结束状态
                stopService(intent);
                startBtn.setText(R.string.location_start);
                startBtn.setBackgroundResource(R.color.colorPrimary);
                infoText.setText(R.string.location_stop);
            } else {
                //切换定位状态
                startService(intent);
                startBtn.setText(R.string.location_finish);
                startBtn.setBackgroundResource(R.color.colorAccent);
                infoText.setText(R.string.location_info);
            }
        }
    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    long backTime;

    @Override
    public void onBackPressed() {
        long timeNow = new Date().getTime();
        if (timeNow - backTime > 2000) {
            Toast.makeText(this, "再按退出", Toast.LENGTH_SHORT).show();
            backTime = timeNow;
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }

}
