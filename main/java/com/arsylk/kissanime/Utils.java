package com.arsylk.kissanime;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static JSONObject fileToJson(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while((line = br.readLine()) != null)
                content.append(line);

            return new JSONObject(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setLastAnime(Context context, String title, String path) {
        SharedPreferences.Editor editor = context.getSharedPreferences("library", Context.MODE_PRIVATE).edit();
        editor.putString("last_anime", title);
        editor.putString("last_anime_path", path);
        editor.apply();
    }

    public static String getLastAnime(Context context) {
        return context.getSharedPreferences("library", Context.MODE_PRIVATE).getString("last_anime", "");
    }

    public static String getLastAnimePath(Context context) {
        return context.getSharedPreferences("library", Context.MODE_PRIVATE).getString("last_anime_path", "");
    }

    public static void setWatched(Context context, int id, boolean watched) {
        SharedPreferences pref = context.getSharedPreferences("library", Context.MODE_PRIVATE);
        Set<String> stringSet = new HashSet<>(pref.getStringSet("last_anime_episodes", new HashSet<String>()));
        if(watched) {
            stringSet.add(String.valueOf(id));
        }else {
            stringSet.remove(String.valueOf(id));
        }

        pref.edit().putStringSet("last_anime_episodes", stringSet).apply();
    }

    public static boolean isWatched(Context context, int id) {
        SharedPreferences pref = context.getSharedPreferences("library", Context.MODE_PRIVATE);
        Set<String> stringSet = pref.getStringSet("last_anime_episodes", new HashSet<String>());

        return stringSet.contains(String.valueOf(id));
    }
}
