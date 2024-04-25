package com.game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Duplexer {
    private final Socket socket;
    private final Scanner scanner;
    private final PrintWriter writer;

    public Duplexer(Socket socket) throws IOException {
        this.socket = socket;
        this.scanner = new Scanner(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    public void send(String message) {
        // print the message
        // System.out.println("Sending: " + message);
        // send the message
        writer.println(message);
        writer.flush();
    }

    public String recieve() {
        // recieve a string
        String message = scanner.nextLine();
        // print it
        // System.out.println("<< " + message);
        // return it
        return message;
    }

    public void close() {
        // close everything in a try/catch
        try {
            socket.close();
            writer.close();
            scanner.close();
        } catch(IOException e) {}
    }
}
