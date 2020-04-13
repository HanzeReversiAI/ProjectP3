package com.hanzereversiai.projectp3.networking;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class allows for sending network commands from a terminal
 *
 * @author Mike
 */
public class DebugOutputHandler implements Runnable {

    private PrintWriter writer;
    private Scanner scanner;

    /**
     * Constructor
     * @param writer Writer to use to send commands over the Connection
     */
    public DebugOutputHandler(PrintWriter writer) {
        this.writer = writer;
        scanner = new Scanner(System.in);
    }

    /**
     * Main logic loop, simply checks if there is console input.
     */
    @Override
    public void run() {
        String input;

        while(!Thread.interrupted() && (input = scanner.nextLine()) != null) {
            writer.println(input);
        }
    }
}
