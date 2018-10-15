package com.arsylk.kissanime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Anime implements Serializable {
    private String title, link = null;
    private ArrayList<Episode> animeEpisodes = null;


    private void createFromJson(JSONObject jsonAnime) {
        try{
            this.title = jsonAnime.getString("title");
            this.animeEpisodes = new ArrayList<>();

            JSONArray arrayEpisodes = jsonAnime.getJSONArray("episodes");
            for(int i = 0; i < arrayEpisodes.length(); i++) {
                JSONObject jsonEpisode = arrayEpisodes.getJSONObject(i);
                Episode episode = new Episode(
                        jsonEpisode.getInt("id"),
                        jsonEpisode.getInt("order"),
                        jsonEpisode.getString("url"),
                        jsonEpisode.getString("name"));

                animeEpisodes.add(episode);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Anime(JSONObject jsonAnime) {
        createFromJson(jsonAnime);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Episode getAnimeEpisode(int order) {
        return animeEpisodes.get(order);
    }

    public int getAnimeEpisodeCount() {
        return animeEpisodes.size();
    }

    public class Episode implements Serializable {
        int id, order;
        String url, name;
        boolean watched = false;

        public Episode(int id, int order, String url, String name) {
            this.id = id;
            this.order = order;
            this.url = url;
            this.name = name;
        }

        public void setWatched(boolean watched) {
            this.watched = watched;
        }

        public int getId() {
            return id;
        }

        public int getOrder() {
            return order;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public boolean isWatched() {
            return watched;
        }
    }
}
