package com.example.lyricssearchforyoutube.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class GoogleLyricsService {

    public String getGoogleLyrics(String title ){
//        ArrayList<String> lyrics = new ArrayList<String>();
        String lyrics = "";
        final StringBuilder t_builder = new StringBuilder();

        try{
            String url = "https://www.google.com/search?q="+ title;
            Document doc = Jsoup.connect(url).get();
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("span").append("\\n\\n");

            for(Element element:doc.select("div.ujudUb")) {
                if(element.text().endsWith("더보기")) continue;
                lyrics += element.text().replaceAll("\\\\n", "\n");;
            }
            t_builder.append(lyrics).append("\n");

        }catch (IOException e){
            t_builder.append("error");
        }

        return lyrics;
    }

}
