//lyrics 전달하는 부분
package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class secondActivity extends AppCompatActivity {
    private Button getBtn1;
    private TextView result_lyrics;


    ArrayList<String> whole_lyrics = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        result_lyrics = (TextView)findViewById(R.id.result_lyrics);
        getBtn1 = (Button) findViewById(R.id.getBtn1);
        getBtn1.setOnClickListener((view)-> {getWebsite1();});
    }
    private void getWebsite1(){
        new Thread((Runnable) ()->{
            final StringBuilder ly_builder = new StringBuilder();


            try{
              String url ="https://music.bugs.co.kr/track/6065263";
                Document doc = Jsoup.connect(url).get();

                Elements lyrics = doc.select("div.lyricsContainer xmp");
                    String s = lyrics.text();
                    whole_lyrics.add(s);



                ly_builder.append(whole_lyrics).append("\n");


            }catch (IOException e){
                ly_builder.append("error");

            }
            runOnUiThread(() ->{
                result_lyrics.setText(ly_builder.toString());

            });
        }).start();
    }
}