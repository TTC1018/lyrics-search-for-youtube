package com.example.lyricssearchforyoutube;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lyricssearchforyoutube.StrData.StrData;
import com.example.lyricssearchforyoutube.widget.WidgetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String youtubeMusicPackagePath = "com.google.android.apps.youtube.music";
    private WidgetManager widget;
    private boolean ACCESSIBILITY_PERMISSION;
    private ImageButton startBtn;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAccessibilityPermission();

        widget = WidgetManager.getWidget();
        widget.stopService(MainActivity.this);

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다른 앱 위에 그리기 권한 체크
                if(checkOverlayDisplayPermission()){
                    //플로팅 위젯 시작
                    widget.startService( MainActivity.this );

                    // youtube Music으로 화면전환
                    if( existPackage(youtubeMusicPackagePath) ){
                        // youtube music이 설치 되어있을시 화면 전환
                        Intent intent = getPackageManager().getLaunchIntentForPackage(youtubeMusicPackagePath);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        // 미설치시 youtube music 설치 화면으로 넘어감
                        String url = "market://details?id=" + youtubeMusicPackagePath;
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }

                    //본 앱은 종료
                    // finish();
                }
                else{
                    //권한이 없으므로 설정으로 전송
                    requestOverlayDisplayPermission();
                }
            }
        });

    }

    private boolean checkOverlayDisplayPermission() {
        // 안드로이드 마쉬멜로우 or API 23 이하 버전은
        // 다른 앱 위에 그리기 권한 필요없음
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // '다른 앱 위에 그리기 권한' 유효성 판단
            if (!Settings.canDrawOverlays(this)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void requestOverlayDisplayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("다른 앱 위에 표시");
        builder.setMessage("'다른 앱 위에 표시' 권한이 필요합니다.");
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 설정으로 이동
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, RESULT_OK);
            }
        });
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    private boolean checkAccessibilityPermission(){
        ACCESSIBILITY_PERMISSION = isAccessibilityServiceEnabled(getApplicationContext(), MyAccessibilityService.class);
        if(ACCESSIBILITY_PERMISSION){
            Toast.makeText(getApplicationContext(), "접근성 권한 허가됨", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "접근성 권한 허용 필요", Toast.LENGTH_LONG).show();
            requestAccessibilityPermission();
            return false;
        }
    }

    private boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }
        return false;
    }

    private void requestAccessibilityPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        builder.setCancelable(true);
        builder.setTitle("접근성 권한 필요");
        builder.setMessage("어플의 기능을 위해 '접근성 권한'을 허용해주세요.");
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 설정으로 이동
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }

    public boolean existPackage( String packagePath ) {
        boolean isExist = false;

        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith( packagePath )){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }


}