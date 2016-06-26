package com.example.wnati.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wnati on 6/24/2016.
 */
@Table(name = "Items")
public class Item extends Model {

    @Column(name = "text")
    public String text;

    public Item() {
        super();
    }

    public Item(String text) {
        super();
        this.text = text;
    }

    public Item(JSONObject object){
        try {
            this.text = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // Item.fromJson(jsonArray);
    public static ArrayList<Item> fromJson(JSONArray jsonObjects) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                items.add(new Item(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }


}
