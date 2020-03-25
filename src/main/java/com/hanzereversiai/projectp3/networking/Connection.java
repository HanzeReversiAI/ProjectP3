package com.hanzereversiai.projectp3.networking;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Connection(String hostname, int port, int timeout) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), timeout);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        while(true) {
            try {
                writer.write("\r\nhelp");
                System.out.println(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
