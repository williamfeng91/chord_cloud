package com.parse.starter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by williamfeng on 20/02/15.
 */
public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

    public CustomAdapter(Context context, final String searchStr) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Song");
                query.whereMatches("tags", searchStr);
                query.orderByAscending("name");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.adapter_item, null);
        }
        super.getItemView(object, v, parent);

        TextView songNameView = (TextView) v.findViewById(R.id.songName);
        songNameView.setText(object.getString("name"));

        TextView songSubtitleView = (TextView) v.findViewById(R.id.songSubtitle);
        songSubtitleView.setText(object.getString("subtitle"));

        return v;
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.adapter_next_page, null);
        }
        return v;
    }
}
