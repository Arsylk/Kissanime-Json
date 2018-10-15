package com.arsylk.kissanime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Context context = MainActivity.this;

    private TextView lastAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(Utils.getLastAnime(context));
        System.out.println(Utils.getLastAnimePath(context));

        initViews();
    }

    private void initViews() {
        //on file pick click
        findViewById(R.id.main_load_from_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileChooser((Activity)context).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        openAnimeActivity(file.getPath());
                    }
                }).showDialog();
            }
        });

        //on url pick click
        findViewById(R.id.main_load_from_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CloudflareActivity.class));
            }
        });


        //last anime item
        lastAnime = (TextView) findViewById(R.id.main_last_anime);
        lastAnime.setText(Utils.getLastAnime(context));
        lastAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAnimeActivity(Utils.getLastAnimePath(context));
            }
        });
        lastAnime.setClickable(lastAnime.getText().length() > 0);
    }

    @Override
    protected void onResume() {
        lastAnime.setText(Utils.getLastAnime(context));
        lastAnime.setClickable(lastAnime.getText().length() > 0);
        super.onResume();
    }

    private void openAnimeActivity(String path) {
        JSONObject json = Utils.fileToJson(new File(path));
        if(json != null) {
            Anime anime = new Anime(json);
            Utils.setLastAnime(context, anime.getTitle(), path);
            startActivity(new Intent(context, AnimeActivity.class).putExtra("anime", anime));
        }else {
            Toast.makeText(context, "Couldn't parse this json file", Toast.LENGTH_SHORT).show();
        }
    }
}
