package com.example.lyricssearchforyoutube.widget;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.example.lyricssearchforyoutube.FloatingViewService;

public class WidgetManager {
    private WidgetManager(){ }

    private static class WigetHelper{
        private static final WidgetManager INSTANCE = new WidgetManager();
    }

    public static WidgetManager getWidget(){
        return WigetHelper.INSTANCE;
    }

    public void stopService(Activity activity){
        if( isMyServiceRunning(activity) ) {
            activity.stopService(new Intent(activity, FloatingViewService.class));
        }
    }

    public void startService(Activity activity){
        if( !isMyServiceRunning(activity) ) {
            activity.startService(new Intent(activity, FloatingViewService.class));
        }
    }

    private boolean isMyServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        // 현재 실행중인 어플 중 플로팅 위젯이 실행중인지를 판단
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FloatingViewService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
