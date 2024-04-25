package com.game;

/**
 * TicTacToe keeps track of the actual game board. Is has methods to make a move
 *  and check if the game is over because someone has won.
 */
public class TicTacToe {
    private final String PLAYER1 = "\u001B[31m" + "X" + "\u001B[0m"; // The funny strings are for color
    private final String PLAYER2 = "\u001B[34m" + "O" + "\u001B[0m";
    private final String BLANK = " ";
    private final int ROWS = 3;
    private final int COLS = 3;
    private String[][] board;
    private int nextMove;
    private int moves;

    public TicTacToe() {
        board = new String[3][3];
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                board[i][j] = BLANK;
            }
        }
        nextMove = 1;
        moves = 0;
    }

    public int getNextMove() {
        return nextMove;
    }

    /**
     * Tries to place the next piece at row, col. If it is successfull, changes
     *  the next piece to be the other piece, i.e. if it just placed an X, the
     *  next piece it places should be an O.
     * 
     * @param row the desired row to place the piece
     * 
     * @param col the desired column to place the piece
     * 
     * @return true if it placed the piece, false if the desired row, col was 
     *  already occupied.
     */
    public boolean move(int row, int col) {
        if(board[row][col] == BLANK) {
            if(nextMove == 1) {
                board[row][col] = PLAYER1;
                nextMove = 2;
            } else {
                board[row][col] = PLAYER2;
                nextMove = 1;
            }
            moves++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String output = "  -   -   -  \n";
        for(int i = 0; i < board.length; i++) {
            output += "| " + board[i][0] + " | ";
            for(int j = 1; j < board[i].length; j++) {
                output += board[i][j] + " | ";
            }
            if(i == board.length - 1) {
                output += "\n  -   -   -  ";
            } else {
                output += "\n  -   -   -  \n";
            }
        }
        return output;
    }

    /**
     * Helper function for isWinner() below, checks one column, or one row, or one diagonal
     * 
     * @param row The starting row to check
     * 
     * @param col The starting column to check
     * 
     * @param rowIncrement The direction that the row will increment, up or down.
     * 
     * @param colIncrement The directoin that the column will increment, left or right.
     * 
     * @return 0: if nobody won in this specific row/col/diag, 
     *  1: if player 1 won, 
     *  2: if player 2 won
     */
    public int isWinnerInDirection(int row, int col, int rowIncrement, int colIncrement) {
        int count = 0;
        String piece = board[row][col];
        if(piece.equals(BLANK)) {
            return 0;
        }
        while(row >= 0 && row < board.length && col >= 0 && col < board[row].length) {
            if(board[row][col] == piece) {
                count++;
            }
            if(count == 3) {
                if(piece.equals(PLAYER1))
                    return 1;
                return 2;
            }
            row += rowIncrement;
            col += colIncrement;
        }
        return 0;
    }

    /**
     * Calls isWinnerInDirection() for each row, each column, and the two diagonals
     *  to check if either player has put 3 of their pieces in a row.
     * 
     * @return 0: if nobody won,
     *  1: if player 1 won,
     *  2: if player 2 won,
     *  3: if there are no more possible moves
     */
    public int isWinner() {
        for(int i = 0; i < board.length; i++) {
            int win = isWinnerInDirection(i, 0, 0, 1);
            if(win != 0)
                return win;
        }

        for(int i = 0; i < board.length; i++) {
            int win = isWinnerInDirection(0, i, 1, 0);
            if(win != 0)
                return win;
        }

        int win = isWinnerInDirection(0, 0, 1, 1);
        if(win != 0) {
            return win;
        }

        win = isWinnerInDirection(2, 0, -1, 1);
        if(win != 0) {
            return win;
        }

        if(moves == 9) {
            return 3;
        }
        return 0;
    }
}
