package com.example.lyricssearchforyoutube;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import androidx.annotation.Nullable;

import com.example.lyricssearchforyoutube.StrData.StrData;

import java.io.IOException;
import java.util.ArrayList;

public class FloatingViewService extends Service implements View.OnClickListener {

    MyHandler mHandler;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams floatWindowLayoutParam;
    private ViewGroup mFloatingView;
    private static View buttonView, panelView, listView, scrollView;
    private ImageView floatBtn, closeBtn, loadBtn, listBtn;
    private static TextView songBtn[];
    private int LAYOUT_TYPE;
    private Thread lyricsThr;

    public FloatingViewService() { }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new MyHandler();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //floating_widget.xml 읽어오기
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = (ViewGroup) inflater.inflate(R.layout.floating_widget, null);

        closeBtn = mFloatingView.findViewById(R.id.closeButton);
        floatBtn = mFloatingView.findViewById(R.id.floatButton);
        listBtn = mFloatingView.findViewById(R.id.listButton);
        loadBtn = mFloatingView.findViewById(R.id.loadButton);
        buttonView = mFloatingView.findViewById(R.id.buttonLayout);
        panelView = mFloatingView.findViewById(R.id.panelLayout);
        listView = mFloatingView.findViewById(R.id.listLayout);
        scrollView = mFloatingView.findViewById(R.id.scrollView);
        songBtn = new TextView[5];
        songBtn[0] = listView.findViewById(R.id.song1);
        songBtn[1] = listView.findViewById(R.id.song2);
        songBtn[2] = listView.findViewById(R.id.song3);
        songBtn[3] = listView.findViewById(R.id.song4);
        songBtn[4] = listView.findViewById(R.id.song5);


        loadBtn.setOnClickListener(this);
        floatBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);
        for(int i=0; i<5; i++) songBtn[i].setOnClickListener(this);
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
                ((TextView)panelView.findViewById(R.id.titleView)).setText(StrData.title);
                ((TextView)panelView.findViewById(R.id.artistView)).setText(StrData.artist);
                for(int i=0; i<5; i++) songBtn[i].setText("");
                songBtn[0].setText("노래 검색 중..");
                getInforms();
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

            case R.id.song1:
                getLyrics(1);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                listBtn.setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.song2:
                getLyrics(2);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                listBtn.setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.song3:
                getLyrics(3);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                listBtn.setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.song4:
                getLyrics(4);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                listBtn.setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.song5:
                getLyrics(5);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                listBtn.setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.INVISIBLE);
                break;

            case R.id.listButton:
                listBtn.setVisibility(View.INVISIBLE);
                ((TextView)scrollView.findViewById(R.id.lyricsView)).setText("");
                scrollView.setVisibility(View.GONE);
                for(int i=0; i<5; i++) songBtn[i].setVisibility(View.VISIBLE);
                loadBtn.setVisibility(View.VISIBLE);
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

    private void getInforms(){
        lyricsThr = new Thread(){
            @Override
            public void run(){
                    ArrayList<String> titles = new ArrayList<String>();
                    ArrayList<String> artists = new ArrayList<String>();
                    ArrayList<String> links = new ArrayList<String>();
                    try{
                        String words[] = StrData.title.split("\\(");
                        String url = "https://music.bugs.co.kr/search/integrated?q="+ words[0];
                        Document doc = Jsoup.connect(url).get();

                        for(Element title:doc.select(".lyrics .title a")) {
                            String s = title.text();
                            titles.add(s);
                        }
                        for(Element artist:doc.select(".lyrics .artist a")) {
                            String s = artist.text();
                            artists.add(s);
                        }
                        for(Element lyrics:doc.select("tr .lyrics a")) {
                            links.add(lyrics.attr("abs:href"));
                        }

                    }catch (IOException e){
                        Log.e("LYRICSSEARCH", "Search Exception, keep search");
                    }
                    for(int i=0; i<titles.size(); i++){
                        StrData.titles[i] = titles.get(i);
                        StrData.artists[i] = artists.get(i);
                        StrData.links[i] = links.get(i);
                    }
                    mHandler.sendEmptyMessage(1000);
            }
        };
        lyricsThr.start();
    }

    private void getLyrics(int order){
        new Thread() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1999);
                try{
                    String url = StrData.links[order-1];
                    Document doc = Jsoup.connect(url).get();
                    doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
                    doc.select("br").append("\\n");
                    doc.select("p").prepend("\\n\\n");

                    Elements lyrics = doc.select("div.lyricsContainer xmp");
                    StrData.lyrics = lyrics.html().replaceAll("\\\\n", "\n");
                }catch (IOException e){
                    Log.e("GETLYRICS", "failed to get lyrics");
                }

                if(StrData.lyrics.equals("")){
                    mHandler.sendEmptyMessage(2001);
                }else{
                    mHandler.sendEmptyMessage(2000);
                }

            }

        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        mWindowManager.removeView(mFloatingView);
    }

    public static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1000:
                    songBtn[0].setText(StrData.titles[0] + " - " + StrData.artists[0]);
                    songBtn[1].setText(StrData.titles[1] + " - " + StrData.artists[1]);
                    songBtn[2].setText(StrData.titles[2] + " - " + StrData.artists[2]);
                    songBtn[3].setText(StrData.titles[3] + " - " + StrData.artists[3]);
                    songBtn[4].setText(StrData.titles[4] + " - " + StrData.artists[4]);
                    break;
                case 1999:
                    ((TextView)scrollView.findViewById(R.id.lyricsView)).setText("불러오는 중...");
                    break;
                case 2000:
                    ((TextView)scrollView.findViewById(R.id.lyricsView)).setText(StrData.lyrics);
                    break;
                case 2001:
                    ((TextView)scrollView.findViewById(R.id.lyricsView)).setText("성인인증이 필요한 곡은 불러올 수 없습니다.");
                    break;
            }
        }
    }
}
