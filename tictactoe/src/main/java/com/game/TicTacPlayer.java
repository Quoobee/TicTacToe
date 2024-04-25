package com.game;

import java.util.Scanner;

/**
 * TicTacPlayer is the class that handles user input and calls the appropriate
 *  methods.
 */
public class TicTacPlayer {
    private TicTacGame game;

    public TicTacPlayer(TicTacGame game) {
        this.game = game;
    }

    /**
     * Starts a game play loop. The player is prompted to enter a command,
     * and the command is interpreted. The player may quit at any time, and
     * the loop is terminated.
     */
    public void playTheGame() {
        Scanner scanner = new Scanner(System.in);
        // a sentinel boolean used to control the play loop; play continues
        // as long as the boolean is not set to false (i.e. when the user
        // opts to quit).
        boolean sentinel = true;
        while (sentinel) {
            int status = game.waitForTurn();
            if(status == 1) { // status 1 the player QUIT
                break;
            }
            boolean loop = true;
            while(loop) { // This is janky
                
                System.out.print("Enter a command (HELP to list commands): ");
                String commandLine = scanner.nextLine();
                String[] tokens = commandLine.split(" ");

                if (tokens.length == 0) {
                    System.err.println("You must enter a command.");
                    continue;
                }

                switch (tokens[0]) {
                    case "HELP":
                        help();
                        loop = true;
                        break;
                    case "MOVE":
                        move(commandLine);
                        loop = false;
                        break;
                    case "QUIT":
                        sentinel = !quit(scanner);
                        loop = false;
                        break;
                    default:
                        System.err.println("Unrecognized command.");
                        loop = true;
                        break;
                }

            }
            
        }

        System.out.println("Game Over!");
    }

    private static void help() {
        System.out.println("Available commands: ");
        System.out.println("  HELP - displays this message");
        System.out.println("  MOVE [row] [col] - makes a move");
        System.out.println("  QUIT - quits the game");
        System.out.println();
    }

    /**
     * 
     * @param line The line that the user typed in
     * @return
     */
    private boolean move(String line) {
        String[] tokens = line.trim().split(" ");
        if(tokens.length != 3) {
            System.err.println("Usage: MOVE [row] [col]");
        }
    
        return game.move(line);
    }

    /**
     * Prompts the user to confirm that they would like to quit the game.
     *
     * @param scanner The scanner used to read the user's input.
     *
     * @return True if the user opted to quite the game; false otherwise.
     */
    private boolean quit(Scanner scanner) {
        System.out.print("Are you sure (Y/N)? ");
        String response = scanner.nextLine();
        boolean shouldQuit = response.equalsIgnoreCase("y");
        if (shouldQuit) {
            game.quit();
        }
        return shouldQuit;
    }
}
