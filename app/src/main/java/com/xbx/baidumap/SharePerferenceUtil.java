package com.xbx.baidumap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EricYuan on 2016/3/15.
 */
public class SharePerferenceUtil {
    /**
     * 保存当前位置信息
     * @param context
     * @param locationBean
     */
    public static void setLocationInfo(Context context,
                                       LocationBean locationBean) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        if (null == locationBean) {
        } else {
            SharedPreferences.Editor editor = defaultSharedPreferences.edit();
            if (!StringUtil.isNull(locationBean.getCity())) {
                editor.putString("city", locationBean.getCity());
            }
            if (!StringUtil.isNull(locationBean.getDetailAddress())) {
                editor.putString("detailAddress", locationBean.getDetailAddress());
            }
            if (!StringUtil.isNull(locationBean.getLon()) && !StringUtil.isNull(locationBean.getLat())) {
                editor.putString("lon", locationBean.getLon()).putString("lat",
                        locationBean.getLat());
            }
            editor.commit();
        }
    }
    /**
     * 获取保存的位置信息
     * @param context
     * @return
     */
    public static LocationBean getLocationInfo(Context context) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        LocationBean locationBean = new LocationBean();
        locationBean.setLon(defaultSharedPreferences.getString("lon", null));
        locationBean.setLat(defaultSharedPreferences.getString("lat", null));
        locationBean.setCity(defaultSharedPreferences.getString("city", null));
        locationBean.setDetailAddress(defaultSharedPreferences.getString("detailAddress", null));
        return locationBean;
    }
}
