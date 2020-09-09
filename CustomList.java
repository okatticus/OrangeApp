package com.example.android.orange;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Apoorva on 6/21/2018.
 */

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> web;

    public CustomList(Activity context, ArrayList<String> ss
    ) {
        super(context, R.layout.userlist);
        this.context = context;
        this.web = ss;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.userlist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listitem_text);
        return rowView;
    }
}


