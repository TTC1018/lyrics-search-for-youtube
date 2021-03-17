package com.example.lyricssearchforyoutube.parsing;

import com.example.lyricssearchforyoutube.data.Music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BugsLyricsService {

    public String getBugsLyrics(String link){
        String lyrics = "";

        final StringBuilder ly_builder = new StringBuilder();

        try{
            Document doc = Jsoup.connect(link).get();

            Elements lyricsElement = doc.select("div.lyricsContainer xmp");
            lyrics = lyricsElement.text();

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
            String url = "https://music.bugs.co.kr/search/integrated?q="+ musicQuery;
            Document doc = Jsoup.connect(url).get();

            Elements titles = doc.select(".lyrics .title a");
            Elements artists = doc.select(".lyrics .artist a");
            Elements links = doc.select("tr .lyrics a");

            for(int index=0; index<titles.size(); index++){
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
