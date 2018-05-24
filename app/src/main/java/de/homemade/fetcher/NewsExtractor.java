package de.homemade.fetcher;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsExtractor {

    String TAG = "FETCHER ";
    String CLASS = "NEWS ";

    String ue = "&#252;";
    String Ue = "&#220;";
    String oe = "&#246;";
    String Oe = " &#214;";
    String ae = "&#228;";
    String Ae = "&#196;";
    String ss = "&#223;";
    String quote = "&quot;";


    // extract news from String
    public String extractNews(String newsString){

        String refacoredNews = "";
        newsString.replaceAll(".", "/");

        // regEx to macht date in String
        Matcher matcher = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d").matcher(newsString);
        Log.i(TAG, CLASS + " dots exchanged with dash " + newsString);

        int count = 0;
        String[] allMatches = new String[newsString.length()];
        while(matcher.find()){
            allMatches[count] = matcher.group();
            count++;
        }

        Gson g = new Gson();
        refacoredNews = g.toJson(allMatches);

        return refacoredNews;
    }

    // converrt unicode literals to string in content
    public ArrayList<Article> convertUnicodeToString(ArrayList<Article> list){

        String date = "";
        String content = "";

        for(int i = 0; i < list.size(); i++){
            date = String.valueOf(list.get(i) .getPubDate());
            content = list.get(i) .getDescription();

            // convert unicode
            content = content.replace(ue, "ue").replace(Ue,"Ue").
                                replace(oe,"oe").replace(Oe, "oe").
                                replace(Ae, "Ae").replace(ae,"ae").
                                replace(ss,"ss").replace(quote, "''");

            // set converted content in list
            list.set(i, list.get(i)).setDescription(content);

            // Log.i(TAG,CLASS + " \ndate   : " + date);
            // Log.i(TAG,CLASS + "content: " + content + "\n\n");


        }

        return list;
    }

    // create news String
    @SuppressLint("ResourceAsColor")
    public String createNewsString(ArrayList<Article> list){
        String news = "";

        String date = "";
        String content = "";
        String newsPair = "";

        list = convertUnicodeToString(list);

        // Gson g = new Gson();
        // Log.i(TAG,CLASS + "GSON LIST :" +  g.toJson(list));

        for(int i = 0; i < list.size(); i++){

            date = String.valueOf(list.get(i).getPubDate());
            content = list.get(i).getDescription();

            newsPair = date.concat( "\n- - - - - - - - - - - - " +
                    "- - - - - - - - - - - - - - - - - - - - - - -" +
                    "\n").concat(content).concat("\n\n");

            news = news.concat(newsPair);
        }

        Log.i(TAG, CLASS + " received / correted / formated news: \n" + news);

        return news;
    }

}
