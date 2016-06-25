package com.example.wnati.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wnati on 6/25/2016.
 */
public class ToDoItemAdapter extends ArrayAdapter<Item> {
    public ToDoItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    // View lookup cache => improved performance
    private static class ViewHolder {
        TextView text;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        // Get the todo item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.text.setText(item.text);
        // Return the completed view to render on screen

        return convertView;
    }
}
