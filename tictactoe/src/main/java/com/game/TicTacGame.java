package com.game;

public interface TicTacGame {
    public boolean move(String move);
    public int waitForTurn();
    public void quit();
}
