package com.example.lyricssearchforyoutube.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class GoogleLyricsService {

    public String getGoogleLyrics(String title ){
        final StringBuilder ly_builder = new StringBuilder();
        String lyrics = "";

        try{
            String url = "https://www.google.com/search?q="+ title;
            Document doc = Jsoup.connect(url).get();
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("span").append("\\n\\n");
            // ujudUb WRZytc
            for(Element element:doc.select("div.ujudUb")) {
                if(element.text().endsWith("더보기")) continue;
                lyrics += element.text().replaceAll("\\\\n", "\n");;
            }
            ly_builder.append(lyrics).append("\n");

        }catch (IOException e){
            ly_builder.append("error");
        }

        return lyrics;
    }

}
