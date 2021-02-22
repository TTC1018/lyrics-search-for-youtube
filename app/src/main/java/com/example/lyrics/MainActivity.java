//title, artist, lyrics_link 전달하는 부분
package com.example.lyrics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button getBtn;
    private TextView title_list;
    private TextView artist_list;
    private TextView lyrics_link_list;
    private String word ="celebrity";
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> artists = new ArrayList<String>();
    ArrayList<String> links = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title_list = (TextView)findViewById(R.id.title_list);
        artist_list = (TextView)findViewById(R.id.artist_list);
        lyrics_link_list = (TextView)findViewById(R.id.lyrics_link_list);
        getBtn = (Button) findViewById(R.id.getBtn);
        getBtn.setOnClickListener((view)-> {getWebsite();});
    }
    private void getWebsite(){
        new Thread((Runnable) ()->{
            final StringBuilder t_builder = new StringBuilder();
            final StringBuilder a_builder = new StringBuilder();
            final StringBuilder l_builder = new StringBuilder();

            try{
                String url = "https://music.bugs.co.kr/search/integrated?q="+ word;
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
                t_builder.append(titles).append("\n");
                a_builder.append(artists).append("\n");
                l_builder.append(links).append("\n");

            }catch (IOException e){
                t_builder.append("error");

        }
        runOnUiThread(() ->{
            title_list.setText(t_builder.toString());
            artist_list.setText(a_builder.toString());
            lyrics_link_list.setText(l_builder.toString());
        });
        }).start();
    }

}