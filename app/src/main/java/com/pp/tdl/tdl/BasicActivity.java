package com.pp.tdl.tdl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import android.app.ListActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

import static java.lang.Thread.sleep;

public class BasicActivity extends ListActivity {

    public static int result = 0;
    ListView listView;
    ArrayList<String> listItems=new ArrayList<>();
    TcpClient serverConnectionClient;
    ArrayAdapter<String> adapter;
    String selectedItem;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_of_items);
        listView = (ListView) findViewById(android.R.id.list);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                try{

                Log.w("OnClickListener","Position: " + position);
                    AlertDialog alertDialog = new AlertDialog.Builder(BasicActivity.this).create();
                    alertDialog.setTitle("Done");
                    alertDialog.setMessage("Is it done?");

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                            listItems.remove(position);
                            try {
                                saveDataToFile(listItems);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Item still in progress", Toast.LENGTH_SHORT).show();
                        }
                    });


                    alertDialog.show();

                }
                catch(Exception e){
                    Log.w("BasicActivity", "Bad removal or saving problem");
                }
            }
        });
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

    ArrayList<String> convertXmlStringToList(String content) throws Exception {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        DocumentBuilderFactory dbf;
        DocumentBuilder db;
        Document dom;

        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        dom = db.parse(is);
        // normalize the document
        dom.getDocumentElement().normalize();

        NodeList items = dom.getElementsByTagName("record");

        ArrayList<String> arr = new ArrayList<>();

        for (int i=0;i<items.getLength();i++){

            Node item = items.item(i);
            String temp = item.getTextContent();
            arr.add(temp);
            Log.w("convertXmlToString",temp);

        }
        return arr;

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

        ArrayList<String> arr = new ArrayList<>();

        for (int i=0;i<items.getLength();i++){

            Node item = items.item(i);

            arr.add(item.getTextContent());

        }
        return arr;
    }

    public void syncWithServer(View view) {
        new PushTask().execute("");
    }

    public void pushToServer(View view) {
        new PushTask().execute("");

    }

    public void pullFromServer(View view) {
        new PullTask().execute("");
        try {
            Thread.sleep(100);
            updateScreen(loadFromFile());
        } catch (Exception e) {
            Log.d("Puller", "Interrupted waiting operation, data may be not synchronised!");
            e.printStackTrace();
        }


    }

    private class PushTask extends AsyncTask<String, String, TcpClient> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            serverConnectionClient = new TcpClient(getFilesDir() +"/saved_list.xml");
            serverConnectionClient.sendXml();

            return serverConnectionClient;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }

    void updateScreen(ArrayList<String> list)
    {
        listItems.clear();
        listItems.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private class PullTask extends AsyncTask<String, String, TcpClient> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            serverConnectionClient = new TcpClient(getFilesDir() +"/saved_list.xml");
            String xmlString = serverConnectionClient.receiveXml();
            Log.w("PullTask", "Received xml\n" + xmlString);
            try {
                ArrayList<String> listOfItems = convertXmlStringToList(xmlString);
                saveDataToFile(listOfItems);
            }catch(Exception e) {
                Log.w("PullTask","Problem with parsing received Xml");
        }
            return serverConnectionClient;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
