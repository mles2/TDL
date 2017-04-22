package com.pp.tdl.tdl;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by mles on 2017-04-22.
 * Platformy Programistyczne
 */

class TcpClient {

    private static final String SERVER_IP = "192.168.8.104"; //server IP address
    private static final int SERVER_PORT = 1234;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }


    void run() {


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

                message = "JESTEM";
                outToServer.writeBytes(message + '\n');
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

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    interface OnMessageReceived {
        void messageReceived(String message);
    }

}