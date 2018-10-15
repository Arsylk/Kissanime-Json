package com.arsylk.kissanime.Async;

import android.os.AsyncTask;
import com.arsylk.kissanime.Cloudflare;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AsyncKissanimeQuery extends AsyncTask<String, Void, Map<String, String>> {

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Map<String, String> doInBackground(String... keywords) {
        Map<String, String> query = new HashMap<>();
        String url = String.format(Locale.US, "http://kissanime.ru/Search/SearchSuggestx?type=Anime&keyword=%s", keywords[0]);
        try {
            Cloudflare cf = new Cloudflare();
            Document doc = cf.bypass(url, Connection.Method.POST);
            for(Element qElement : doc.select("a[href]")) {
                query.put(qElement.text(), qElement.attr("href").replace("kissanime.ru/Anime", "kissanime.ru/M/Anime"));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    protected void onPostExecute(Map<String, String> query) {
        System.out.println(query);
    }
}