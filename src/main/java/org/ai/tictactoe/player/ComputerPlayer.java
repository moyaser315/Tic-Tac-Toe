package org.ai.tictactoe.player;

import org.ai.tictactoe.game.GameBoard;
import org.ai.tictactoe.game.GameSymbol;

public class ComputerPlayer implements Player {
    private GameDifficulty difficulty;

    public ComputerPlayer(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void makeMove(GameBoard board) {
        try {
            board.placeMove(6, 6, GameSymbol.O);
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }
    }
}
