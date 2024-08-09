package org.ai.tictactoe.judge;

import org.ai.tictactoe.game.GameBoard;

public interface Judger {
    boolean isGameOver(GameBoard board);
    boolean isFirstWon(GameBoard board);
    boolean isLastWon(GameBoard board);
    boolean isDraw(GameBoard board);
}
