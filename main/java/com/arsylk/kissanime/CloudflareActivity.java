package com.arsylk.kissanime;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.arsylk.kissanime.Async.AsyncKissanime;
import com.arsylk.kissanime.Async.AsyncKissanimeQuery;

import java.util.Map;

public class CloudflareActivity extends AppCompatActivity {
    private Context context = CloudflareActivity.this;
    private AutoCompleteTextView input_search = null;
    private EditText input_url = null;

    private AsyncKissanimeQuery asyncQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudflare);

        input_search = (AutoCompleteTextView) findViewById(R.id.cf_query);
        input_search.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                input_search.showDropDown();
                return true;
            }
        });
        findViewById(R.id.cf_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(asyncQuery == null || asyncQuery.getStatus() == AsyncTask.Status.FINISHED) {
                    asyncQuery = new AsyncKissanimeQuery() {
                        @Override
                        protected void onPostExecute(final Map<String, String> query) {
                            super.onPostExecute(query);
                            findViewById(R.id.cf_search).setEnabled(true);
                            findViewById(R.id.cf_query).setEnabled(true);
                            final String keys[] = new String[query.keySet().size()];
                            for(int i = 0;  i < query.keySet().size(); i++)
                                keys[i] = query.keySet().toArray()[i].toString();
                            input_search.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, keys));
                            input_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    input_url.setText(query.get(keys[position]));
                                }
                            });
                            input_search.showDropDown();
                        }
                    };
                }else {
                    asyncQuery.cancel(true);
                }
                findViewById(R.id.cf_search).setEnabled(false);
                findViewById(R.id.cf_query).setEnabled(false);
                asyncQuery.execute(input_search.getText().toString());
            }
        });

        input_url = (EditText) findViewById(R.id.cf_url);
        findViewById(R.id.cf_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncKissanime(context).execute(input_url.getText().toString());
            }
        });
    }
}
