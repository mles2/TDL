package com.pp.tdl.tdl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import android.app.ListActivity;
import android.widget.ArrayAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BasicActivity extends ListActivity {

    public static int result = 0;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_of_items);
        try {
            listItems = loadFromFile();
            Log.w("FileReader", "File read!");
        }catch(Exception e) {
            Log.w("FileReader","Problem with reading last configuration from file!");
        }
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                        saveDataToFile(listItems);
                        Log.w("FileSaver","File succesfully created!");
                    }catch(Exception e) {
                        Log.w("FileSaver","Problem with saving file!");
                    }
                }
                break;
            }
        }
    }

    void saveDataToFile(ArrayList<String> listItems) throws IOException{
        String filename = getFilesDir() +"/saved_list.xml";
        Log.w("FileSaver", filename);
        File myFile = new File(filename);
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, "UTF-8");
        serializer.startDocument(null, Boolean.TRUE);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        serializer.startTag(null, "root");

        for(int j = 0 ; j < listItems.size() ; j++)
        {

            serializer.startTag(null, "record");

            serializer.text(listItems.get(j));

            serializer.endTag(null, "record");
        }
        serializer.endDocument();

        serializer.flush();

        fos.close();
    }

    ArrayList<String> loadFromFile() throws Exception {
        String filename =  getFilesDir() + "/saved_list.xml";
        Log.w("FileReader", filename);
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis);

        char[] inputBuffer = new char[fis.available()];
        isr.read(inputBuffer);
        String data = new String(inputBuffer);
        isr.close();
        fis.close();

    /*
     * converting the String data to XML format
     * so that the DOM parser understand it as an XML input.
     */
        InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document dom;

        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        dom = db.parse(is);
        // normalize the document
        dom.getDocumentElement().normalize();

        NodeList items = dom.getElementsByTagName("record");

        ArrayList<String> arr = new ArrayList<String>();

        for (int i=0;i<items.getLength();i++){

            Node item = items.item(i);

            arr.add(item.getTextContent());

        }
        return arr;
    }
}
