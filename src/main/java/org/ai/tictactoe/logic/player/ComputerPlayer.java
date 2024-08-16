package org.ai.tictactoe.logic.player;

import org.ai.tictactoe.logic.game.GameBoard;
import org.ai.tictactoe.logic.game.GameSymbol;

import java.util.Random;

public class ComputerPlayer extends Player {
    private final int difficulty;

    public ComputerPlayer(GameSymbol sym, int diff) {
        super(sym);
        difficulty = diff;
    }

    public void play(GameBoard b) {
        int[] move = MinMax.optimalMove(b, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, s);
        for (int depth = 2; depth <= difficulty; depth++) {
            int[] play = MinMax.optimalMove(b, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, s);
            if (s == GameSymbol.O) {
                if (play[0] > move[0]) {
                    move = play;
                }
            } else {
                if (play[0] < move[0]) {
                    move = play;
                }
            }
            System.out.println("iterating");
            if (s == GameSymbol.O) {
                if (play[0] == Integer.MAX_VALUE - 10 || play[0] == Integer.MAX_VALUE - 20) {
                    break;
                }
            } else {
                if (play[0] == -(Integer.MAX_VALUE - 10) || play[0] == -(Integer.MAX_VALUE - 20)) {
                    break;
                }
            }
        }

        System.out.println("End iterative");
        if (move[1] == -1 || move[2] == -1) {
            while (!b.isEmpty(move[1], move[2])) {
                move[1] = new Random().nextInt() % 7;
                move[2] = new Random().nextInt() % 7;
            }
            System.out.println("Random move");
        }

        b.place(s, move[1], move[2]);
    }
}
