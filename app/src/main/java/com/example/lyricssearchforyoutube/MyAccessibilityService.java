package com.example.lyricssearchforyoutube;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.lyricssearchforyoutube.StrData.StrData;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if(source == null) return;

        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        int target = -1;
        for(int i=0; i<source.getChildCount(); i++){
            list.add(source.getChild(i));
            if(list.get(i).getClassName().equals("android.widget.LinearLayout")){
                target = i;
                break;
            }
        }

        try{
            if(list.size() >= 2 && list.get(target) != null && list.get(target).getChildCount() >= 4){
                StrData.title = list.get(target).getChild(1).getText().toString();
                StrData.artist = list.get(target).getChild(3).getText().toString();
            }
        }catch(Exception e){
            Log.e("Accessibility", "Skipping Exception, keep research");
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected(){
        Toast.makeText(getApplicationContext(), "접근성 권한 켜짐", Toast.LENGTH_SHORT).show();
    }
}
