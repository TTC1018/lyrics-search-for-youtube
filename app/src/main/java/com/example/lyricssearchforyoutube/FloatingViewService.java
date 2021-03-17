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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.lyricssearchforyoutube.StrData.StrData;
import com.example.lyricssearchforyoutube.data.Music;
import com.example.lyricssearchforyoutube.parsing.BugsLyricsService;
import com.example.lyricssearchforyoutube.parsing.GoogleLyricsService;

import java.util.List;

public class FloatingViewService extends Service implements View.OnClickListener {

    MyHandler mHandler;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams floatWindowLayoutParam;
    private ViewGroup mFloatingView;

    private int LAYOUT_TYPE;
    private Thread lyricsThr;


    private BugsLyricsService bugsLyricsService;
    private GoogleLyricsService googleLyricsService;
    List<Music> musicList;

    private View collapsedLayout, expandedLayout, appHeadLayout, appBodyLayout, appBottomLayout, scrollView;
    private ImageView expandedButton, closeButton, homeButton, searchButton;
    private TextView parsingTitleTextView, parsingArtistTextView;
    private Spinner strategySpinner;
    private EditText searchEditText;

    private String searchStrategy = "벅스";
 //   private InputMethodManager imm;

    private static View songBtn[];
    private static TextView songTitle[];
    private static TextView songArtist[];
    private static TextView lyricsView;


    public FloatingViewService() { }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new MyHandler();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        bugsLyricsService = new BugsLyricsService();
        googleLyricsService = new GoogleLyricsService();

        //floating_widget.xml 읽어오기
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = (ViewGroup) inflater.inflate(R.layout.floating_widget, null);

        // 작은 화면
        collapsedLayout = mFloatingView.findViewById(R.id.collapsedLayout);
        closeButton     = collapsedLayout.findViewById(R.id.closeButton);
        expandedButton  = collapsedLayout.findViewById(R.id.expandedButton);

        // 확장 화면
        expandedLayout  = mFloatingView.findViewById(R.id.expandedLayout);
        // 확장 화면 헤더 : psring 정보들
        appHeadLayout         = expandedLayout.findViewById(R.id.appHeadLayout);
        parsingTitleTextView  = appHeadLayout.findViewById(R.id.parsingTitleTextView);
        parsingArtistTextView = appHeadLayout.findViewById(R.id.parsingArtistTextView);
        strategySpinner       = appHeadLayout.findViewById(R.id.strategySpinner);
        // 확장 화면 바디 : 가사 리스트업 / 가사 표시하는 frame
        appBodyLayout   = expandedLayout.findViewById(R.id.appBodyLayout);
        songBtn         = new View[5];
        songBtn[0]      = appBodyLayout.findViewById(R.id.song1);
        songBtn[1]      = appBodyLayout.findViewById(R.id.song2);
        songBtn[2]      = appBodyLayout.findViewById(R.id.song3);
        songBtn[3]      = appBodyLayout.findViewById(R.id.song4);
        songBtn[4]      = appBodyLayout.findViewById(R.id.song5);
        songTitle       = new TextView[5];
        songTitle[0]    = appBodyLayout.findViewById(R.id.song1TitleTextView);
        songTitle[1]    = appBodyLayout.findViewById(R.id.song2TitleTextView);
        songTitle[2]    = appBodyLayout.findViewById(R.id.song3TitleTextView);
        songTitle[3]    = appBodyLayout.findViewById(R.id.song4TitleTextView);
        songTitle[4]    = appBodyLayout.findViewById(R.id.song5TitleTextView);
        songArtist      = new TextView[5];
        songArtist[0]   = appBodyLayout.findViewById(R.id.song1ArtistTextView);
        songArtist[1]   = appBodyLayout.findViewById(R.id.song2ArtistTextView);
        songArtist[2]   = appBodyLayout.findViewById(R.id.song3ArtistTextView);
        songArtist[3]   = appBodyLayout.findViewById(R.id.song4ArtistTextView);
        songArtist[4]   = appBodyLayout.findViewById(R.id.song5ArtistTextView);
        scrollView      = appBodyLayout.findViewById(R.id.scrollView);
        lyricsView      = scrollView.findViewById(R.id.lyricsView);
        // 확장 화면 바텀 : 축소버튼, 검색값 직접입력, 검색버튼
        appBottomLayout = expandedLayout.findViewById(R.id.appBottomLayout);
        homeButton      = mFloatingView.findViewById(R.id.homeButton);
        searchEditText  = mFloatingView.findViewById(R.id.searchEditText);
        searchButton    = mFloatingView.findViewById(R.id.searchButton);


