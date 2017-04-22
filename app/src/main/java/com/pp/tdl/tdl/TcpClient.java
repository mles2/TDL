package com.pp.tdl.tdl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mles on 2017-04-22.
 * Platformy Programistyczne
 */

class TcpClient {

    private static final String SERVER_IP = "192.168.8.104"; //server IP address
    private static final int SERVER_PORT = 1234;
    private String filename;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    TcpClient(String p_filename) {
        filename = p_filename;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void sendXml() {


        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server

            try (Socket socket = new Socket(serverAddr, SERVER_PORT)) {
                String message;
                String responseFromServer;

                //trying to connect

                //creating out to write to server
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

                //creating in to get data from server
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
                content += "\nq";
                outToServer.writeBytes(content + '\n');
                responseFromServer = inFromServer.readLine();
                socket.close();
                //sends the message to the server
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + responseFromServer + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            }
            //the socket must be closed. It is not possible to reconnect to this socket
            // after it is closed, which means a new socket instance has to be created.


        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }
}