package com.arsylk.kissanime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.File;

public class AnimeActivity extends AppCompatActivity {
    private Context context = AnimeActivity.this;
    private ListView animeEpisodesList;
    private AnimeAdapter animeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        if(getIntent().getSerializableExtra("anime") != null) {
            initViews((Anime)getIntent().getSerializableExtra("anime"));
        }else {
            finish();
        }
    }

    private void initViews(Anime anime) {
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(anime.getTitle());
        else if(getActionBar() != null)
            getActionBar().setTitle(anime.getTitle());

        animeAdapter = new AnimeAdapter(context, anime);
        animeEpisodesList = (ListView) findViewById(R.id.list_episodes);
        animeEpisodesList.setAdapter(animeAdapter);
        animeEpisodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anime.Episode episode = animeAdapter.getItem(position);
                Utils.setWatched(context, episode.getId(), true);
                episode.setWatched(true);
                animeAdapter.notifyDataSetChanged();
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(animeAdapter.getItem(position).getUrl())));
            }
        });
        animeEpisodesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Anime.Episode episode = animeAdapter.getItem(position);
                Utils.setWatched(context, episode.getId(), false);
                episode.setWatched(false);
                animeAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
