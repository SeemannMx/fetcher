package de.homemade.fetcher;

import android.util.Log;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsExtractor {

    String TAG = "FETCHER ";
    String CLASS = "NEWS ";

    // extract news from String
    public String extractNews(String newsString){

        String refacoredNews = "";
        newsString.replaceAll(".", "/");

        Matcher matcher = Pattern.compile("(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d").matcher(newsString);
        Log.i(TAG, CLASS + " dots exchanged with dash " + newsString);

        int count = 0;
        String[] allMatches = new String[newsString.length()];
        while(matcher.find()){
            allMatches[count] = matcher.group();
            count++;
        }

        Gson g = new Gson();
        refacoredNews = g.toJson(allMatches);

        // Log.i(TAG,CLASS + "count " + count);
        // Log.i(TAG,CLASS + "array " + refacoredNews);


        String[] split = newsString.split(":");
        String firstSubString = split[0];
        String secondSubString = split[1];

        return refacoredNews;
    }

}
