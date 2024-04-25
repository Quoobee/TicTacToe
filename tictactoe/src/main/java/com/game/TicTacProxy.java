package com.game;

import java.io.IOException;
import java.net.Socket;

/**
 * TicTacProxy sits between the user and the server, and it translates what
 *  the user types in the console into messages that get sent to the server.
 *  It also takes the responses from the server and gives that back to the 
 *  user in the console.
 */
public class TicTacProxy extends Duplexer implements TicTacGame {

    public TicTacProxy(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Since the board is 7 lines tall, you need to call recieve 7 times.
     * 
     * @return the TicTacToe board all in one String
     */
    private String getBoard() {
        String board = "";
        for(int i = 0; i < 6; i++) {
            board += recieve() + "\n";
        }
        board += recieve();
        return board;
    }

    @Override
    public boolean move(String move) {
        send(move);

        String response = recieve();
        if(response.equals("OK")) {
            String board = getBoard();
            System.out.println(board);
            return true;
        } else if(response.equals("BAD")) {
            System.out.println("Bad move, try again");
            return false;
        }
        return false;
    }

    @Override
    public int waitForTurn() {
        String response = recieve();
        String board;
        switch (response) {
            case "GAMEOVER":
                board = getBoard();
                // Print final board configuration
                System.out.println(board);
                // Print reason for game over
                System.out.println(recieve()); 
                return 1;
            case "YOURTURN":
                board = getBoard();
                System.out.println(board);
                return 0;
            default:
                return 2;
        }
    }

    @Override
    public void quit() {
        send("QUIT");
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 24953);
        System.out.println("Connected to server");

        // Scanner scanner = new Scanner(socket.getInputStream());
        // System.out.println(scanner.nextLine());
        // scanner.close();

        TicTacProxy proxy = new TicTacProxy(socket);
        TicTacPlayer player = new TicTacPlayer(proxy);
        player.playTheGame();
    }
    
}
