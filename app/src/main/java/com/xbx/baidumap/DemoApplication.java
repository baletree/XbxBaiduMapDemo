package com.xbx.baidumap;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by EricYuan on 2016/3/15.
 */
public class DemoApplication extends Application {

    public LocationClient mLocationClient;
    public MyLocationListener myListener;
    private boolean isFirstLoc = true; // 是否首次定位

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        initLocate();
    }

    /**初始化地图信息*/
    private void initLocate() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
//        mGeofenceClient = new GeofenceClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll");
        option.setScanSpan(15 * 1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    /**停止定位*/
    public void stopLocate() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                if (isFirstLoc) {
                    Log.i("Tag", "Application==>" + location.getLatitude() + " " + location.getLongitude()
                            + "" + location.getCity() + location.getAddrStr());
                    isFirstLoc = false;
                    LocationBean locationBean = new LocationBean();
                    locationBean.setCity(location.getCity());
                    locationBean.setLat("" + location.getLatitude());
                    locationBean.setLon("" + location.getLongitude());
                    locationBean.setDetailAddress(location.getAddrStr());
                    SharePerferenceUtil.setLocationInfo(getApplicationContext(),
                            locationBean);
                }
                stopLocate();
            }else {
                //定位失败
                Log.i("Tag","定位失败");
            }
        }
    }
}