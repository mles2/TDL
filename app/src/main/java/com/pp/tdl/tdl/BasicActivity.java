package com.pp.tdl.tdl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.app.ListActivity;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class BasicActivity extends ListActivity {

    public static int result = 0;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    FileSaver fileSaver = new FileSaver();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_of_items);
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        Intent intent = new Intent(this, ItemAddForm.class);
        startActivityForResult(intent, result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    String desc = data.getStringExtra("desc");
                    String email = data.getStringExtra("email");
                    String date = data.getStringExtra("date");
                    listItems.add(desc + " \n" + email + " \n" + date);
                    adapter.notifyDataSetChanged();
                    try{
                        saveToFile();
                        Log.w("FileSaver","File succesfully created!");
                    }catch(Exception e) {
                        Log.w("FileSaver","Problem with saving file!");
                    }
                }
                break;
            }
        }
    }

    public void saveToFile() throws IOException{
        fileSaver.saveDataToFile(listItems);
    }
}
