package com.example.lyricssearchforyoutube.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class GoogleLyricsService {

    public String getGoolgeLyrics(String title ){
        ArrayList<String> lyrics = new ArrayList<String>();
        final StringBuilder t_builder = new StringBuilder();

        try{
            String url = "https://www.google.com/search?q="+ title;
            Document doc = Jsoup.connect(url).get();

            for(Element element:doc.select("div.ujudUb")) {
                String s = element.text();
                lyrics.add(s);
                lyrics.add("\n");
            }
            t_builder.append(lyrics).append("\n");

        }catch (IOException e){
            t_builder.append("error");
        }

        return lyrics.toString();
    }

}
