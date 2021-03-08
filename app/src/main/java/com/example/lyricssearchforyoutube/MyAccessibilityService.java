package com.example.lyricssearchforyoutube;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.lyricssearchforyoutube.StrData.StrData;
import com.example.lyricssearchforyoutube.FloatingViewService;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if(source == null) return;

        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();

        try{
            int target = -1;
            for(int i=0; i<source.getChildCount(); i++){
                list.add(source.getChild(i));
                if(list.get(i).getClassName().equals("android.widget.LinearLayout")){
                    if(list.get(i).getChildCount() >= 4 && list.get(i).getChild(3).getText() != null){
                        target = i;
                        break;
                    }
                }
            }

            if(target != -1 && list.get(target) != null){
                StrData.title = list.get(target).getChild(1).getText().toString();
                StrData.artist = list.get(target).getChild(3).getText().toString();
                Log.e("UPDATE", "Title : " + StrData.title + ", Artist : " + StrData.artist);
            }
        }catch(Exception e){
            Log.e("Accessibility", "Skipping Exceptions, keep research");
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
