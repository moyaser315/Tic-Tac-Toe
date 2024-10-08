package org.ai.tictactoe.logic.player;

import org.ai.tictactoe.logic.game.GameBoard;
import org.ai.tictactoe.logic.game.GameStatus;
import org.ai.tictactoe.logic.game.GameSymbol;
import org.ai.tictactoe.logic.game.Judger;

public class MinMax {
    public static int[] optimalMove(GameBoard b, int alpha, int beta, int depth, GameSymbol s) {
        if (b.complete() || depth <= 0 || s == null) { // Root node
            return new int[] { 0, -1, -1 }; // score achieved & by which move
        }

        int[] bestMov = { -1, -1 }, curLastPlay = b.getLastPlay();
        for (int row = 0; row < b.size(); row++) {
            for (int col = 0; col < b.size(); col++) {
                if (b.isValidPosition(row, col) && b.isEmpty(row, col)) {
                    b.place(s, row, col);
                    GameStatus playState = Judger.getJudger().getStateAferPlay(b, row, col);
                    int playScore = Judger.getJudger().getScore(b, row, col);

                    int[] opp = null;
                    if (playState == GameStatus.X_WIN || playState == GameStatus.O_WIN || playState == GameStatus.DRAW) {
                        opp = optimalMove(b, alpha, beta, 0, s == GameSymbol.O ? GameSymbol.X : GameSymbol.O);
                    } else {
                        opp = optimalMove(b, alpha, beta, depth - 1, s == GameSymbol.O ? GameSymbol.X : GameSymbol.O);
                    }
                    b.remove(row, col);
                    b.setLastPlay(curLastPlay[0], curLastPlay[1]);

                    if (opp[1] == -1 && opp[2] == -1) { // opponent didn't play cause he depth is done
                        opp[0] = playScore; // consider the score of my play
                    } else if (opp[1] == -2 && opp[2] == -2) {
                        continue;
                    }
                    

                    if (s == GameSymbol.O) {
                        if (opp[0] > alpha) {
                            alpha = opp[0];
                            bestMov[0] = row;
                            bestMov[1] = col;
                        }
                    } else {
                        if (opp[0] < beta) {
                            beta = opp[0];
                            bestMov[0] = row;
                            bestMov[1] = col;
                        }
                    }

                    if (alpha >= beta) {
                        return new int[] { 0, -2, -2 }; // pruned
                    }
                }
            }
        }

        return new int[] { s == GameSymbol.O ? alpha : beta, bestMov[0], bestMov[1] };
    }
}