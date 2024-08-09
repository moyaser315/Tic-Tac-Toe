package org.ai.tictactoe.judge;

import org.ai.tictactoe.game.GameBoard;

public class TicTacToeJudger implements Judger{
    @Override
    public boolean isGameOver(GameBoard board) {
        return false;
    }

    @Override
    public boolean isFirstWon(GameBoard board) {
        return false;
    }

    @Override
    public boolean isLastWon(GameBoard board) {
        return false;
    }

    @Override
    public boolean isDraw(GameBoard board) {
        return false;
    }
}
