package com.hanzereversiai.projectp3.networking;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Network {

    private Socket socketConnection;
    private BufferedReader bufferedReader;
    private SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 7789);

    public Network() {
        this.socketConnection = new Socket();
        try {
            socketConnection.connect(socketAddress, 30);
            bufferedReader = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
            //dataOutputStream = new DataOutputStream(socketConnection.getOutputStream());
            //dataOutputStream.writeChars("login Test");

            System.out.println(bufferedReader.readLine());
            System.out.println(bufferedReader.readLine());




        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
