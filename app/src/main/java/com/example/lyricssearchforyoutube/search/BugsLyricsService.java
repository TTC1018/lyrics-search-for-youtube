package com.example.lyricssearchforyoutube.search;

import com.example.lyricssearchforyoutube.model.Music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugsLyricsService {

    public String getBugsLyrics(String link){
        final StringBuilder ly_builder = new StringBuilder();
        String lyrics = "";

        try{
            Document doc = Jsoup.connect(link).get();
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            doc.select("br").append("\\n");
            doc.select("p").prepend("\\n\\n");

            Elements lyricsElement = doc.select("div.lyricsContainer xmp");
            lyrics = lyricsElement.html().replaceAll("\\\\n", "\n");

            ly_builder.append(lyrics).append("\n");

        }catch (IOException e){
            ly_builder.append("error");
        }

        return lyrics;
    }


    public List<Music> searchMusic(String  musicQuery ) {
        List<Music> musicList = new ArrayList<>();
        final StringBuilder t_builder = new StringBuilder();

        try{
            // ex) https://music.bugs.co.kr/search/track?q=No.5&target=TRACK_ONLY
            // 전체 검색이 아닌 "곡 / 곡명"으로 검색으로 바꿔야 함
            //String url = "https://music.bugs.co.kr/search/track?q="+ musicQuery + "&target=TRACK_ONLY";
            String url = "https://music.bugs.co.kr/search/integrated?q="+ musicQuery;

            Document doc = Jsoup.connect(url).get();

            Elements titles = doc.select(".lyrics .title a");
            Elements artists = doc.select(".lyrics .artist a");
            Elements links = doc.select("tr .lyrics a");

            int MAXSIZE = 5;
            for(int index=0; index<titles.size(); index++){
                if( index >= MAXSIZE ) break;
                
                String title = titles.get(index).text();
                String artist = artists.get(index).text();
                String lyricsLink = links.get(index).attr("abs:href");
                Music music =  new Music( title, artist,  lyricsLink );
                musicList.add( music );
            }
        }catch (IOException e){
            t_builder.append("error");
        }

        return musicList;
    }
}
