package com.hanzereversiai.projectp3.networking;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Network {

    public String hostname = "127.0.0.1";
    public int port = 7789;
    public int timeout = 30;

    private Connection connection;

    public Network() {
        try {
            connection = new Connection(hostname, port, timeout);
            Thread thread = new Thread(connection);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
