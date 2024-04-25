package com.game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TicTacToe tictactoe = new TicTacToe();
        try (Scanner scanner = new Scanner(System.in)) {
            while(true) {
                System.out.println("Input \"row col\"");
                String[] move = scanner.nextLine().trim().split(" ");
                int row = Integer.parseInt(move[0]);
                int col = Integer.parseInt(move[1]);
                System.out.println("Move: " + tictactoe.move(row, col));
                System.out.println(tictactoe);
                System.out.println("Win: " + tictactoe.isWinner());
            }
        }
    }
}