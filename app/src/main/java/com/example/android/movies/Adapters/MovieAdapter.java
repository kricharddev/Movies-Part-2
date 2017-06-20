package com.example.android.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movies.MainActivity;
import com.example.android.movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by kyle on 5/15/17.
 */

// Adapter to get poster Image into a GridView using Picasso
public class MovieAdapter extends BaseAdapter {

    private Context mContext;

    public MovieAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return MainActivity.posters.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView poster;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            poster = (ImageView) inflater.inflate(R.layout.item_movie, parent, false);
        } else {
            poster = (ImageView) convertView;
        }

        Picasso.with(mContext).load(MainActivity.posters.get(position)).into(poster);

        return poster;
    }
}

