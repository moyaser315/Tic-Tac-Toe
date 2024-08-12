package org.ai.tictactoe.logic;

public interface Judger {
    boolean isGameOver(GameBoard board);
    boolean isFirstWon(GameBoard board);
    boolean isLastWon(GameBoard board);
    boolean isDraw(GameBoard board);
}
