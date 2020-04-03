package com.hanzereversiai.projectp3.networking;

import java.io.BufferedReader;
import java.io.IOException;

public class InputHandler implements Runnable {

    private BufferedReader reader;

    public InputHandler(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("RECEIVED: " + reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
