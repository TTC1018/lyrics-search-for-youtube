package com.example.lyricssearchforyoutube;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if(source == null) return;

        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        for(int i=0; i<source.getChildCount(); i++){
            list.add(source.getChild(i));
        }

        try{
            if(list.size() >= 2 && list.get(1) != null && list.get(1).getChildCount() >= 4){
                String title = list.get(1).getChild(1).getText().toString();
                String artist = list.get(1).getChild(3).getText().toString();
                System.out.println("title : " + title + "\nartist : " + artist);
            }
        }catch(Exception e){
            System.out.println("Skipping Exception, keep research");
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected(){

    }
}
