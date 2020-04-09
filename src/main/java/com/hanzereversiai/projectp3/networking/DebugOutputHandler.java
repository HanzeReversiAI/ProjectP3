package com.hanzereversiai.projectp3.networking;

import java.io.PrintWriter;
import java.util.Scanner;

public class DebugOutputHandler implements Runnable {

    private PrintWriter writer;
    private Scanner scanner;

    public DebugOutputHandler(PrintWriter writer) {
        this.writer = writer;
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while(true) {
            String input = scanner.nextLine();
            writer.println(input);
        }
    }
}
