package com.example.android.orange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.orange.R;

import java.util.ArrayList;

/**
 * Created by Apoorva on 6/21/2018.
 */

public class ListAdapter1 extends ArrayAdapter<String> {
    ArrayList<String> data;
    Context context;
    customButtonListener customListner;

    public ListAdapter1(Context context, ArrayList<String> dataItem)
    {
        super(context, R.layout.userlist,dataItem);
        this.data = dataItem;
        this.context = context;
    }

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.userlist, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.listitem_text);
            viewHolder.button = (Button) convertView
                    .findViewById(R.id.listitem_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position,temp);
                }

            }
        });

        return convertView;
    }
    public class ViewHolder {
        TextView text;
        Button button;
    }
}
