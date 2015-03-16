package com.parse.starter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ProgressCallback;

import java.util.List;

/**
 * Created by williamfeng on 19/02/15.
 */
public class SearchableActivity extends ListActivity {

    private CustomAdapter songAdapter;
    private View resultHeader;
    private TextView resultView;
    private ListView listView;
    private ProgressDialog progressDialog;
    private long mLastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        resultHeader = View.inflate(this, R.layout.search_result_header, null);
        resultView = (TextView) resultHeader.findViewById(R.id.searchResult);
        resultView.setVisibility(TextView.GONE);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (getString(R.string.search).equals(item.getTitle())) {
            return onSearchRequested();
        } else {
            return false;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(final String searchStr) {
        if (searchStr.length() <= 0) {
            return;
        }

        songAdapter = new CustomAdapter(this, searchStr);

        // Perhaps set a callback to be fired upon successful loading of a new set of ParseObjects.
        songAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                // Trigger any "loading" UI
                showProgressDialog();
            }

            @Override
            public void onLoaded(List<ParseObject> parseObjects, Exception e) {
                // Execute any post-loading logic, hide "loading" UI
                dismissProgressDialog();
                resultView.setText(getString(R.string.search_result1) + " " + listView.getCount()
                        + " " + getString(R.string.search_result2));
                resultView.setVisibility(TextView.VISIBLE);
            }
        });

        listView = (ListView) findViewById(android.R.id.list);
        listView.addHeaderView(resultHeader, null, false);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (detectMultipleClicks()) {
                    return;
                }
                showProgressDialog();
                ParseObject selectedObject = songAdapter.getItem(position - 1);
                ParseFile scoreFile = (ParseFile) selectedObject.get("file");
                scoreFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        dismissProgressDialog();
                        if (e == null) {
                            Intent intent = new Intent(SearchableActivity.this, ScoreDisplayActivity.class);
                            intent.putExtra("score", new String(data));
                            startActivity(intent);
                        } else {

                        }
                    }
                });
            }

        });
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(SearchableActivity.this, "", getString(R.string.loading), true);
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public boolean detectMultipleClicks() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
