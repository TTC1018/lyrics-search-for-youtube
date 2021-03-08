package com.example.lyrics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class googleLyrics {
    public ArrayList<String> goolgeLyrics(String title ){
        ArrayList<String> lyrics = new ArrayList<String>();
        new Thread((Runnable) ()->{
            final StringBuilder t_builder = new StringBuilder();

            try{
                String url = "https://www.google.com/search?q="+ title;
                Document doc = Jsoup.connect(url).get();

                for(Element t:doc.select("div.ujudUb")) {
                    String s = t.text();
                    lyrics.add(s);
                }
                t_builder.append(lyrics).append("\n");

            }catch (IOException e){
                t_builder.append("error");
            }
        }).start();
        return(lyrics);
    }
}
