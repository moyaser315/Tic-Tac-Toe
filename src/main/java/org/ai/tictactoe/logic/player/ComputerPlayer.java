package org.ai.tictactoe.logic.player;

import org.ai.tictactoe.logic.game.GameBoard;
import org.ai.tictactoe.logic.game.GameSymbol;

public class ComputerPlayer extends Player {
    private final int difficulty;

    public ComputerPlayer(GameSymbol sym, int diff) {
        super(sym);
        difficulty = diff;
    }

    public void play(GameBoard b) {
        System.out.println("Computer is thinking!");
        int[] move = {0, -1, -1};
        for (int depth = 1; depth <= difficulty; depth++) {
            System.out.println("iterating..");
            int[] play = MinMax.optimalMove(b, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, s);
            if (s == GameSymbol.O) {
                if (move[1] == -1 || play[0] > move[0]) {
                    move = play;
                }
            } else {
                if (move[1] == -1 || play[0] < move[0]) {
                    move = play;
                }
            }

            if (s == GameSymbol.O) {
                if (move[0] == Integer.MAX_VALUE - 10) {
                    break;
                }
            } else {
                if (move[0] == -(Integer.MAX_VALUE - 10)) {
                    break;
                }
            }
        }


        b.place(s, move[1], move[2]);
        System.out.println("Computer played\n");
    }
}
