package com.arsylk.kissanime;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnimeAdapter extends BaseAdapter {
    private Context context;
    private Anime anime;

    public AnimeAdapter(Context context, Anime anime) {
        this.context = context;
        this.anime = anime;
    }

    @Override
    public int getCount() {
        return anime.getAnimeEpisodeCount();
    }

    @Override
    public Anime.Episode getItem(int position) {
        return anime.getAnimeEpisode(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = (convertView != null) ?
                convertView : inflater.inflate(R.layout.item_anime_episode, parent, false);

        item.setBackgroundColor((getItem(position).isWatched() || Utils.isWatched(context, getItem(position).getId())) ? Color.LTGRAY : Color.WHITE);
        ((TextView) item.findViewById(R.id.item_anime_episode_name)).setText(getItem(position).getName());
        ((TextView) item.findViewById(R.id.item_anime_episode_url)).setText(getItem(position).getUrl());


        return item;
    }
}
