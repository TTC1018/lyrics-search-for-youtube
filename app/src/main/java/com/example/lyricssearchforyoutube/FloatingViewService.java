package com.example.lyricssearchforyoutube;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.lyricssearchforyoutube.StrData.StrData;

public class FloatingViewService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams floatWindowLayoutParam;
    private ViewGroup mFloatingView;
    private View buttonView, panelView, listView;
    private ImageView floatBtn, closeBtn, loadBtn;
    private int LAYOUT_TYPE;

    public FloatingViewService() { }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //floating_widget.xml 읽어오기
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = (ViewGroup) inflater.inflate(R.layout.floating_widget, null);

        closeBtn = mFloatingView.findViewById(R.id.closeButton);
        floatBtn = mFloatingView.findViewById(R.id.floatButton);
        loadBtn = mFloatingView.findViewById(R.id.loadButton);
        buttonView = mFloatingView.findViewById(R.id.buttonLayout);
        panelView = mFloatingView.findViewById(R.id.panelLayout);
        listView = mFloatingView.findViewById(R.id.listLayout);

        loadBtn.setOnClickListener(this);
        floatBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        panelView.findViewById(R.id.homeButton).setOnClickListener(this);
        LAYOUT_TYPE = findLayoutType();

        floatWindowLayoutParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        //시작 위치 (상단 중앙)
        floatWindowLayoutParam.gravity = Gravity.TOP;
        floatWindowLayoutParam.x = 0;
        floatWindowLayoutParam.y = 0;

        mWindowManager.addView(mFloatingView, floatWindowLayoutParam);

        mFloatingView.setOnTouchListener(makeTouchListener());
        floatBtn.setOnTouchListener(makeTouchListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                buttonView.setVisibility(View.VISIBLE);
                panelView.setVisibility(View.GONE);
                break;

            case R.id.loadButton:
                ((TextView)panelView.findViewById(R.id.textView1)).setText(StrData.title);
                ((TextView)panelView.findViewById(R.id.textView2)).setText(StrData.artist);
                listView.setVisibility(View.VISIBLE);
                break;

            case R.id.floatButton:
                buttonView.setVisibility(View.GONE);
                panelView.setVisibility(View.VISIBLE);
                break;

            case R.id.closeButton:
                stopSelf();
                mWindowManager.removeView(mFloatingView);
                Intent backToHome = new Intent(FloatingViewService.this, MainActivity.class);
                backToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(backToHome);
                break;
        }
    }

    private View.OnTouchListener makeTouchListener(){
        View.OnTouchListener otl = new View.OnTouchListener(){
            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatWindowLayoutParam;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParam.x;
                        y = floatWindowLayoutUpdateParam.y;

                        px = event.getRawX();
                        py = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);
                        mWindowManager.updateViewLayout(mFloatingView, floatWindowLayoutUpdateParam);
                        break;
                }
                return false;
            }
        };

        return otl;
    }

    private int findLayoutType(){
        int LAYOUT_TYPE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26 이상일시, TYPE_APPLICATION_OVERLAY 필요
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // API 26 미만
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }
        return LAYOUT_TYPE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        mWindowManager.removeView(mFloatingView);
    }
}
