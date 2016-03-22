package com.xbx.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/21.
 */
public class UserOnMapActivity extends Activity implements BDLocationListener,
        BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback {
    private MapView mapView;
    private ImageView user_marki_img;

    private BaiduMap mBaiduMap;
    private LocationClient locationClient;
    private GeoCoder geoCoder;
    //当前位置
    private LatLng latLngCurrent;

    private View markerView;
    private BitmapDescriptor markerIcon;

    private List<LatLng> latLngList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapuser);
        mapView = (MapView) findViewById(R.id.user_map);
        user_marki_img = (ImageView) findViewById(R.id.user_marki_img);
        initViews();
    }

    private void initViews() {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(20000);
        locationClient.setLocOption(option);
        geoCoder = GeoCoder.newInstance();
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        if (zoom != null) {
            zoom.setVisibility(View.GONE);
        }
        if (locationClient != null) {
            locationClient.start();
        }
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
        LocationBean locationBean = SharePerferenceUtil.getLocationInfo(UserOnMapActivity.this);
        LatLng nowLl = new LatLng(locationBean.getTargetLat(),locationBean.getTargetLon());
        MapStatus mMapstatus = new MapStatus.Builder().target(nowLl).zoom(18f)
                .build();
        MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapstatus);
        mBaiduMap.setMapStatus(u);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null || mapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mapView.getMap().setMyLocationData(locData);
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        Log.i("Tag", "getLatitude=>" + bdLocation.getLatitude() + "/getLongitude=>" + bdLocation.getLongitude());
        if (latLngCurrent == null) {
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mapView.getMap().animateMapStatus(u);
        }
        latLngCurrent = latLng;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLngCurrent));
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null) {
            return;
        }
        if (reverseGeoCodeResult.getLocation() == null) {
            return;
        }
        Log.i("Tag", "推动后位置：" + reverseGeoCodeResult.getAddress());
        LatLng latLng = reverseGeoCodeResult.getLocation();
        initLatLng(latLng);
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
        }
        mapView.onDestroy();
        mapView = null;
    }

    private void resetUser(){
        mBaiduMap.clear();
        for(int i = 0;i<latLngList.size();i++){
            markerView = LayoutInflater.from(this).inflate(R.layout.userinfo_show, null);
            markerIcon = BitmapDescriptorFactory.fromBitmap(Utils.getBitmapFromView(markerView));
            OverlayOptions options = new MarkerOptions().position(new LatLng(latLngList.get(i).latitude, latLngList.get(i).longitude)).icon(markerIcon);
            mBaiduMap.addOverlay(options);
            mapView.invalidate();
        }
    }

    private void initLatLng(LatLng latLng) {
        latLngList = new ArrayList<>();
        double lat = latLng.latitude;
        double lgt = latLng.longitude;
        for (int i = 0; i < 8; i++) {
            LatLng ll = null;
            switch (i){
                case 0:
                    ll = new LatLng(lat + 0.001500, lgt - 0.001500);
                    break;
                case 1:
                    ll = new LatLng(lat, lgt - 0.001500);
                    break;
                case 2:
                    ll = new LatLng(lat - 0.001500, lgt - 0.001500);
                    break;
                case 3:
                    ll = new LatLng(lat - 0.001200, lgt);
                    break;
                case 4:
                    ll = new LatLng(lat - 0.001500, lgt + 0.001500);
                    break;
                case 5:
                    ll = new LatLng(lat, lgt + 0.001500);
                    break;
                case 6:
                    ll = new LatLng(lat + 0.001500, lgt + 0.001500);
                    break;
                case 7:
                    ll = new LatLng(lat + 0.001000, lgt);
                    break;
            }
            latLngList.add(ll);
        }
        if(latLngList.size() > 0){
            resetUser();
        }
    }
}
