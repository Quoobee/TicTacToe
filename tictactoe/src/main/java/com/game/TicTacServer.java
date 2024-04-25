package com.game;

import java.io.IOException;
// import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TicTacServer is the server that the players will connect to in order
 *  to play the game. It first waits for two players to connect, them
 *  starts a new game of TicTacToe with those two players.
 */
public class TicTacServer implements Runnable {
    private TicTacToe game;
    private Duplexer player1;
    private Duplexer player2;

    public TicTacServer (Socket socket1, Socket socket2) throws IOException {
        this.game = new TicTacToe();
        this.player1 = new Duplexer(socket1);
        this. player2 = new Duplexer(socket2);
    }

    /**
     * Attempts to make a move on the TicTacToe board
     * 
     * @param response example: "MOVE 0 1", exactly what the server recieves 
     *  from the player
     * 
     * @return true if the move was successful or false if the move was on an 
     *  already occupied space
     */
    public boolean move(String response) {
        String[] tokens = response.trim().split(" ");
        int row = Integer.parseInt(tokens[1]);
        int col = Integer.parseInt(tokens[2]);
        return game.move(row, col);
    }

    /**
     * Handles the TicTacToe game, decides which player is supposed to move next
     *  and signals to them that it is their turn. Then recieves a response
     *  telling what move the player would like to do.
     */
    public void run() {
        Duplexer currentPlayer = null;
        while(true) {
            if(game.getNextMove() == 1) {
                currentPlayer = player1;
            } else {
                currentPlayer = player2;
            }
            int winCase = game.isWinner();
            if(winCase != 0) {
                endGame(winCase);
                break;
            }
            currentPlayer.send("YOURTURN");
            currentPlayer.send(game.toString());

            String line = currentPlayer.recieve();
            String[] tokens = line.split(" ");
            String firstWord = tokens[0];
            if(firstWord.equals("MOVE")) {
                boolean success = move(line);
                if(success) {
                    // Move was successful
                    currentPlayer.send("OK"); 
                    currentPlayer.send(game.toString());
                } else {
                    // Move was on an already occupied space
                    currentPlayer.send("BAD"); 
                }
            } else {
                endGame(4);
                break;
            }
        }
    }

    /**
     * Notifies both players that the game is over, and the case that caused 
     *  the game to end.
     * 
     * @param winCase There are 4 different cases that could cause the game 
     *  to end:
     *  1: Player 1 got 3 in a row
     *  2: Player 2 got 3 in a row
     *  3: There are no more possible moves, a tie
     *  4: A player typed "QUIT"
     */
    public void endGame(int winCase) {
        player1.send("GAMEOVER");
        player2.send("GAMEOVER");
        player1.send(game.toString());
        player2.send(game.toString());
        switch(winCase) {
            case 1:
                player1.send("Player 1 (X) wins!");
                player2.send("Player 1 (X) wins!");
                break;
            case 2:
                player1.send("Player 2 (O) wins!");
                player2.send("Player 2 (O) wins!");
                break;
            case 3:
                player1.send("Tie!");
                player2.send("Tie!");
                break;
            case 4:
                player1.send("Opponent has quit!");
                player2.send("Opponent has quit!");
                break;
        }
        player1.close();
        player2.close();
    }
    
    /**
     * The main method allows two clients to connect, and assigns them to two sockets.
     *  Then creates an instance of the game server with the two sockets.
     * 
     * @param args Unused
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try(ServerSocket server = new ServerSocket(24953)) {
            while(true) {
                System.out.println("Waiting for Player 1...");
                Socket socket1 = server.accept();
                // PrintWriter writer1 = new PrintWriter(socket1.getOutputStream());
                // writer1.println("Server: You have connected! You are Player 1 (X)! Waiting for Player 2 to connect...");
                // writer1.flush();
                System.out.println("Player 1 Connected!");

                System.out.println("Waiting for Player 2...");
                Socket socket2 = server.accept();
                // PrintWriter writer2 = new PrintWriter(socket1.getOutputStream());
                // writer2.println("Server: You have connected! You are Player 2 (O)! Waiting for Player 1 to make a move...");
                // writer2.flush();
                System.out.println("Player 2 Connected!");

                TicTacServer gameServer = new TicTacServer(socket1, socket2);
                Thread thread = new Thread(gameServer);

                thread.start();
            }
        }
    }
}
