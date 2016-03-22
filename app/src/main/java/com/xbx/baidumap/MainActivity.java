package com.xbx.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button start_move_btn;
    private Button start_notification_btn;
    private Button start_user_btn;

    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Marker mMarkerA;
    private Marker mMarkerB;

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_markb);

    private List<LatLng> listA;
    private List<LatLng> listB;

    private int countA = 0;
    private int countB = 0;

    private LocationBean locationBean = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(countA < listA.size()){
                        handler.sendEmptyMessageDelayed(0,2000);
                        mMarkerA.setPosition(listA.get(countA));
                        countA++;
                    }
                    break;
                case 1:
                    if(countB < listB.size()){
                        handler.sendEmptyMessageDelayed(1,2000);
                        mMarkerB.setPosition(listB.get(countB));
                        countB++;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
        initOverlay();
    }

    private void initView(){
        start_move_btn = (Button) findViewById(R.id.start_move_btn);
        start_notification_btn = (Button) findViewById(R.id.start_notification_btn);
        start_user_btn = (Button) findViewById(R.id.start_user_btn);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        locationBean = SharePerferenceUtil.getLocationInfo(MainActivity.this);
        LatLng ll = new LatLng(30.554328,104.075372);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        start_move_btn.setOnClickListener(this);
        start_notification_btn.setOnClickListener(this);
        start_user_btn.setOnClickListener(this);
    }

    private void initOverlay() {
        MarkerOptions ooA = new MarkerOptions().position(listA.get(countA)).icon(bdA)
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MarkerOptions ooB = new MarkerOptions().position(listB.get(countB)).icon(bdB)
                .zIndex(5);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
    }

    private void initDatas() {
        LatLng llA = new LatLng(30.55336, 104.074271);
        LatLng llB= new LatLng(30.55336, 104.075008);
        LatLng llC= new LatLng(30.553407, 104.075421);
        LatLng llD= new LatLng(30.554348, 104.075367);

        LatLng llE = new LatLng(30.557279, 104.076939);
        LatLng llF= new LatLng(30.557302, 104.075906);
        LatLng llG= new LatLng(30.556618, 104.075852);
        LatLng llH= new LatLng(30.554907, 104.075412);

        listA = new ArrayList<>();
        listB = new ArrayList<>();

        listA.add(llA);
        listA.add(llB);
        listA.add(llC);
        listA.add(llD);
        listB.add(llE);
        listB.add(llF);
        listB.add(llG);
        listB.add(llH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_move_btn:
                handler.sendEmptyMessageDelayed(0,2000);
                handler.sendEmptyMessageDelayed(1,2000);
                countA = 0;
                countB = 0;
                break;
            case R.id.start_notification_btn:
                startActivity(new Intent(MainActivity.this,NotificationActivity.class));
                break;
            case R.id.start_user_btn:
                startActivity(new Intent(MainActivity.this,UserOnMapActivity.class));
                break;
        }
    }
}
