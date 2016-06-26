package com.example.wnati.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;
    ToDoItemAdapter itemsAdapter;
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
        itemsAdapter = new ToDoItemAdapter(this, items);
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
                Item toDelete = itemsAdapter.getItem(pos);
                toDelete.delete();
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
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
                Item editItem = itemsAdapter.getItem(pos);
;               i.putExtra("task", editItem.text);
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

        Item item = new Item();
        item.text = itemText;
        item.save();

        itemsAdapter.add(item);
        etNewItem.setText("");
    }

    // read Items from Items table
    private void readItems() {
        List<Item> todoList = new Select().from(Item.class).execute();

        if (todoList.size() > 0) {
            items = new ArrayList<Item>(todoList);
        } else {
            items = new ArrayList<Item>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST) {
            String task = data.getExtras().getString("task");
            int pos = data.getExtras().getInt("pos", -1);

            Item editedItem = itemsAdapter.getItem(pos);
            editedItem.text = task;
            editedItem.save();
            // update the Item's text and save changes
            items.set(pos, editedItem);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