        // Spinner설정
        strategySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(strategySpinner.getSelectedItemPosition() >= 0) {
                    String selectedItem = strategySpinner.getSelectedItem().toString();
                    switch(selectedItem) {
                        case "벅스":
                            searchStrategy = "벅스";
                            break;
                        case "구글":
                            searchStrategy = "구글";
                            break;
                        case "직접입력":
                            searchStrategy = "직접입력";
                            break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        expandedButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        for(int i=0; i<5; i++) songBtn[i].setOnClickListener(this);
        searchEditText.setOnClickListener(this);
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
        expandedButton.setOnTouchListener(makeTouchListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeButton:
                // 종료버튼
                stopSelf();
                mWindowManager.removeView(mFloatingView);
                Intent backToHome = new Intent(FloatingViewService.this, MainActivity.class);
                backToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(backToHome);
                break;
            case R.id.expandedButton:
                // 화면 확장
                collapsedLayout.setVisibility(View.GONE);
                expandedLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.homeButton:
                // 화면 축소
                collapsedLayout.setVisibility(View.VISIBLE);
                expandedLayout.setVisibility(View.GONE);
                break;


            case R.id.searchButton:
                // parsing된 데이터 적용
                setParsingData();
                searchService();

                switch (searchStrategy) {
                    case "직접입력":
                    case "벅스":
                        openSongList();
                        break;
                    case "구글":
                        openLyricsView();
                        break;
                }
                break;

            case R.id.song1:
                openLyricsView(0);
                break;
            case R.id.song2:
                openLyricsView(1);
                break;
            case R.id.song3:
                openLyricsView(2);
                break;
            case R.id.song4:
                openLyricsView(3);
                break;
            case R.id.song5:
                openLyricsView(4);
                break;

            case R.id.searchEditText:
                if( searchStrategy.equals( "직접입력" ) ){
                    searchEditText.clearFocus();
                    searchEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchEditText, 0);

                }
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

    private void setParsingData(){
        parsingTitleTextView.setText(StrData.parsingTitle);
        parsingArtistTextView.setText(StrData.parsingArtist);
    }


    private void searchService(){
        lyricsThr = new Thread() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1999);

                String musicQuery = getSearchQuery();

                Log.e("musicQuery", musicQuery);

                switch (searchStrategy) {
                    case "직접입력":
                        musicQuery = searchEditText.getText().toString(); // editText data
                    case "벅스":
                        musicList = bugsLyricsService.searchMusic(musicQuery);

                        for( int i = 0 ; i < 5 ; i++ ){
                            if ( i < musicList.size() ) StrData.music[i] = musicList.get(i);
                            else StrData.music[i] = new Music();
                        }

                        mHandler.sendEmptyMessage(1000);
                        break;
                    case "구글":
                        String lyrics = googleLyricsService.getGoolgeLyrics(musicQuery);
                        StrData.lyrics = lyrics;

                        if(StrData.lyrics.equals("")){
                            mHandler.sendEmptyMessage(2001);
                        }else{
                            mHandler.sendEmptyMessage(2000);
                        }
                        break;
                }
            }
        };
        lyricsThr.start();
    }

    private String getSearchQuery(){
        String musicQuery = StrData.parsingTitle; // parsing data
        int featuring = musicQuery.indexOf( "(" );
        if( featuring != -1 ) musicQuery = musicQuery.substring( 0,  featuring);

        return musicQuery;
    }

    private void openSongList(){
        appBodyLayout.setVisibility(View.VISIBLE);
        for(int i=0; i<5; i++) songBtn[i].setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }


    private void openLyricsView(){
        lyricsView.setText( StrData.lyrics );

        for(int i=0; i<5; i++) songBtn[i].setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void openLyricsView( int songNum ){
        if( StrData.music[songNum].getLyricsLink() == "" ) return;

        new Thread() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1999);

                String link = StrData.music[songNum].getLyricsLink();
                String lyrics = bugsLyricsService.getBugsLyrics(link);
                StrData.lyrics = lyrics;


                if(StrData.lyrics.equals("")){
                    mHandler.sendEmptyMessage(2001);
                }else{
                    mHandler.sendEmptyMessage(2000);
                }

            }
        }.start();

        openLyricsView();
    }





    public static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1000:
                    for( int i = 0 ; i < 5 ; i++ ) {
                        songTitle[i].setText( StrData.music[i].getTitle() );
                        songArtist[i].setText( StrData.music[i].getArtist() );
                    }
                    break;
                case 1999:
                    lyricsView.setText("불러오는 중...");
                    break;
                case 2000:
                    lyricsView.setText(StrData.lyrics);
                    break;
                case 2001:
                    lyricsView.setText("성인인증이 필요한 곡은 불러올 수 없습니다.");
                    break;
            }
        }
    }
}
