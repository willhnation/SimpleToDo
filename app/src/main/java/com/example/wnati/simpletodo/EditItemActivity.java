package com.example.wnati.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String task = getIntent().getStringExtra("task");
        pos = getIntent().getIntExtra("pos", -1);

        // if -1 then fail
        EditText editItem = (EditText)findViewById(R.id.editItemText);
        editItem.setText(task);
    }

    public void onEditItem(View v) {
        EditText editItem = (EditText)findViewById(R.id.editItemText);
        String itemText = editItem.getText().toString();

        // prepare the data to send back to the parent
        Intent data = new Intent();
        data.putExtra("task", itemText);
        data.putExtra("pos", pos);

        setResult(RESULT_OK, data); // send result code and response data
        finish(); // close activity and return to parent
    }
}
