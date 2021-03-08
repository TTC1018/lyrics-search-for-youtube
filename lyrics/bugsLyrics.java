package com.example.lyrics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class bugsLyrics {

    public ArrayList<String> bugsTitles(String title ){
        ArrayList<String> titles = new ArrayList<String>();

        new Thread((Runnable) ()->{
            final StringBuilder t_builder = new StringBuilder();

            try{
                String url = "https://music.bugs.co.kr/search/integrated?q="+ title;
                Document doc = Jsoup.connect(url).get();

                for(Element t:doc.select(".lyrics .title a")) {
                    String s = t.text();
                    titles.add(s);
                }
            }catch (IOException e){
                t_builder.append("error");
            }

        }).start();
        return(titles);

    }
    public ArrayList<String> bugsArtists(String title){

        ArrayList<String> artists = new ArrayList<String>();
        new Thread((Runnable) ()->{
            final StringBuilder a_builder = new StringBuilder();

            try{
                String url = "https://music.bugs.co.kr/search/integrated?q="+ title;
                Document doc = Jsoup.connect(url).get();

                for(Element artist:doc.select(".lyrics .artist a")) {
                    String s = artist.text();
                    artists.add(s);
                }
                a_builder.append(artists).append("\n");


            }catch (IOException e){
                a_builder.append("error");
            }

        }).start();
        return(artists);

    }
    public ArrayList<String> bugsLyricsLink(String title){

        ArrayList<String> lyrics_link = new ArrayList<String>();
        new Thread((Runnable) ()->{
            final StringBuilder l_builder = new StringBuilder();

            try{
                String url = "https://music.bugs.co.kr/search/integrated?q="+ title;
                Document doc = Jsoup.connect(url).get();


                for(Element lyrics:doc.select("tr .lyrics a")) {
                    lyrics_link.add(lyrics.attr("abs:href"));
                }
                l_builder.append(lyrics_link).append("\n");

            }catch (IOException e){
                l_builder.append("error");

            }

        }).start();
        return(lyrics_link);

    }
    public ArrayList<String> bugsLyrics(String link){
        ArrayList<String> whole_lyrics = new ArrayList<String>();
        new Thread((Runnable) ()->{
            final StringBuilder ly_builder = new StringBuilder();

            try{
                Document doc = Jsoup.connect(link).get();

                Elements lyrics = doc.select("div.lyricsContainer xmp");
                String s = lyrics.text();

                whole_lyrics.add(s);
                ly_builder.append(whole_lyrics).append("\n");

            }catch (IOException e){
                ly_builder.append("error");
            }

        }).start();
        return(whole_lyrics);
    }

}
