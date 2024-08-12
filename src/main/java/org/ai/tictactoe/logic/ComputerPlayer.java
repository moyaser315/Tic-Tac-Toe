package org.ai.tictactoe.logic;

public class ComputerPlayer implements Player {
    private int difficulty;

    public ComputerPlayer(int difficulty) {
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
