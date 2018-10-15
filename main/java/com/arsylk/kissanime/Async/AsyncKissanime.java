package com.arsylk.kissanime.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import com.arsylk.kissanime.Cloudflare;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;

public class AsyncKissanime extends AsyncTask<String, Integer, String> {
    private Context context = null;
    private android.app.AlertDialog dialog = null;

    public AsyncKissanime(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        ProgressDialog.Builder dialog = new ProgressDialog.Builder(context);
        dialog.setTitle("Downloading...");
        dialog.setMessage("Starting cloudflare scraper");
        dialog.setCancelable(false);
        this.dialog = dialog.create();
        this.dialog.show();
        System.out.println("----------STARTED CFS----------");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setMessage("Scraping url "+values[0]+"/"+values[1]);
    }

    @Override
    protected String doInBackground(String... urls) {
        try{
            //init cloudflare scraper
            Cloudflare cf = new Cloudflare();
            Document doc = cf.bypass(urls[0], Connection.Method.GET);

            //Prepare return json
            JSONObject json = new JSONObject("{}");
            JSONArray jsonEpisodes = new JSONArray();

            Elements episodes = doc.getElementsByClass("episode");
            for(int i = episodes.size()-1; i >= 0; i--) {
                publishProgress(episodes.size()-i, episodes.size());
                String eID = episodes.get(i).attr("data-value");
                String linkBody = cf.bypass("http://kissanime.ru/Mobile/GetEpisode?eID="+eID, Connection.Method.POST).text();
                linkBody = linkBody.substring(0, linkBody.indexOf("|||"));


                JSONObject jsonEpisode = new JSONObject("{}");
                jsonEpisode.put("id", eID);
                jsonEpisode.put("url", linkBody);
                jsonEpisode.put("name", episodes.get(i).text());
                jsonEpisode.put("order", episodes.size()-i-1);
                jsonEpisodes.put(jsonEpisode);
            }

            json.put("title", doc.selectFirst("#divContentList .post-content > h2 > a").text());
            json.put("episodes", jsonEpisodes);

            File outputDir = new File(Environment.getExternalStorageDirectory(), "Kissanime");
            outputDir.mkdir();
            File output = new File(outputDir.getPath(), (json.getString("title")+".json"));
            FileOutputStream stream = new FileOutputStream(output);
            stream.write(json.toString().getBytes());
            stream.close();

            return output.toString();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String json) {
        dialog.dismiss();
        Toast.makeText(context, "Anime saved to:\n"+json, Toast.LENGTH_SHORT).show();
        System.out.println("----------FINISHED CFS----------");
    }
}