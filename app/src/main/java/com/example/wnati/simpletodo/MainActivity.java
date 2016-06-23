package com.example.wnati.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems; //list view handle

    private final int EDIT_REQUEST = 20;
    private final int EDIT_FAIL = 30;

    @Override
    /*
     * set XML layout for activity
     * attach adapter to ListView
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    // setup click listener
    private void setupListViewListener() {
        /*
         * add a LongClickListener for each Item in the ListView
         * Item will be removed and the adapter will be refreshed
         */
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
             * add a ClickListener for each Item in the ListView
             * this will allow for the Item to be edited
             */
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {

                /*
                 * start the EditItemActivity
                 * pass along the Item's pos and text
                 */
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("description", item.toString());
                i.putExtra("pos", pos);

                // start EditItemActivity
                startActivityForResult(i, EDIT_REQUEST);
            }

        });
    }

    /*
     * add text input item when Add Button is clicked
     */
    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // read items from todo.txt, otherwise return an empty list
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    // write the items to todo.txt
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST) {
            String task = data.getExtras().getString("task");
            int pos = data.getExtras().getInt("pos", -1);

            // update the Item's text and save changes
            items.set(pos, task);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
