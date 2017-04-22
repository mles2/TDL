package com.pp.tdl.tdl;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by mles on 2017-04-22.
 * Platformy Programistyczne
 */

public class FileSaver {

    void saveDataToFile(ArrayList<String> listItems) throws IOException{
        String filename = "saved_list.xml";
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
}


